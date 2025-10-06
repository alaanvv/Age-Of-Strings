package modelo;
import java.util.ArrayList;
import java.util.PriorityQueue;

/* This is the transaction class of the project. The composition relation is the soldier class: although not declared here, for sake of organization,
 * it is only instantiated inside Battle class.
*/
public class Battle extends Entidade {
  private Army attacker, defender;

  private Army.General attackerGeneral;
  private Army.General defenderGeneral;

  private ArrayList < Army.Soldier > attackerSoldiers = new ArrayList < > ();
  private int attackerSoldiersAlive;
  private ArrayList < Army.Soldier > defenderSoldiers = new ArrayList < > ();
  private int defenderSoldiersAlive;

  private persistencia.BancoDeDados db;

  public Battle(Army attacker, Army defender, persistencia.BancoDeDados db) {
    super(db.getBattles().getSize());
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
      attackerSoldiers.getLast().morale += 5;
    }
    attackerSoldiersAlive = attackerSoldiers.size();

    for (int i = 0; i < defender.getSoldiersAmount(); i++) {
      defenderSoldiers.add(defender.new Soldier(defenderGeneral));
    }
    defenderSoldiersAlive = defenderSoldiers.size();
  }

  public void destroy() {
    db.getBattles().remove(super.getId());
  }

  public Army getAttacker() {
    return attacker;
  }

  public Army getDefender() {
    return defender;
  }

  public int getAttackerSoldiersAlive() {
    return attackerSoldiersAlive;
  }

  public int getDefenderSoldiersAlive() {
    return defenderSoldiersAlive;
  }

  /**
   * This class simulates a round in which every soldier has the possibility of attacking.
   * <p>
   * All alive soldiers are thrown in a priority queue heap of max. The priority factor is randomly determined and directly proportional to each soldier's dexterity.
   * Then, untill priority queue is not empty, the top of the pq is popped and attacks a random soldier of the opposite army. This soldier does not attack in this round anymore.
   * There is a chance that a soldier attacks the general of the opposite army.
   *
   * @return -1 if defender won; 0 if there was no winner; 1 if the attacker won. A battle is won when all soldiers of an army has hp <= 0.
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

      //Decide se o soldado irá fugir
      if (shouldSoldierFlee(currentSoldier, allyGeneral, isAttacker)) {
        currentSoldier.flee();
        continue;
      }

      // Chooses a random direction and searches fo the first soldier alive in that direction. The searching is circular.
      int battleStatus = attackAdjacentRandomEnemy(currentSoldier, isAttacker, targetArmy);

      //If someone won (last soldier of an army killed), return the winner (attacker 1, defender -1);
      if (battleStatus != 0) {
        return battleStatus;
      }
    }
    return 0;
  }

  private void populateQueue(PriorityQueue < TurnOrder > next) {
    attackerSoldiersAlive = 0;
    defenderSoldiersAlive = 0;

    for (int i = 0; i < attacker.getSoldiersAmount(); i++) {
      if (attackerSoldiers.get(i).getHp() <= 0) continue;
      attackerSoldiers.get(i).setIdx(i);
      next.add(new TurnOrder(i, (int)(Math.random() * attackerSoldiers.get(i).getDexterity()), true));
      attackerSoldiersAlive++;
    }

    for (int i = 0; i < defender.getSoldiersAmount(); i++) {
      if (defenderSoldiers.get(i).getHp() <= 0) continue;
      defenderSoldiers.get(i).setIdx(i);
      next.add(new TurnOrder(i, (int) Math.random() * defenderSoldiers.get(i).getDexterity(), false));
      defenderSoldiersAlive++;
    }
  }


  private boolean shouldSoldierFlee(Army.Soldier currentSoldier, Army.General allyGeneral, boolean isAttacker) {
    if (allyGeneral.isDead()) allyGeneral.charisma = 0;
    int armyDifference = isAttacker ? defenderSoldiersAlive - attackerSoldiersAlive : attackerSoldiersAlive - defenderSoldiersAlive;
    int fearFactor = Math.max(0, armyDifference / 2 - allyGeneral.charisma);
    int moraleRequired = (int)(Math.random() * (fearFactor + 1));

    return currentSoldier.morale < moraleRequired;
  }

  private int attackAdjacentRandomEnemy(Army.Soldier currentSoldier, boolean isAttacker, ArrayList < Army.Soldier > targetArmy) {
    int direction = Math.random() * 10 < 5 ? -1 : 1;
    int aim = (currentSoldier.getIdx() + direction + targetArmy.size()) % targetArmy.size();
    for (int i = 0; i < targetArmy.size(); i++) {
      if (targetArmy.get(aim).getHp() > 0) break;
      else aim = (aim + direction + targetArmy.size()) % targetArmy.size();
    }

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

  void insertSoldier(Boolean is_attacker, int amount){
    ArrayList<Army.Soldier> receiverSoldiers = is_attacker? attackerSoldiers : defenderSoldiers;
    Army receiverArmy = is_attacker? attacker : defender;
    for(int i = 0; i < amount; i++){
      receiverSoldiers.addLast(receiverArmy.new Soldier());
    }
  }
}


class TurnOrder implements Comparable < TurnOrder > {
  int idx;
  int priority;
  boolean isAttacker;

  public TurnOrder(int idx, int priority, boolean isAttacker) {
    this.idx = idx;
    this.priority = priority;
    this.isAttacker = isAttacker;
  }

  @Override
  public int compareTo(TurnOrder other) {
    int ret = Integer.compare(this.priority, other.priority);
    return ret;
  }

  @Override
  public String toString() {
    return "[Soldier " + idx + ", priority=" + priority + "]";
  }

}
