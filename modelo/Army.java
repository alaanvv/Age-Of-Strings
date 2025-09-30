package modelo;

public class Army extends Entidade{
   
   int armory_level = 0;
   int food_level = 0;
   int hiring_level = 1;
   int hiring_cost = 0;
   
   int soldiers_amt = 0;

   public Army(){
      super();
   }
   
   /** @return Amount of food spent */
   public int supply_food(int food_supply){
      if(food_supply < 0){
         throw new IllegalArgumentException("It's not possible to remove food of an army (they will be angry).");
      }
      int _food_level = food_level;
      food_level = Math.min(soldiers_amt, food_level + food_supply);
      return food_level - _food_level;
   }

   /**
    * In order to increase a level of of armory, it is necessary IRON_COST irons and GOLD_COST gold. This method will upgrade as much as it can.
    * @param intended_points Amount of points that is trying to upgrade.
    * @return Amount of points effectively added.
     */
   public final int IRON_COST_ARMORY = 25, GOLD_COST_ARMORY = 5;
   public Auxiliar.Pair.ii upgrade_armory(int intended_points, Empire empire){
      int iron = Math.min(empire.iron, intended_points*IRON_COST_ARMORY);
      int gold = Math.min(empire.gold, intended_points*GOLD_COST_ARMORY);
      int iron_packs = iron/IRON_COST;
      int gold_packs = gold/GOLD_COST;

      int points_added = Math.min(iron_packs, gold_packs);
      armory_level += points_added;
      
      empire.iron -= (IRON_COST_ARMORY*points_added);
      empire.gold -= (GOLD_COST_ARMORY*points_added);

      return points_added;
   }

   /** @return Gold difference between previous hiring cost and new hiring cost  (if this methos is called outside time_update_army,
    * the diference must be added to the empire's gold.
    */
   public int change_hiring_funds(int cyclical_gold){
      int prev_hiring_cost = hiring_cost;

      hiring_level = (int)Auxiliar.LogCalculator.logb(cyclical_gold, 1.055D);
      hiring_cost = Math.pow(1.055D, hiring_level);
      return hiring_cost - prev_hiring_cost;
   }

   public boolean allocate_work(Empire empire, int amt){
      if(amt > empire.get_population()){
         return false;
      }

      soldiers_amt += amt;
      empire.set_population(empire.get_population() - amt);
      empire.set_workers(empire.get_workers() + amt);
   }

   public void time_update_army(Empire empire){
      
      // Manages army payment
      if(empire.gold < hiring_cost){
         change_hiring_funds(empire.gold);
         empire.gold -= hiring_cost;
      }else {
         empire.gold -= hiring_cost;
      }

      //Manages army food supply
      food_level = Math.max(food_level - soldiers_amt, 0);
      if(food_level < 0){
         soldiers_amt += (food_level/2);
         soldiers_amt = Math.max(soldiers_amt, 0);
      }
      if(empire.food < soldiers){
         empire.food -= supply_food(empire.food);
      }else{
         empire.food -= supply_food(soldiers_amt)
      }

      
   }



   
   
   // --- INNER CLASSES
   
   
   
   /**
    * The general is the head of an army and can certainly decide the fate of a battle. 
    * While the general is alive, his charisma maintains soldiers' morale high, avoiding desertion durring battle.
    * <p>
    * An army shall have at most 1 general, and never engage in battle when having none.
    */
   class General{
      int hp;
      int charisma;
      double armor_factor;
      int damage;
      
      public General(){
         this.hp = 20 + (int)(Math.random() * (100 + ((food_level*food_level/soldiers_amt)/2)));
         this.charisma = (int) Math.min(Math.random() * (5.1*(Math.log(hiring_level) * Math.log(hiring_level))), 100D);
   
         if(armor_factor < 0 || 1 < armor_factor){
            throw new IllegalArgumentException("Armor factor must be between 0 inclusive and 1 exclusive.");
         }
         
         this.armor_factor = (int) Math.min(0.99D, (Auxiliar.LogCalculator.logb(hiring_level, 140)+0.01D));

         this.damage = (int) Auxiliar.LogCalculator.logb((2*armory_level + hiring_level)/3, 2);
      }
   
      public boolean is_dead(){
         return hp <= 0;
      }
   
      
      /**
       * Update general's hit points given a certain damage.
       * @return True if hp reaches 0. 
       * @throws IllegalArgumentException if damage is negative (there are no healers in this game).
       */
      public boolean get_hit(int damage){
         if(damage < 0){
            throw new IllegalArgumentException("It's not possible to receive negative damage.");
         }
   
         int real_damage = (int) (damage - (damage*armor_factor));
         hp = Math.max(0, hp - real_damage);
   
         if(hp == 0) return true;
         else return false;
      }
   }
   General general;
   
   
   
   /** The class soldier is only instantiated inside Battle class. His values are then generated randomly accordingly with his army status. */
   class Soldier{
      int hp;
      int dexterity;
      int damage;
      double armor_factor;
      int morale;
      
      public Soldier(General general){
         hp = (int)(Math.random() * (20 + (food_level*food_level/soldiers_amt/1.25)));
         dexterity = (int) Math.random()*(hiring_level + 20);
         damage = (int) Math.random() * ((hiring_level + armory_level)/10 + 10);
         armor_factor = Math.random() * (Math.min(0.9, Auxiliar.LogCalculator.logb(hiring_level, 200)));
         morale = 1.5*(food_level/soldiers_amt)*(hiring_level) + general.charisma;
      }
   }
   
}

