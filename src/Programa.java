import Interface.Interface;
import persistencia.BancoDeDados;
import visao.Terminal;

public class Programa {
  public static void main(String[] args) {
    BancoDeDados database = new BancoDeDados();
    //Terminal.mainMenu(database);
    Interface game = new Interface(database);
  }
}
