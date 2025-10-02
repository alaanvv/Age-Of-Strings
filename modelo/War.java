package modelo;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Collections;
/* Esta é a classe de transação do projeto. O objeto de composição será batalhas acontecendo.*/

public class War extends Entidade{
   ArrayList<Battle> current_battles = new ArrayList<Battle>();
   
   
}

class Battle{
   Army attacker, defender;
   
   Army.General genattacker;
   Army.General gendefender;
   
   ArrayList<Army.Soldier> attacker_soldiers = new ArrayList<>();
   int attacker_soldiers_alive;
   ArrayList<Army.Soldier> defender_soldiers = new ArrayList<>();
   int defender_soldiers_alive;

   PriorityQueue<TurnOrder> next = new PriorityQueue<>(Collections.reverseOrder());
   
   Battle(Army attacker, Army defender){
      this.attacker = attacker;
      this.defender = defender;

      genattacker = attacker.general;
      gendefender = defender.general;



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
      
      next.clear();
      attacker_soldiers_alive = 0;
      defender_soldiers_alive = 0;

      for(int i = 0; i < attacker.soldiers_amt; i++){
         if(attacker_soldiers.get(i).hp <= 0) continue;
         next.add(new TurnOrder(i, (int) (Math.random()*attacker_soldiers.get(i).dexterity), true));
         attacker_soldiers_alive++;
      }

      for(int i = 0; i < defender.soldiers_amt; i++){
         if(defender_soldiers.get(i).hp <= 0) continue;
         next.add(new TurnOrder(i, (int) Math.random()*defender_soldiers.get(i).get_dexterity(), false));
         defender_soldiers_alive++;
      }

      if(defender_soldiers_alive <= 0) return 1;
      else if(attacker_soldiers_alive <= 0) return -1;

      while(next.size() > 0){
         int current_idx = next.element().idx;
         boolean is_attacker = next.element().is_attacker;
         next.remove();
         
         Army.Soldier current_soldier;
         ArrayList<Army.Soldier> target_army = is_attacker? defender_soldiers : attacker_soldiers;
         current_soldier = is_attacker? attacker_soldiers.get(current_idx) : defender_soldiers.get(current_idx);
         Army.General gen_enemy = is_attacker? gendefender : genattacker;

         //Decide se o soldado irá fugir

         // Escolhe uma direção aleatória e busca o primeiro soldado com vida nessa direção. Dá a volta no array.
         int direction = Math.random() * 10 < 5? -1 : 1;
         int aim = (current_idx + direction + target_army.size()) % target_army.size();
         for(int i = 0; i < target_army.size(); i++){
            if(target_army.get(aim).hp > 0) break;
            else aim = (aim + direction + target_army.size()) % target_army.size();
         }

         if(target_army.get(aim).hp <= 0) continue;

         if(current_soldier.hit(target_army.get(aim))){
            if(is_attacker){
               defender_soldiers_alive--;
               if(defender_soldiers_alive <= 0) return 1;
            }else{
               attacker_soldiers_alive--;
               if(attacker_soldiers_alive <= 0) return -1;
            }
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