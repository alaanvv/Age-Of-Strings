package modelo;
import java.util.ArrayList;
import java.util.PriorityQueue;
/* Esta é a classe de transação do projeto. O objeto de composição será batalhas acontecendo.*/

public class War extends Entidade{
   ArrayList<Battle> current_battles = new ArrayList<Battle>();
   
   
}

class Battle{
   private Army attacker, defender;
   
   private Army.General genattacker;
   private Army.General gendefender;
   
   private ArrayList<Army.Soldier> attacker_soldiers = new ArrayList<>();
   private int attacker_soldiers_alive;
   private ArrayList<Army.Soldier> defender_soldiers = new ArrayList<>();
   private int defender_soldiers_alive;
   
   
   Battle(Army attacker, Army defender){
      this.attacker = attacker;
      this.defender = defender;
      
      attacker.general = attacker. new General();
      defender.general = defender.new General();

      genattacker = attacker.general;
      gendefender = defender.general;
      attacker_soldiers.add(genattacker);
      defender_soldiers.add(gendefender);
      
      
      for(int i = 0; i < attacker.soldiers_amt; i++){
         attacker_soldiers.add(attacker. new Soldier(genattacker));
         attacker_soldiers.getLast().morale += 5;
      }
      attacker_soldiers_alive = attacker_soldiers.size();

      for(int i = 0; i < defender.soldiers_amt; i++){
         defender_soldiers.add(defender.new Soldier(gendefender));
      }
      defender_soldiers_alive = defender_soldiers.size();
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
   public int simulate_round(){
      
      PriorityQueue<TurnOrder> next = new PriorityQueue<>();
      populate_queue(next);

      if(defender_soldiers_alive <= 0) return 1;
      if(attacker_soldiers_alive <= 0) return -1;

      while(next.size() > 0){
         TurnOrder current_turn = next.poll();
         int current_idx = current_turn.idx;
         boolean is_attacker = current_turn.is_attacker;
         
         //Relativiza alvos de acordo com o exército do soldado atual.
         ArrayList<Army.Soldier> target_army = is_attacker? defender_soldiers : attacker_soldiers;
         Army.Soldier current_soldier = is_attacker? attacker_soldiers.get(current_idx) : defender_soldiers.get(current_idx);
         Army.General gen_ally = !is_attacker? gendefender : genattacker;

         if (current_soldier.get_hp() <= 0) {
            continue;
         }

         //Decide se o soldado irá fugir
         if(soldier_flee(current_soldier, gen_ally, is_attacker)){
            current_soldier.flee();
            continue;
         }
         
         // Escolhe uma direção aleatória e busca o primeiro soldado com vida nessa direção. Dá a volta no array.
         int battle_status = attack_adjacent_random_enemy(current_soldier, is_attacker, target_army);
         
         // If the attack resulted in a win/loss, end the round immediately.
         if (battle_status != 0) {
            return battle_status;
         }
      }
      return 0;
   }

   private void populate_queue(PriorityQueue<TurnOrder> next){
      attacker_soldiers_alive = 0;
      defender_soldiers_alive = 0;
      
      for(int i = 0; i < attacker.soldiers_amt; i++){
         if(attacker_soldiers.get(i).hp <= 0) continue;
         attacker_soldiers.get(i).set_idx(i);
         next.add(new TurnOrder(i, (int) (Math.random()*attacker_soldiers.get(i).dexterity), true));
         attacker_soldiers_alive++;
      }
      
      for(int i = 0; i < defender.soldiers_amt; i++){
         if(defender_soldiers.get(i).hp <= 0) continue;
         defender_soldiers.get(i).set_idx(i);
         next.add(new TurnOrder(i, (int) Math.random()*defender_soldiers.get(i).get_dexterity(), false));
         defender_soldiers_alive++;
      }
   }

   private boolean soldier_flee(Army.Soldier current_soldier, Army.General gen_ally, boolean is_attacker){
      if(gen_ally.is_dead()) gen_ally.charisma = 0;
      int army_diff = is_attacker? defender_soldiers_alive - attacker_soldiers_alive : attacker_soldiers_alive - defender_soldiers_alive;
      int fear_factor = Math.max(0, army_diff/2 - gen_ally.charisma); 
      int morale_required = (int)(Math.random() * (fear_factor + 1));
      
      return current_soldier.morale < morale_required;
   }

   private int attack_adjacent_random_enemy(Army.Soldier current_soldier, boolean is_attacker, ArrayList<Army.Soldier> target_army){
      int direction = Math.random() * 10 < 5? -1 : 1;
         int aim = (current_soldier.get_idx() + direction + target_army.size()) % target_army.size();
         for(int i = 0; i < target_army.size(); i++){
            if(target_army.get(aim).hp > 0) break;
            else aim = (aim + direction + target_army.size()) % target_army.size();
         }

         if(target_army.get(aim).hp <= 0){
            if(is_attacker) return 1;
            else return -1;
         }

         if(current_soldier.hit(target_army.get(aim))){
            if(is_attacker){
               defender_soldiers_alive--;
               if(defender_soldiers_alive <= 0) return 1;
            }else{
               attacker_soldiers_alive--;
               if(attacker_soldiers_alive <= 0) return -1;
            }
         }

         return 0;
   }
}


class TurnOrder implements Comparable<TurnOrder>{
   int idx;
   int priority;
   boolean is_attacker;

   public TurnOrder(int idx, int priority, boolean is_attacker){
      this.idx = idx;
      this.priority = priority;
      this.is_attacker = is_attacker;
   }

   @Override
   public int compareTo(TurnOrder other){
      int ret = Integer.compare(this.priority, other.priority);
      return ret;
   }

   @Override 
   public String toString(){
      return "[Soldier " + idx + ", priority=" + priority + "]";
   }

}