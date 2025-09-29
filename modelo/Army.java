package modelo;

public class Army extends Entidade{
   General general;
   int soldiers;
   
   int armory_level;
   int food_level = 100;
   int hiring_level = 1;
   


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
      
      /**
       * @param hp
       * @param charisma
       * @param armor_factor
       *
       * @throws IllegalArgumentException if armor_factor is not between 0 and 1 inclusive.
       */
      public General(int hp, int charisma, int armor_factor){
         this.hp = 20 + (int)(Math.random() * (100 + (food_level/2)));
         this.charisma = (int) Math.min(Math.random() * (5.1*(Math.log(hiring_level) * Math.log(hiring_level))), 100D);
   
         if(armor_factor < 0 || 1 < armor_factor){
            throw new IllegalArgumentException("Armor factor must be between 0 inclusive and 1 exclusive.");
         }
         
         this.armor_factor = (int)Math.min(0.99D, (Auxiliar.LogCalculator.logb(hiring_level, 140)+0.01D));
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
         armor_factor = Math.min(0.9, Auxiliar.LogCalculator.logb(hiring_level, 200));
      }
   }
}

