import Interface.Interface;
import persistencia.BancoDeDados;
import visao.Terminal;

public class Programa {
  public static void main(String[] args) {
    BancoDeDados db = new BancoDeDados();
    //Terminal.mainMenu(db);
    Interface game = new Interface(db);
  }
}
