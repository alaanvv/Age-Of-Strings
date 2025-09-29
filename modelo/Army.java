package modelo;

public class Army extends Entidade{
   
   int armory_level = 0;
   int food_level = 100;
   int hiring_level = 1;
   
   int soldiers;

   public Army(){
      super();
   }
   
   /** @return Amount of food spent */
   public int supply_food(int food_supply){
      if(food_supply < 0){
         throw new IllegalArgumentException("It's not possible to remove food of an army (they will be angry).");
      }
      int _food_level = food_level;
      food_level = Math.min(1000, food_level + food_supply);
      return food_level - _food_level;
   }

   /**
    * In order to increase a level of of armory, it is necessary 15 irons and 5 gold. This method will upgrade as much as it can.
    * @param iron
    * @param gold
    * @return A pair<int, int> where .first = remainder iron and .second = remainder gold.
     */
   public Auxiliar.Pair.ii upgrade_armory(int iron, int gold){
      final int IRON_COST = 15, GOLD_COST = 5;
      
      int iron_packs = iron/IRON_COST;
      int gold_packs = gold/GOLD_COST;

      int points_added = Math.min(iron_packs, gold_packs);
      armory_level += points_added;
      
      return new Auxiliar.Pair.ii((iron_packs - points_added)*IRON_COST, (gold_packs - points_added)*GOLD_COST);
   }

   /** @return money not invested  */
   public int change_hiring_funds(int cyclical_gold){
      hiring_level = (int)Auxiliar.LogCalculator.logb(cyclical_gold, 1.055D);
      return cyclical_gold - (int)Auxiliar.LogCalculator.logb(cyclical_gold, 1.055D);
   }

   public void time_update_army(Empire empire){

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
         this.hp = 20 + (int)(Math.random() * (100 + (food_level/2)));
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
   
   
   
   /** The class soldier is only instantiated inside Battle class. His values are then generated randomly accordingly with his army status. */
   class Soldier{
      int hp;
      int dexterity;
      int damage;
      double armor_factor;
      int morale;
      
      public Soldier(){
         hp = (int)(Math.random() * (20 + (food_level/1.25)));
         dexterity = (int) Math.random()*(hiring_level + 20);
         damage = (int) Math.random() * ((hiring_level + armory_level)/10 + 10);
         armor_factor = Math.random() * (Math.min(0.9, Auxiliar.LogCalculator.logb(hiring_level, 200)));
      }
   }
   
   General general;
}

