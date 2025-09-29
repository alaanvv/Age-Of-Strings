package modelo;
import modelo.Army;
import java.util.ArrayList;
/* Esta é a classe de transação do projeto. O objeto de composição será batalhas acontecendo.*/

public class War extends Entidade{
   ArrayList<Battle> current_battles = new ArrayList<Battle>();
   

}

class Battle{
   Army attacker, defender;
}
