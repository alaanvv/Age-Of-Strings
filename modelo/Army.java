package modelo;

public class Army extends Entidade{
   int soldiers;
   General general;

   
}

/**
 * The general is the head of an army and can certainly decide the fate of a battle. 
 * While the general is alive, his charisma maintains soldiers' morale high, avoiding desertion durring battle.
 * <p>
 * An army shall have at most 1 general, and never engage in battle when having none.
 */
class General{
   int hp;
   int charisma;
   int armor_factor;
   /**
    * @param hp
    * @param charisma
    * @param armor_factor
    *
    * @throws IllegalArgumentException if armor is not between 0 and 1 inclusive.
    */
   public General(int hp, int charisma, int armor_factor){
      this.hp = hp;
      this.charisma = charisma;

      if(armor_factor < 0 || 1 < armor_factor){
         throw new IllegalArgumentException("Armor factor must be between 0 and 1.");
      }
      
      this.armor_factor = armor_factor;
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

      int real_damage = damage - (damage*armor_factor);
      hp = Math.max(0, hp - real_damage);

      if(hp == 0) return true;
      else return false;
   }
}