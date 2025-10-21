package modelo;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Represents a battle between two armies, an attacker and a defender.
 * This class manages the state of the battle, including soldier lists,
 * generals, and the simulation of combat rounds.
 * The composition relation is the soldier class: although not declared here, for sake of organization,
 * it is only instantiated inside Battle class.
 */
public class Battle extends Entidade {
  /** The attacking army in the battle. */
  private Army attacker;
  /** The defending army in the battle. */
  private Army defender;

  /** The general of the attacking army. */
  private Army.General attackerGeneral;
  /** The general of the defending army. */
  private Army.General defenderGeneral;

  /** A list of all soldiers fighting for the attacker. */
  private ArrayList < Army.Soldier > attackerSoldiers = new ArrayList < > ();
  /** The count of currently living soldiers for the attacker. */
  private int attackerSoldiersAlive;
  /** A list of all soldiers fighting for the defender. */
  private ArrayList < Army.Soldier > defenderSoldiers = new ArrayList < > ();
  /** The count of currently living soldiers for the defender. */
  private int defenderSoldiersAlive;

  /** Reference to the database for persistence operations. */
  private persistencia.BancoDeDados db;

  /**
   * Constructs a new Battle instance.
   * Initializes the battle by creating generals for each army and populating
   * the soldier lists based on the armies' sizes.
   *
   * @param attacker The army initiating the attack.
   * @param defender The army defending against the attack.
   * @param db The database instance.
   */
  public Battle(Army attacker, Army defender, persistencia.BancoDeDados db) {
    super(db.nextBattle());
    this.db = db;
    this.attacker = attacker;
    this.defender = defender;

    attacker.general = attacker.new General();
    defender.general = defender.new General();

    attackerGeneral = attacker.general;
    defenderGeneral = defender.general;
    attackerSoldiers.add(attackerGeneral);
    defenderSoldiers.add(defenderGeneral);


    for (int i = 0; i < attacker.getSoldiersAmount(); i++) {
      attackerSoldiers.add(attacker.new Soldier(attackerGeneral));
      attackerSoldiers.getLast().morale += 5; // Attackers get a morale bonus
    }
    attackerSoldiersAlive = attackerSoldiers.size();

    for (int i = 0; i < defender.getSoldiersAmount(); i++) {
      defenderSoldiers.add(defender.new Soldier(defenderGeneral));
    }
    defenderSoldiersAlive = defenderSoldiers.size();
  }

  /**
   * Removes this battle instance from the database.
   */
  public void destroy() {
    db.getBattles().remove(super.getId());
  }

  /**
   * Gets the attacking army.
   * @return The attacker Army object.
   */
  public Army getAttacker() {
    return attacker;
  }

  /**
   * Gets the defending army.
   * @return The defender Army object.
   */
  public Army getDefender() {
    return defender;
  }

  /**
   * Gets the number of living soldiers on the attacker's side.
   * @return The count of alive attacker soldiers.
   */
  public int getAttackerSoldiersAlive() {
    return attackerSoldiersAlive;
  }

  /**
   * Gets the number of living soldiers on the defender's side.
   * @return The count of alive defender soldiers.
   */
  public int getDefenderSoldiersAlive() {
    return defenderSoldiersAlive;
  }

  /**
   * This class simulates a round in which every soldier has the possibility of attacking.
   * <p>
   * All alive soldiers are thrown in a priority queue heap of max. The priority factor is randomly determined and directly proportional to each soldier's dexterity.
   * Then, until the priority queue is not empty, the top of the pq is popped and attacks a random soldier of the opposite army. This soldier does not attack in this round anymore.
   * There is a chance that a soldier attacks the general of the opposite army.
   *
   * @return -1 if defender won; 0 if there was no winner; 1 if the attacker won. A battle is won when all soldiers of an army have hp <= 0.
   */
  public int simulateRound() {

    PriorityQueue < TurnOrder > next = new PriorityQueue < > ();
    populateQueue(next);

    if (defenderSoldiersAlive <= 0) return 1;
    if (attackerSoldiersAlive <= 0) return -1;

    while (next.size() > 0) {
      TurnOrder currentTurn = next.poll();
      int currentIndex = currentTurn.idx;
      boolean isAttacker = currentTurn.isAttacker;

      //Relativize targets according to current soldier.
      ArrayList < Army.Soldier > targetArmy = isAttacker ? defenderSoldiers : attackerSoldiers;
      Army.Soldier currentSoldier = isAttacker ? attackerSoldiers.get(currentIndex) : defenderSoldiers.get(currentIndex);
      Army.General allyGeneral = !isAttacker ? defenderGeneral : attackerGeneral;

      if (currentSoldier.getHp() <= 0) {
        continue;
      }

      //Decide if the soldier will flee
      if (shouldSoldierFlee(currentSoldier, allyGeneral, isAttacker)) {
        currentSoldier.flee();
        if (isAttacker) attackerSoldiersAlive--;
        else defenderSoldiersAlive--;
        continue;
      }

      // Chooses a random direction and searches for the first soldier alive in that direction. The searching is circular.
      int battleStatus = attackAdjacentRandomEnemy(currentSoldier, isAttacker, targetArmy);

      //If someone won (last soldier of an army killed), return the winner (attacker 1, defender -1);
      if (battleStatus != 0) {
        return battleStatus;
      }
    }
    return 0;
  }

  /**
   * Populates the priority queue with all living soldiers from both armies to determine the turn order for the round.
   * Also updates the counts of living soldiers for both sides.
   *
   * @param next The priority queue to be populated.
   */
  private void populateQueue(PriorityQueue < TurnOrder > next) {
    attackerSoldiersAlive = 0;
    defenderSoldiersAlive = 0;

    for (int i = 0; i < attackerSoldiers.size(); i++) {
      if (attackerSoldiers.get(i).getHp() <= 0) continue;
      attackerSoldiers.get(i).setIdx(i);
      next.add(new TurnOrder(i, (int)(Math.random() * attackerSoldiers.get(i).getDexterity()), true));
      attackerSoldiersAlive++;
    }

    for (int i = 0; i < defenderSoldiers.size(); i++) {
      if (defenderSoldiers.get(i).getHp() <= 0) continue;
      defenderSoldiers.get(i).setIdx(i);
      next.add(new TurnOrder(i, (int) Math.random() * defenderSoldiers.get(i).getDexterity(), false));
      defenderSoldiersAlive++;
    }
  }

  /**
   * Determines if a soldier should flee based on their morale, the status of their general,
   * and the difference in army sizes.
   *
   * @param currentSoldier The soldier being checked.
   * @param allyGeneral The general of the soldier's army.
   * @param isAttacker True if the soldier is from the attacking army, false otherwise.
   * @return True if the soldier should flee, false otherwise.
   */
  private boolean shouldSoldierFlee(Army.Soldier currentSoldier, Army.General allyGeneral, boolean isAttacker) {
    if (allyGeneral.isDead()) allyGeneral.charisma = 0;
    int armyDifference = isAttacker ? defenderSoldiersAlive - attackerSoldiersAlive : attackerSoldiersAlive - defenderSoldiersAlive;
    int fearFactor = Math.max(0, armyDifference / 2 - allyGeneral.charisma);
    int moraleRequired = (int)(Math.random() * (fearFactor + 1));

    return currentSoldier.morale < moraleRequired;
  }

  /**
   * Makes a soldier attack a random adjacent enemy that is still alive.
   * The search for a living target is circular.
   *
   * @param currentSoldier The soldier who is attacking.
   * @param isAttacker True if the soldier is from the attacking army.
   * @param targetArmy The list of soldiers from the opposing army.
   * @return 1 if the attacker won, -1 if the defender won, 0 for no winner yet.
   */
  private int attackAdjacentRandomEnemy(Army.Soldier currentSoldier, boolean isAttacker, ArrayList < Army.Soldier > targetArmy) {
    int direction = Math.random() * 10 < 5 ? -1 : 1;
    int aim = (currentSoldier.getIdx() + direction + targetArmy.size()) % targetArmy.size();
    for (int i = 0; i < targetArmy.size(); i++) {
      if (targetArmy.get(aim).getHp() > 0) break;
      else aim = (aim + direction + targetArmy.size()) % targetArmy.size();
    }

    // This case happens if the last enemy was just killed by fleeing
    if (targetArmy.get(aim).getHp() <= 0) {
      if (isAttacker) return 1;
      else return -1;
    }

    if (currentSoldier.hit(targetArmy.get(aim))) {
      if (isAttacker) {
        defenderSoldiersAlive--;
        if (defenderSoldiersAlive <= 0) return 1;
      } else {
        attackerSoldiersAlive--;
        if (attackerSoldiersAlive <= 0) return -1;
      }
    }
    return 0;
  }

  /**
   * Adds a specified number of new soldiers to an army mid-battle.
   *
   * @param is_attacker True to add to the attacker's army, false for the defender's.
   * @param amount The number of soldiers to add.
   */
  public void insertSoldier(Boolean is_attacker, int amount) {
    if (amount <= 0) return;

    ArrayList < Army.Soldier > receiverSoldiers = is_attacker ? attackerSoldiers : defenderSoldiers;
    Army receiverArmy = is_attacker ? attacker : defender;

    for (int i = 0; i < amount; i++) {
      receiverSoldiers.add(receiverArmy.new Soldier(receiverArmy.general));
    }

    if (is_attacker) attackerSoldiersAlive += amount;
    else defenderSoldiersAlive += amount;
  }

  /**
   * Gets the total size of an army's soldier list (both alive and dead).
   *
   * @param is_attacker True to get the attacker's army size, false for the defender's.
   * @return The total number of soldiers in the specified army's list.
   */
  public int getSoldiersSize(Boolean is_attacker) {
    if (is_attacker) return attackerSoldiers.size();
    else return defenderSoldiers.size();
  }

  /**
   * Deletes a soldier from an army's list at a specific position.
   *
   * @param is_attacker True to delete from the attacker's army, false for the defender's.
   * @param pos The index of the soldier to remove.
   * @return True if the deletion was successful, false if the index was out of bounds.
   */
  public Boolean deleteSoldier(Boolean is_attacker, int pos) {
    if (is_attacker) {
      if (pos >= attackerSoldiers.size()) return false;
      if (attackerSoldiers.get(pos).hp > 0) attackerSoldiersAlive--;
      attackerSoldiers.remove(pos);

    } else {
      if (pos >= defenderSoldiers.size()) return false;
      if (defenderSoldiers.get(pos).hp > 0) defenderSoldiersAlive--;
      defenderSoldiers.remove(pos);
    }
    return true;
  }

  /**
   * Returns a string representation of the Battle's current state.
   * @return A formatted string with battle and army details.
   */
  @Override
  public String toString() {
    return String.format("Batalha %d | Exército atacante %d | Exército defensor %d | Soldados atacantes vivos %d | Soldados defensores vivos %d\n",
      super.getId(), attacker.getId(), defender.getId(), attackerSoldiersAlive, defenderSoldiersAlive);
  }
}

/**
 * A helper class to manage the turn order of soldiers in a battle round.
 * Implements Comparable to be used in a PriorityQueue, where higher priority
 * soldiers act first.
 */
class TurnOrder implements Comparable < TurnOrder > {
  /** The index of the soldier in their respective army list. */
  int idx;
  /** The priority value determining turn order. Higher values go first. */
  int priority;
  /** A flag indicating if the soldier belongs to the attacker (true) or defender (false). */
  boolean isAttacker;

  /**
   * Constructs a TurnOrder object.
   *
   * @param idx The index of the soldier.
   * @param priority The calculated priority for this turn.
   * @param isAttacker True if the soldier is an attacker, false otherwise.
   */
  public TurnOrder(int idx, int priority, boolean isAttacker) {
    this.idx = idx;
    this.priority = priority;
    this.isAttacker = isAttacker;
  }

  /**
   * Compares this TurnOrder object with another to determine precedence in a priority queue.
   *
   * @param other The other TurnOrder object to compare against.
   * @return A negative integer, zero, or a positive integer as this object's priority
   * is less than, equal to, or greater than the specified object's priority.
   */
  @Override
  public int compareTo(TurnOrder other) {
    // Note: This implementation will result in a min-heap by default.
    // To make a max-heap (higher priority first), the comparison should be reversed,
    // or the PriorityQueue should be initialized with a reverse order comparator.
    // Example for max-heap: return Integer.compare(other.priority, this.priority);
    return Integer.compare(this.priority, other.priority);
  }

  /**
   * Returns a string representation of the TurnOrder object.
   * @return A string showing the soldier index and priority.
   */
  @Override
  public String toString() {
    return "[Soldier " + idx + ", priority=" + priority + "]";
  }
}