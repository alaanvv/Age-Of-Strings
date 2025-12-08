package modelo;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Simulates combat between two {@link Army} instances.
 * Manages per-soldier initiative via a priority queue, handles morale/fleeing
 * logic, and determines victory by tracking alive soldiers on each side.
 */
public class Battle extends Entidade {
  private Army attacker, defender;

  private Army.General attackerGeneral;
  private Army.General defenderGeneral;

  private ArrayList < Army.Soldier > attackerSoldiers = new ArrayList < > ();
  private int attackerSoldiersAlive;
  private ArrayList < Army.Soldier > defenderSoldiers = new ArrayList < > ();
  private int defenderSoldiersAlive;

  public Battle(Army attacker, Army defender, int id) {
    super(id);
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
      attackerSoldiers.get(attackerSoldiers.size() - 1).morale += 5;
    }
    attackerSoldiersAlive = attackerSoldiers.size();

    for (int i = 0; i < defender.getSoldiersAmount(); i++) {
      defenderSoldiers.add(defender.new Soldier(defenderGeneral));
    }
    defenderSoldiersAlive = defenderSoldiers.size();
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

  public void insertSoldier(Boolean is_attacker, int amount){
    if(amount <= 0) return;

    ArrayList<Army.Soldier> receiverSoldiers = is_attacker? attackerSoldiers : defenderSoldiers;
    Army receiverArmy = is_attacker? attacker : defender;
    
    for(int i = 0; i < amount; i++){
      receiverSoldiers.add(receiverArmy.new Soldier(receiverArmy.general));
    }

    if(is_attacker) attackerSoldiersAlive += amount;
    else defenderSoldiersAlive += amount;
  }

  public int getSoldiersSize(Boolean is_attacker){
    if(is_attacker) return attackerSoldiers.size();
    else return defenderSoldiers.size();
  }

  public Boolean deleteSoldier(Boolean is_attacker, int pos){
    if(is_attacker){
      if(pos >= attackerSoldiers.size()) return false;
      if(attackerSoldiers.get(pos).hp > 0) attackerSoldiersAlive--;
      attackerSoldiers.remove(pos);
      
    }else{
      if(pos >= defenderSoldiers.size()) return false;
      if(defenderSoldiers.get(pos).hp > 0) defenderSoldiersAlive--;
      defenderSoldiers.remove(pos);
    }
    return true;
  }

  @Override
  public String toString(){
    return String.format("Batalha %d | Exército atacante %d | Exército defensor %d | Soldados atacantes vivos %d | Soldados defensores vivos %d\n",
    super.getId(), attacker.getId(), defender.getId(), attackerSoldiersAlive, defenderSoldiersAlive);
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
