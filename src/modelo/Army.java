package modelo;

/**
 * Workforce-backed military unit belonging to an {@link Empire}.
 * Tracks soldiers, upkeep (hiring funds), armory upgrades, and exposes hooks
 * to join battles or accept worker assignments as a workpost.
 */
public class Army extends Entidade implements WorkpostInterface{

  private static final int ARMORY_IRON_COST = 25;
  private static final int ARMORY_GOLD_COST = 5;

  private final Empire empire;
  private boolean inBattle = false;
  protected int armoryLevel = 1;
  protected int hiringLevel = 1;
  protected int hiringCost = 0;
  protected int soldiersAmount = 1;

  Battle currentBattle;


  public Army(Empire empire, int id) {
    super(id);
    this.empire = empire;
  }

  public boolean isBattling() {
    return inBattle;
  }

  public int getWorkers() {
    return soldiersAmount;
  }

  /**
   * In order to increase a level of of armory, it is necessary IRON_COST irons and GOLD_COST gold. This method will upgrade as much as it can.
   * @param intendedPoints Amount of points that is trying to upgrade.
   * @return Amount of points effectively added.
   */
  public int upgradeArmory(int intendedPoints, Empire empire) {
    int iron = Math.min(empire.getIron(), intendedPoints * ARMORY_IRON_COST);
    int gold = Math.min(empire.getGold(), intendedPoints * ARMORY_GOLD_COST);
    int ironPacks = iron / ARMORY_IRON_COST;
    int goldPacks = gold / ARMORY_GOLD_COST;

    int pointsAdded = Math.min(ironPacks, goldPacks);
    armoryLevel += pointsAdded;
    hiringLevel = armoryLevel;

    empire.setIron((empire.getIron() - ARMORY_IRON_COST * pointsAdded));
    empire.setGold(empire.getGold() - (ARMORY_GOLD_COST * pointsAdded));

    return pointsAdded;
  }

  /** @return Gold difference between previous hiring cost and new hiring cost  (if this methos is called outside time_update_army,
   * the diference must be added to the empire's gold.
   */
  public int changeHiringFunds(int cyclicalGold) {
    if(isBattling()) return 0;
    int previousHiringCost = hiringCost;

    hiringLevel = (int) auxiliar.LogCalculator.logBase(cyclicalGold, 1.055D);
    hiringCost = (int) Math.pow(1.095D, hiringLevel);
    hiringLevel = Math.max(hiringLevel, 1);
    return previousHiringCost - hiringCost;
  }

  // Retorna quantos trabalhadores entraram
  public int sendWorkers(int amount) {
    if(isBattling()) return 0;
    int previousSoldiers = soldiersAmount;
    soldiersAmount += amount;
    return soldiersAmount - previousSoldiers;
  }
  
  public int takeWorkers(int amount) {
    if(isBattling()) return 0;
    int previousSoldiers = soldiersAmount;
    soldiersAmount = Math.max(0, soldiersAmount - amount);
    return previousSoldiers - soldiersAmount;
  }

  public void timeUpdateArmy(Empire empire) {

    // Manages army payment
    if (empire.getGold() < hiringCost) {
      changeHiringFunds(empire.getGold());
      empire.setGold(empire.getGold() - hiringCost);
    } else {
      empire.setGold(empire.getGold() - hiringCost);
    }
  }

  public int getEmpireId() {
    return empire.getId();
  }

  public int getSoldiersAmount() {
    return soldiersAmount;
  }

  public int getHiringLevel(){ return hiringLevel; }
  public int getHiringCost(){ return hiringCost; }

  @Override
  public String toString() {
    return String.format("Army #%d | Empire %d %s | Armory level: %d | Soldiers amount: %d | Hiring cost: %d | Hiring level: %d",
            super.getId(), getEmpireId(), empire.getName(), armoryLevel, soldiersAmount, hiringCost, hiringLevel);
  }

  // --- INNER CLASSES

  /**
   * The general is the head of an army and can certainly decide the fate of a battle.
   * While the general is alive, his charisma maintains soldiers' morale high, avoiding desertion durring battle.
   * <p>
   * An army shall have at most 1 general, and never engage in battle when having none.
   */
  class General extends Soldier {
    protected int charisma;

    public General() {
      this.hp = 20 + (int)(Math.random() * 100);

      this.dexterity = (int)(Math.random() * (hiringLevel + 30));

      this.charisma = (int) Math.min(Math.random() * (5.1 * (Math.log(hiringLevel) * Math.log(hiringLevel))), 100D);

      this.armorFactor = Math.min(0.99D, (auxiliar.LogCalculator.logBase(hiringLevel, 140) + 0.01D));

      this.damage = (int) auxiliar.LogCalculator.logBase((2 * armoryLevel + hiringLevel) / 3, 2);

      this.morale = hiringLevel * 100;
    }

    public boolean isDead() {
      return hp <= 0;
    }

    @Override
    public void flee() {
      super.flee();
      charisma = 0;
    }

    @Override
    public boolean getHit(int damage) {
      receiveDamage(damage);

      if (hp == 0) {
        charisma = 0;
        return true;
      } else return false;
    }
  }
  General general;



  /** The class soldier is only instantiated inside Battle class. His values are then generated randomly accordingly with his army status. */
  class Soldier {
    protected int hp;
    protected int dexterity;
    protected int damage;
    protected double armorFactor;
    protected int morale;
    protected int idx;

    public Soldier() {};

    public Soldier(General general) {
      hp = (int)(Math.random() * 10);
      dexterity = (int)(Math.random() * (hiringLevel + 20));
      damage = (int)(Math.random() * ((hiringLevel + armoryLevel) / 10 + 10));
      armorFactor = Math.min(0.9, Math.random() * auxiliar.LogCalculator.logBase(hiringLevel, 200));
      morale = (int)(1.5 * (hiringLevel)) + general.charisma;
    }

    int getHp() {
      return hp;
    }
    int getDexterity() {
      return dexterity;
    }
    int getDamage() {
      return damage;
    }
    public double getArmorFactor() {
      return armorFactor;
    }
    public int getIdx() {
      return idx;
    }
    public void setIdx(int ind) {
      idx = ind;
    }


    public boolean getHit(int damage) {

      receiveDamage((damage));

      if (hp == 0) return true;
      else return false;
    }

    protected void receiveDamage(int damage) {
      if (damage < 0) {
        throw new IllegalArgumentException("It's not possible to receive negative damage.");
      }

      int realDamage = (int) Math.max((damage - (damage * armorFactor)), 1);
      hp = Math.max(0, hp - realDamage);
    }

    public boolean hit(Soldier target) {
      return target.getHit(damage);
    }

    public void flee() {
      this.hp = 0;
    }

  }

}
