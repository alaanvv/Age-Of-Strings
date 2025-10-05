package modelo;

public class Army extends Entidade{
   
   protected int empire_id = -1;
   protected int idx = -1;
   protected int armory_level = 1;
   protected int food_level = 10;
   protected int hiring_level = 1;
   protected int hiring_cost = 1;
   protected int soldiers_amt = 1;

   public Army(int empire_id, int idx){
      super();
      this.empire_id = empire_id;
      this.idx = idx;
   }
   
   /** @return Amount of food spent */
   public int supply_food(int food_supply){
      if(food_supply < 0){
         throw new IllegalArgumentException("It's not possible to remove food of an army (they will be angry).");
      }
      int _food_level = food_level;
      food_level = Math.min((int)(1.5*soldiers_amt), food_level + food_supply);
      return food_level - _food_level;
   }

   /**
    * In order to increase a level of of armory, it is necessary IRON_COST irons and GOLD_COST gold. This method will upgrade as much as it can.
    * @param intended_points Amount of points that is trying to upgrade.
    * @return Amount of points effectively added.
     */
   public final int IRON_COST_ARMORY = 25, GOLD_COST_ARMORY = 5;
   public int upgrade_armory(int intended_points, Empire empire){
      int iron = Math.min(empire.get_iron(), intended_points*IRON_COST_ARMORY);
      int gold = Math.min(empire.get_gold(), intended_points*GOLD_COST_ARMORY);
      int iron_packs = iron/IRON_COST_ARMORY;
      int gold_packs = gold/GOLD_COST_ARMORY;

      int points_added = Math.min(iron_packs, gold_packs);
      armory_level += points_added;
      
      empire.set_iron((empire.get_iron() - IRON_COST_ARMORY*points_added));
      empire.set_gold(empire.get_gold() - (GOLD_COST_ARMORY*points_added));

      return points_added;
   }

   /** @return Gold difference between previous hiring cost and new hiring cost  (if this methos is called outside time_update_army,
    * the diference must be added to the empire's gold.
    */
   public int change_hiring_funds(int cyclical_gold){
      int prev_hiring_cost = hiring_cost;

      hiring_level = (int)auxiliar.LogCalculator.logb(cyclical_gold, 1.055D);
      hiring_cost = (int) Math.pow(1.055D, hiring_level);
      return hiring_cost - prev_hiring_cost;
   }

   public boolean allocate_work(Empire empire, int amt){
      if(amt > empire.get_population()){
         return false;
      }

      soldiers_amt += amt;
      empire.set_population(empire.get_population() - amt);
      empire.set_workers(empire.get_workers() + amt);
      return true;
   }

   public void time_update_army(Empire empire){
      
      // Manages army payment
      if(empire.get_gold() < hiring_cost){
         change_hiring_funds(empire.get_gold());
         empire.set_gold(empire.get_gold() - hiring_cost);
      }else {
         empire.set_gold(empire.get_gold() - hiring_cost);
      }

      //Manages army food supply
      food_level = Math.max(food_level - soldiers_amt, 0);
      if(food_level < 0){
         soldiers_amt += food_level;
         soldiers_amt = Math.max(soldiers_amt, 0);
      }

      if(empire.get_food() < soldiers_amt){
         empire.set_food(empire.get_food() - supply_food(empire.get_food()));
      }else{
         empire.set_food(empire.get_food() - supply_food(soldiers_amt));
      }

      
   }

   // --- INNER CLASSES
   
   /**
    * The general is the head of an army and can certainly decide the fate of a battle. 
    * While the general is alive, his charisma maintains soldiers' morale high, avoiding desertion durring battle.
    * <p>
    * An army shall have at most 1 general, and never engage in battle when having none.
    */
    class General extends Soldier{
       protected int charisma;
       
       public General(){
         this.hp = 20 + (int) (Math.random() * (100 + ((food_level*food_level/soldiers_amt)/2)));
         
         this.dexterity = (int) (Math.random()*(hiring_level + 30));
         
         this.charisma = (int) Math.min(Math.random() * (5.1*(Math.log(hiring_level) * Math.log(hiring_level))), 100D);
         
         this.armor_factor = Math.min(0.99D, (auxiliar.LogCalculator.logb(hiring_level, 140)+0.01D));
         
         this.damage = (int) auxiliar.LogCalculator.logb((2*armory_level + hiring_level)/3, 2);

         this.morale = hiring_level*100;
      }
   
      public boolean is_dead(){
         return hp <= 0;
      }

      @Override
      public void flee(){
         super.flee();
         charisma = 0;
      }

      @Override
      public boolean get_hit(int damage){
         receive_damage(damage);
   
         if(hp == 0) {
            charisma = 0;
            return true;
         }
         else return false;
      }
   }
   General general;
   
   
   
   /** The class soldier is only instantiated inside Battle class. His values are then generated randomly accordingly with his army status. */
   class Soldier{
      protected int hp;
      protected int dexterity;
      protected int damage;
      protected double armor_factor;
      protected int morale;
      protected int idx;
      
      public Soldier(){};

      public Soldier(General general){
         hp = (int)(Math.random() * (10 + (food_level*food_level/soldiers_amt/1.25)));
         dexterity = (int) (Math.random()*(hiring_level + 20));
         damage = (int) (Math.random() * ((hiring_level + armory_level)/10 + 10));
         armor_factor = Math.min(0.9, Math.random() * auxiliar.LogCalculator.logb(hiring_level, 200));
         morale = (int) (1.5*(food_level/soldiers_amt)*(hiring_level)) + general.charisma;
      }

      int get_hp(){
         return hp;
      }
      int get_dexterity(){
         return dexterity;
      }
      int get_damage(){
         return damage;
      }
      public double getArmor_factor() {
         return armor_factor;
      }
      public int get_idx(){return idx;}
      public void set_idx(int ind){idx = ind;}

      
      public boolean get_hit(int damage){
         
         receive_damage((damage));
         
         if(hp == 0) return true;
         else return false;
      }
      
      protected void receive_damage(int damage){
         if(damage < 0){
            throw new IllegalArgumentException("It's not possible to receive negative damage.");
         }
   
         int real_damage = (int) Math.max((damage - (damage*armor_factor)), 1);
         hp = Math.max(0, hp - real_damage);
      }

      public boolean hit(Soldier target){
         return target.get_hit(damage);
      }
      
      public void flee(){
         this.hp = 0;
      }

   }
   
}

