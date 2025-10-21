package modelo;


import persistencia.BancoDeDados;

/**
 * Represents an Army, a military unit belonging to an Empire.
 * An army contains soldiers and a general, and can engage in battles.
 * It has attributes like armory level and hiring level which determine its strength.
 */
public class Army extends Entidade {

  /** The ID of the Empire this army belongs to. */
  int empireId = -1;

  /** Flag indicating if the army is currently engaged in a battle. */
  Boolean inBattle = false;

  /** The level of the army's armory, influencing soldier equipment and stats. */
  protected int armoryLevel = 1;

  /** The level of recruitment facilities, affecting soldier quality and cost. */
  protected int hiringLevel = 1;

  /** The cyclical gold cost required to maintain the army. */
  protected int hiringCost = 1;

  /** The total number of soldiers in this army. */
  protected int soldiersAmount = 1;

  /** The current battle the army is engaged in. Null if not in battle. */
  Battle currentBattle;

  /** Reference to the database instance for data operations. */
  private BancoDeDados db;

  /**
   * Constructs a new Army for a given empire.
   *
   * @param empireId The ID of the empire that owns this army.
   * @param db The database instance used for persistence.
   */
  public Army(int empireId, BancoDeDados db) {
    super(db.nextArmy());
    this.db = db;
    this.empireId = empireId;
    this.general = new General();
  }

  /**
   * Destroys the army, removing it from the database and returning its
   * soldiers to the parent empire's population.
   */
  public void destroy() {
    db.getArmies().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(soldiersAmount);
  }

  /**
   * Checks if the army is currently in a battle.
   * @return true if the army is battling, false otherwise.
   */
  public boolean isBattling() {
    return inBattle;
  }

  /**
   * Gets the number of soldiers in the army.
   * @return The total number of soldiers.
   */
  public int getWorkers() {
    return soldiersAmount;
  }

  /** The cost in iron to upgrade the armory by one level. */
  public final int IRON_COST_ARMORY = 25;
  /** The cost in gold to upgrade the armory by one level. */
  public final int GOLD_COST_ARMORY = 5;

  /**
   * In order to increase a level of of armory, it is necessary IRON_COST irons and GOLD_COST gold. This method will upgrade as much as it can.
   * @param intendedPoints Amount of points that is trying to upgrade.
   * @param empire The empire funding the upgrade.
   * @return Amount of points effectively added.
   */
  public int upgradeArmory(int intendedPoints, Empire empire) {
    int iron = Math.min(empire.getIron(), intendedPoints * IRON_COST_ARMORY);
    int gold = Math.min(empire.getGold(), intendedPoints * GOLD_COST_ARMORY);
    int ironPacks = iron / IRON_COST_ARMORY;
    int goldPacks = gold / GOLD_COST_ARMORY;

    int pointsAdded = Math.min(ironPacks, goldPacks);
    armoryLevel += pointsAdded;
    hiringLevel = armoryLevel;

    empire.setIron((empire.getIron() - IRON_COST_ARMORY * pointsAdded));
    empire.setGold(empire.getGold() - (GOLD_COST_ARMORY * pointsAdded));

    return pointsAdded;
  }

  /**
   * Recalculates the hiring level and cost based on the available gold for funding.
   *
   * @param cyclicalGold The amount of gold available to fund the army.
   * @return Gold difference between previous hiring cost and new hiring cost  (if this method is called outside time_update_army,
   * the difference must be added to the empire's gold.
   */
  public int changeHiringFunds(int cyclicalGold) {
    int previousHiringCost = hiringCost;

    hiringLevel = (int) auxiliar.LogCalculator.logBase(cyclicalGold, 1.055D);
    hiringCost = (int) Math.pow(1.055D, hiringLevel);
    return hiringCost - previousHiringCost;
  }

  /**
   * Adds a specified number of soldiers to the army.
   *
   * @param amount The number of soldiers to add.
   * @return The actual number of soldiers that were added.
   */
  public int sendWorkers(int amount) {
    int previousSoldiers = soldiersAmount;
    soldiersAmount += amount;
    return soldiersAmount - previousSoldiers;
  }

  /**
   * Removes a specified number of soldiers from the army.
   * The number of soldiers will not go below zero.
   *
   * @param amount The number of soldiers to remove.
   * @return The actual number of soldiers that were removed.
   */
  public int takeWorkers(int amount) {
    int previousSoldiers = soldiersAmount;
    soldiersAmount = Math.max(0, soldiersAmount - amount);
    return previousSoldiers - soldiersAmount;
  }

  /**
   * Performs a periodic update for the army, primarily managing maintenance costs.
   * It deducts the hiring cost from the empire's gold reserves.
   * If the empire cannot afford the cost, the army's funding is adjusted.
   *
   * @param empire The empire that owns this army.
   */
  public void timeUpdateArmy(Empire empire) {

    // Manages army payment
    if (empire.getGold() < hiringCost) {
      changeHiringFunds(empire.getGold());
      empire.setGold(empire.getGold() - hiringCost);
    } else {
      empire.setGold(empire.getGold() - hiringCost);
    }
  }

  /**
   * Gets the ID of the empire that owns this army.
   * @return The empire's ID.
   */
  public int getEmpireId() {
    return empireId;
  }

  /**
   * Gets the current number of soldiers in the army.
   * @return The total number of soldiers.
   */
  public int getSoldiersAmount() {
    return soldiersAmount;
  }

  /**
   * Returns a string representation of the Army object.
   * @return A string containing the army's details.
   */
  @Override
  public String toString() {
    return "{" + super.toString() + " | " + String.format("Army #%d | Empire %d %s | Armory level: %d| Soldiers amount: %d}",
      super.getId(), empireId, ((Empire)db.getEmpires().findById(empireId)).getName(), armoryLevel, hiringLevel, hiringCost, soldiersAmount);
  }

  // --- INNER CLASSES

  /** The general leading this army. */
  General general;

  /**
   * The general is the head of an army and can certainly decide the fate of a battle.
   * While the general is alive, his charisma maintains soldiers' morale high, avoiding desertion during battle.
   * <p>
   * An army shall have at most 1 general, and never engage in battle when having none.
   */
  class General extends Soldier {
    /** The general's ability to inspire troops and maintain morale. */
    protected int charisma;

    /**
     * Constructs a new General, with stats calculated based on the army's
     * current hiring and armory levels.
     */
    public General() {
      this.hp = 20 + (int)(Math.random() * 100);
      this.dexterity = (int)(Math.random() * (hiringLevel + 30));
      this.charisma = (int) Math.min(Math.random() * (5.1 * (Math.log(hiringLevel) * Math.log(hiringLevel))), 100D);
      this.armorFactor = Math.min(0.99D, (auxiliar.LogCalculator.logBase(hiringLevel, 140) + 0.01D));
      this.damage = (int) auxiliar.LogCalculator.logBase((2 * armoryLevel + hiringLevel) / 3, 2);
      this.morale = hiringLevel * 100;
    }

    /**
     * Checks if the general has fallen in battle.
     * @return true if the general's health is 0 or less, false otherwise.
     */
    public boolean isDead() {
      return hp <= 0;
    }

    /**
     * Causes the general to flee, setting HP and charisma to 0.
     */
    @Override
    public void flee() {
      super.flee();
      charisma = 0;
    }

    /**
     * Applies damage to the general from an attack.
     * If the hit is fatal, the general's charisma is set to 0.
     *
     * @param damage The amount of damage to inflict.
     * @return true if the damage was fatal, false otherwise.
     */
    @Override
    public boolean getHit(int damage) {
      receiveDamage(damage);

      if (hp <= 0) { // Changed to <= for robustness
        charisma = 0;
        return true;
      } else return false;
    }
  }


  /**
   * Represents a single soldier within an army.
   * The class soldier is only instantiated inside the Battle class. His values are then generated randomly accordingly with his army status.
   */
  class Soldier {
    /** The health points of the soldier. */
    protected int hp;
    /** The dexterity of the soldier, affecting hit chance or evasion. */
    protected int dexterity;
    /** The amount of damage the soldier can inflict. */
    protected int damage;
    /** A factor from 0.0 to 1.0 representing damage reduction from armor. */
    protected double armorFactor;
    /** The soldier's morale, affecting their performance in battle. */
    protected int morale;
    /** An index identifier for the soldier, often used within a battle context. */
    protected int idx;

    /**
     * Default constructor for a Soldier.
     */
    public Soldier() {};

    /**
     * Constructs a new Soldier with stats influenced by the army's levels and the general's charisma.
     * @param general The general of the army this soldier belongs to.
     */
    public Soldier(General general) {
      hp = (int)(Math.random() * 10);
      dexterity = (int)(Math.random() * (hiringLevel + 20));
      damage = (int)(Math.random() * ((hiringLevel + armoryLevel) / 10 + 10));
      armorFactor = Math.min(0.9, Math.random() * auxiliar.LogCalculator.logBase(hiringLevel, 200));
      morale = (int)(1.5 * (hiringLevel)) + general.charisma;
    }

    /**
     * Gets the soldier's current health points.
     * @return The current HP.
     */
    int getHp() {
      return hp;
    }

    /**
     * Gets the soldier's dexterity.
     * @return The dexterity value.
     */
    int getDexterity() {
      return dexterity;
    }

    /**
     * Gets the soldier's damage value.
     * @return The damage value.
     */
    int getDamage() {
      return damage;
    }

    /**
     * Gets the soldier's armor factor for damage reduction.
     * @return The armor factor.
     */
    public double getArmorFactor() {
      return armorFactor;
    }

    /**
     * Gets the index of the soldier.
     * @return The soldier's index.
     */
    public int getIdx() {
      return idx;
    }

    /**
     * Sets the index of the soldier.
     * @param ind The new index for the soldier.
     */
    public void setIdx(int ind) {
      idx = ind;
    }

    /**
     * Inflicts damage on the soldier.
     *
     * @param damage The amount of incoming damage.
     * @return true if the damage was fatal, false otherwise.
     */
    public boolean getHit(int damage) {
      receiveDamage(damage);
      return hp <= 0;
    }

    /**
     * Calculates the actual damage taken after armor reduction and applies it to the soldier's HP.
     *
     * @param damage The raw damage value before armor is applied.
     * @throws IllegalArgumentException if the incoming damage is negative.
     */
    protected void receiveDamage(int damage) {
      if (damage < 0) {
        throw new IllegalArgumentException("It's not possible to receive negative damage.");
      }
      int realDamage = (int) Math.max((damage - (damage * armorFactor)), 1);
      hp = Math.max(0, hp - realDamage);
    }

    /**
     * The soldier attacks a target soldier.
     *
     * @param target The soldier being attacked.
     * @return true if the attack was fatal to the target, false otherwise.
     */
    public boolean hit(Soldier target) {
      return target.getHit(damage);
    }

    /**
     * Causes the soldier to flee the battle by setting HP to 0.
     */
    public void flee() {
      this.hp = 0;
    }
  }
}