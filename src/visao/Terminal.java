package visao;

import java.util.Scanner;
import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import modelo.Entidade;
import modelo.Farm;
import modelo.Mine;
import persistencia.BancoDeDados;

/**
 * Handles all command-line interface (CLI) interactions for the game.
 * <p>
 * This class is responsible for displaying menus, reading user input,
 * and calling the appropriate model methods based on the commands entered.
 * It operates as a series of recursive menu loops that manage the application's state.
 */
public class Terminal {
  /**
   * A string buffer to accumulate feedback messages for the user.
   * Messages are logged here and then printed to the console all at once
   * during the next input cycle in the {@link #read()} method.
   */
  private static StringBuilder logs = new StringBuilder();
  public static Scanner sc = new Scanner(System.in);

  /**
   * Appends a message to the log buffer to be displayed later.
   * An empty string will be ignored.
   *
   * @param text The message to add to the log.
   */
  private static void log(String text) {
    if (text.equals("")) return;
    logs.append(text).append("\n");
  }

  /**
   * Prints a formatted command entry for a menu.
   * Ensures consistent alignment for better readability.
   *
   * @param cmd The command string (e.g., "new <name>").
   * @param desc A description of the command, which can include format specifiers.
   * @param args Optional arguments to be formatted into the description string.
   */
  private static void print(String cmd, String desc, Object... args) {
    System.out.printf("%-20s # ", cmd);
    System.out.printf(desc + "\n", args);
  }

  /**
   * The core input and screen refresh method for the terminal UI.
   * <p>
   * This method performs three main tasks:
   * 1. Prints all accumulated messages from the {@code logs} buffer.
   * 2. Prompts the user for input and reads the next line.
   * 3. Clears the console screen using ANSI escape codes for a clean refresh.
   *
   * @return An array of strings, split from the user's input line.
   */
  private static String[] read() {
    if (logs.length() > 0) {
      System.out.println();
      System.out.print(logs);
      logs.setLength(0); // Clear the buffer after printing
    }

    System.out.print("\n:");
    String line = sc.nextLine();

    // ANSI escape codes to clear the screen.
    // \033[H moves the cursor to the top-left corner.
    // \033[2J clears the entire screen.
    System.out.print("\033[H\033[2J");
    System.out.flush();
    return line.split(" ");
  }

  /**
   * A simple utility wrapper for Integer.parseInt to keep the main code cleaner.
   *
   * @param n The string to be parsed into an integer.
   * @return The integer value of the string.
   */
  private static int parseInt(String n) {
    return Integer.parseInt(n);
  }

  /**
   * Displays the main menu and handles top-level commands like creating,
   * controlling, or destroying empires. This method acts as the entry point
   * for the game's UI loop and calls itself recursively to keep the menu active.
   *
   * @param db The main database object containing all game data.
   */
  public static void mainMenu(BancoDeDados db) {
    if (db.hasEmpire()) {
      for (Entidade e : db.getEmpires().getEntidades().values())
        System.out.println(String.format("Imperio %s #%d", ((Empire) e).getName(), e.getId()));
      System.out.println("");
    }

    print("new <name>", "Criar imperio");
    if (db.hasEmpire()) print("control <id>", "Controlar imperio *id*");
    if (db.hasEmpire()) print("destroy <id>", "Destroi o imperio *id*");
    if (db.hasEmpire()) print("run", "Roda o turno");
    print("exit", "Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = new Empire(cmd[1], db.nextEmpire());
        db.getEmpires().insert(empire);
        break;
      case "control":
        empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio nao encontrado.");
        else empireMenu(empire, db);
        break;
      case "destroy":
        empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else db.destroyEmpire(empire);
        break;
      case "run":
        for (Entidade e : db.getEmpires().getEntidades().values())
          log(((Empire) e).runTurn());
        log("Turno rodado.");
        break;
      case "exit":
        sc.close();
        return; // Exits the recursive loop
    }

    mainMenu(db); // Recursive call to show the menu again
  }

  /**
   * Displays the management menu for a specific empire. This menu is a hub that
   * navigates to sub-menus for managing buildings, armies, and warfare.
   * It operates on a recursive loop until the user chooses to go back.
   *
   * @param empire The empire being controlled.
   * @param db The main database object.
   */
  public static void empireMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    print("house", "Ver menu de casas");
    print("farm", "Ver menu de fazendas");
    print("mine", "Ver menu de minas");
    print("lumber", "Ver menu de campos de lenhador");
    print("army", "Ver menu de exercito");
    print("war", "Ver menu de guerra");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "house":
        houseMenu(empire, db);
        break;
      case "farm":
        farmMenu(empire, db);
        break;
      case "mine":
        mineMenu(empire, db);
        break;
      case "lumber":
        lumberMenu(empire, db);
        break;
      case "army":
        armyMenu(empire, db);
        break;
      case "war":
        warMenu(empire, db);
        break;
      case "back":
        return;
    }

    empireMenu(empire, db);
  }

  /**
   * Displays the menu for building houses. This menu is a simple interface
   * for a single action: constructing a new house. It recursively calls itself
   * to remain on the screen until the user goes back.
   *
   * @param empire The empire performing the action.
   * @param db The main database object.
   */
  public static void houseMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Casa - Aumenta a populacao");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 5]");
    System.out.println("");
    print("build", "Construir");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "build":
        log(empire.buildHouse() ? "Casa construida." : "Recursos insuficientes.");
        break;
      case "back":
        return;
    }

    houseMenu(empire, db);
  }

  /**
   * Displays the menu for managing farms. Allows building new farms and
   * assigning/removing workers from existing ones. Calls itself recursively
   * to stay on this menu.
   *
   * @param empire The empire managing the farms.
   * @param db The main database object.
   */
  public static void farmMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    for (Farm farm : empire.getFarms().values())
      System.out.println(farm.toString());
    if (empire.hasFarm()) System.out.println("");
    System.out.println("Fazenda - Produz comida");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 3]");
    System.out.println("");
    print("build", "Construir nova fazenda");
    if (empire.hasFarm()) print("send <amount> <id>", "Envia *amount* trabalhadores pra fazenda *id*");
    if (empire.hasFarm()) print("take <amount> <id>", "Tira *amount* trabalhadores da fazenda *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "build":
        log(db.createFarm(empire.getId()) != 0 ? "Fazenda construida." : "Recursos insuficientes.");
        break;
      case "send":
        Farm farm = (Farm) db.getFarms().findById(parseInt(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.getEmpireId() != empire.getId())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.sendWorkers(parseInt(cmd[1]), farm)));
        break;
      case "take":
        farm = (Farm) db.getFarms().findById(parseInt(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.getEmpireId() != empire.getId())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.takeWorkers(parseInt(cmd[1]), farm)));
        break;
      case "back":
        return;
    }

    farmMenu(empire, db);
  }

  /**
   * Displays the menu for managing mines. Allows building new mines and
   * assigning/removing workers from existing ones. Calls itself recursively
   * to stay on this menu.
   *
   * @param empire The empire managing the mines.
   * @param db The main database object.
   */
  public static void mineMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    for (Mine mine : empire.getMines().values())
      System.out.println(mine.toString());
    if (empire.hasMine()) System.out.println("");
    System.out.println("Minas - Produzem ferro e ouro");
    System.out.println("Preco de construcao [Madeira: 15; Ouro: 5]");
    System.out.println("");
    print("build", "Construir nova mina");
    if (empire.hasMine()) print("send <amount> <id>", "Envia *amount* trabalhadores pra mina *id*");
    if (empire.hasMine()) print("take <amount> <id>", "Tira *amount* trabalhadores da mina *id*");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "build":
        log(db.createMine(empire.getId()) != 0 ? "Mina construida." : "Recursos insuficientes.");
        break;
      case "send":
        Mine mine = (Mine) db.getMines().findById(parseInt(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.getEmpireId() != empire.getId())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.sendWorkers(parseInt(cmd[1]), mine)));
        break;
      case "take":
        mine = (Mine) db.getMines().findById(parseInt(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.getEmpireId() != empire.getId())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.takeWorkers(parseInt(cmd[1]), mine)));
        break;
      case "back":
        return;
    }

    mineMenu(empire, db);
  }

  /**
   * Displays the menu for managing the empire's single lumber camp. Allows
   * assigning and removing workers. Calls itself recursively.
   *
   * @param empire The empire managing the lumber camp.
   * @param db The main database object.
   */
  public static void lumberMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println(empire.getLumber());
    System.out.println("");
    System.out.println("Campos de lenhador - Produzem madeira");
    System.out.println("");
    print("send <amount>", "Envia *amount* trabalhadores pro campo");
    print("take <amount>", "Tira *amount* trabalhadores do campo");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "send":
        log(String.format("%d trabalhadores enviados.", empire.sendWorkers(parseInt(cmd[1]), empire.getLumber())));
        break;
      case "take":
        log(String.format("%d trabalhadores retirados.", empire.takeWorkers(parseInt(cmd[1]), empire.getLumber())));
        break;
      case "back":
        return;
    }

    lumberMenu(empire, db);
  }

  /**
   * Displays the comprehensive menu for managing armies. This includes creating,
   * viewing, destroying, reinforcing, and upgrading armies. Calls itself recursively.
   *
   * @param empire The empire managing its armies.
   * @param db The main database object, used for viewing/interacting with any army.
   */
  public static void armyMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Exercitos - Usados pra atacar e se defender de outros imperios");
    System.out.println("Preco de construcao [Ferro: 50; Ouro: 20]");
    System.out.println("Preco de melhoria   [Ferro: 25; Ouro:  5]");
    System.out.println("");
    print("view", "Ver exercitos");
    if (db.getArmies().getSize() > 0) print("viewany <id>", "Ver exercito de qualquer imperio com *id* (0-%d)", db.getArmies().getSize() - 1);
    if (db.getArmies().getSize() > 0) print("viewall", "Ver todos exercitos de todos imperios");
    print("new", "Criar novo exercito");
    if (db.getArmies().getSize() > 0) print("destroy <id>", "Destroi o exercito *id* (0-%d)", db.getArmies().getSize() - 1);
    if (empire.getArmies().size() > 0) print("send <amount> <id>", "Envia *amount* tropas pro exercito *id*");
    if (empire.getArmies().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores do exercito *id*");
    if (empire.getArmies().size() > 0) print("upgrade <amount> <id>", "Melhora a armadura do exercito *id* em *amount* niveis");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Army a : empire.getArmies().values())
          log(a.toString());
        break;
      case "viewany":
        Army army = (Army) db.getArmies().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else log(army.toString());
        break;
      case "viewall":
        for (Entidade a : db.getArmies().getEntidades().values())
          log(a.toString());
        break;
      case "new":
        log(db.createArmy(empire.getId()) != 0 ? "Exercito criado." : "Recursos insuficientes.");
        break;
      case "destroy":
        army = (Army) db.getArmies().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else {
          db.destroyEntity(army);
          log(army.toString());
        }
        break;
      case "send":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpireId() != empire.getId())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores se tornaram soldados.", empire.sendWorkers(parseInt(cmd[1]), army)));
        break;
      case "take":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpireId() != empire.getId())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d tropas retiradas.", empire.takeWorkers(parseInt(cmd[1]), army)));
        break;
      case "upgrade":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpireId() != empire.getId())
          log("Esse exercito nao pertence a esse imperio.");
        else {
          int pontos = army.upgradeArmory(parseInt(cmd[1]), empire);
          log(pontos > 0 ? String.format("Armadura melhorada em %d ponto(s).", pontos) :
            "Recursos insuficientes para esta melhoria.");
        }
        break;
      case "back":
        return;
    }

    armyMenu(empire, db);
  }

  /**
   * Displays the menu for initiating and viewing battles. Calls itself recursively.
   *
   * @param empire The empire initiating or viewing wars.
   * @param db The main database object.
   */
  public static void warMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("GUERRAS");
    System.out.println("");
    if (db.getBattles().getSize() > 0) print("view", "Ver batalhas em andamento");
    if (db.getBattles().getSize() > 0) print("viewany <id>", "Ver batalha com *id* (0-%d)", db.getBattles().getSize() - 1);
    print("new <atk_id> <dfn_id>", "Iniciar nova batalha usando a tropa *atk_id* pra atacar *dfn_id*");
    if (db.getBattles().getSize() > 0) print("destroy <id>", "Destroi a batalha *id* (0-%d)", db.getBattles().getSize() - 1);
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (int i = db.getBattles().getSize() - 1; i >= 0; i--) {
          Battle batalhas = (Battle) db.getBattles().findById(i);
          String attackerName = "Army #" + batalhas.getAttacker().getId();
          String defenderName = "Army #" + batalhas.getDefender().getId();
          log("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          log("Soldados Atacantes vivos: " + batalhas.getAttackerSoldiersAlive());
          log("Soldados Defensores vivos: " + batalhas.getDefenderSoldiersAlive());
        }
        break;
      case "viewany":
        Battle battle = (Battle) db.getBattles().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else log(battle.toString());
        break;
      case "new":
        Army attackerArmy = (Army) db.getArmies().findById(parseInt(cmd[1]));
        if (attackerArmy == null || attackerArmy.getEmpireId() != empire.getId()) {
          log("Tropa atacante invalida ou nao pertence ao seu imperio.");
        } else {
          Army defenderArmy = (Army) db.getArmies().findById(parseInt(cmd[2]));
          if (defenderArmy == null || defenderArmy.getEmpireId() == empire.getId()) {
            log("Tropa defensora invalida ou pertence ao seu imperio.");
          } else {
            Battle newBattle = new Battle(attackerArmy, defenderArmy, db.nextBattle());
            db.getBattles().insert(newBattle);
            log("Batalha iniciada!");
          }
        }
        break;
      case "destroy":
        battle = (Battle) db.getBattles().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else {
          db.destroyEntity(battle);
          log(battle.toString());
        }
        break;
      case "back":
        return;
    }

    warMenu(empire, db);
  }
}