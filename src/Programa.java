import persistencia.BancoDeDados;

/**
 * Application entry point. Builds the in-memory database and boots the GUI
 * (or CLI if uncommented) for the Age of Empires simulation.
 */
public class Programa {
  public static void main(String[] args) {
    BancoDeDados database = new BancoDeDados();
    visao.Terminal.mainMenu(database);
    //new Interface(database);
  }
}
