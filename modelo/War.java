package modelo;
import modelo.Army;
import java.util.ArrayList;
/* Esta é a classe de transação do projeto. O objeto de composição será batalhas acontecendo.*/

public class War extends Entidade{
   ArrayList<Battle> current_battles = new ArrayList<Battle>();
   

}

class Battle{
   Army attacker, defender;
   General gen_attacker, gen_defender;
   ArrayList<Army.Soldier> attacker_soldiers = new ArrayList<>();
   ArrayList<Army.Soldier> defender_soldiers = new ArrayList<>();
   
   Battle(Army attacker, Army defender){
      this.attacker = attacker;
      this.defender = defender;
      
      for(int i = 0; i < attacker.soldiers)
   }
}
