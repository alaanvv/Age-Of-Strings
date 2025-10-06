package visao;

import java.util.Scanner;
import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import modelo.Entidade;
import modelo.Farm;
import modelo.Lumber;
import modelo.Mine;
import persistencia.BancoDeDados;

public class Terminal {
  private static StringBuilder logs = new StringBuilder();
  public static Scanner sc = new Scanner(System.in);

  private static void log(String text) {
    logs.append(text).append("\n");
  }

  private static void print(String cmd, String desc, Object... args) {
    System.out.printf("%-20s # ", cmd);
    System.out.printf(desc + "\n", args);
  }

  private static String[] read() {
    if (logs.length() > 0) {
      System.out.println();
      System.out.print(logs);
      logs.setLength(0);
    }

    System.out.print("\n:");
    String line = sc.nextLine();

    System.out.print("\033[H\033[2J");
    System.out.flush();
    return line.split(" ");
  }

  private static int parseInt(String n) {
    return Integer.parseInt(n);
  }

  public static void mainMenu(BancoDeDados db) {
    print("new", "Criar imperio");
    if (db.getEmpire().getSize() > 0) print("control <id>", "Controlar imperio *id* (0-%d)", db.getEmpire().getSize() - 1);
    if (db.getEmpire().getSize() > 0) print("view <id>", "Ver imperio *id* (0-%d)", db.getEmpire().getSize() - 1);
    if (db.getEmpire().getSize() > 0) print("viewall", "Ver todos imperios");
    if (db.getEmpire().getSize() > 0) print("destroy <id>", "Destroi o imperio *id* (0-%d)", db.getEmpire().getSize() - 1);
    if (db.getEmpire().getSize() > 0) print("run", "Roda o turno");
    print("exit", "Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = new Empire(db);
        db.getEmpire().insert(empire);
        log(String.format("Imperio #%d criado", empire.getId()));
        break;
      case "view":
        empire = (Empire) db.getEmpire().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else log(empire.toString());
        break;
      case "viewall":
        for (Entidade e : db.getEmpire().getEntidades())
          log(e.toString());
        break;
      case "destroy":
        empire = (Empire) db.getEmpire().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else {
          empire.destroy();
          log(empire.toString());
        }
        break;
      case "control":
        empire = (Empire) db.getEmpire().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio nao encontrado.");
        else empireMenu(empire, db);
        break;
      case "run":
        for (Entidade e : db.getEmpire().getEntidades())
          log(((Empire) e).runTurn());
        log("Turno rodado.");
        break;
      case "exit":
        sc.close();
        return;
    }

    mainMenu(db);
  }

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

  public static void farmMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Fazenda - Produz comida");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 3]");
    System.out.println("");
    if (empire.getFarms().size() > 0) print("view", "Ver fazendas");
    if (db.getFarm().getSize() > 0) print("viewany <id>", "Ver fazenda de qualquer imperio com *id* (0-%d)", db.getFarm().getSize() - 1);
    if (db.getFarm().getSize() > 0) print("viewall", "Ver todas fazendas de todos imperios");
    print("build", "Construir nova fazenda");
    if (db.getFarm().getSize() > 0) print("destroy <id>", "Destroi a fazenda *id* (0-%d)", db.getFarm().getSize() - 1);
    if (empire.getFarms().size() > 0) print("send <amount> <id>", "Envia *amount* trabalhadores pra fazenda *id*");
    if (empire.getFarms().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores da fazenda *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Farm farm : empire.getFarms())
          log(farm.toString());
        break;
      case "viewany":
        Farm farm = (Farm) db.getFarm().findById(parseInt(cmd[1]));
        if (farm == null) log("Fazenda inexistente.");
        else log(farm.toString());
        break;
      case "viewall":
        for (Entidade f : db.getFarm().getEntidades())
          log(f.toString());
        break;
      case "build":
        log(empire.buildFarm() ? "Fazenda construida." : "Recursos insuficientes.");
        break;
      case "destroy":
        farm = (Farm) db.getFarm().findById(parseInt(cmd[1]));
        if (farm == null) log("Fazenda inexistente.");
        else {
          farm.destroy();
          log(farm.toString());
        }
        break;
      case "send":
        farm = (Farm) db.getFarm().findById(parseInt(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.getEmpireId() != empire.getId())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.sendWorkersToFarm(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        farm = (Farm) db.getFarm().findById(parseInt(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.getEmpireId() != empire.getId())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.takeWorkersFromFarm(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "back":
        return;
    }

    farmMenu(empire, db);
  }

  public static void mineMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Minas - Produzem ferro e ouro");
    System.out.println("Preco de construcao [Madeira: 15; Ouro: 5]");
    System.out.println("");
    if (empire.getMines().size() > 0) print("view", "Ver minas");
    if (db.getMine().getSize() > 0) print("viewany <id>", "Ver mina de qualquer imperio com *id* (0-%d)", db.getMine().getSize() - 1);
    if (db.getMine().getSize() > 0) print("viewall", "Ver todas minas de todos imperios");
    print("build", "Construir nova mina");
    if (db.getMine().getSize() > 0) print("destroy <id>", "Destroi a mina *id* (0-%d)", db.getMine().getSize() - 1);
    if (empire.getMines().size() > 0) print("send <amount> <id>", "Envia *amount* trabalhadores pra mina *id*");
    if (empire.getMines().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores da mina *id*");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Mine m : empire.getMines())
          log(m.toString());
        break;
      case "viewany":
        Mine mine = (Mine) db.getMine().findById(parseInt(cmd[1]));
        if (mine == null) log("Mina inexistente.");
        else log(mine.toString());
        break;
      case "viewall":
        for (Entidade m : db.getMine().getEntidades())
          log(m.toString());
        break;
      case "build":
        log(empire.buildMine() ? "Mina construida." : "Recursos insuficientes.");
        break;
      case "destroy":
        mine = (Mine) db.getMine().findById(parseInt(cmd[1]));
        if (mine == null) log("Mina inexistente.");
        else {
          mine.destroy();
          log(mine.toString());
        }
        break;
      case "send":
        mine = (Mine) db.getMine().findById(parseInt(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.getEmpireId() != empire.getId())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.sendWorkersToMine(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        mine = (Mine) db.getMine().findById(parseInt(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.getEmpireId() != empire.getId())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.takeWorkersFromMine(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "back":
        return;
    }

    mineMenu(empire, db);
  }

  public static void lumberMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Campos de lenhador - Produzem madeira");
    System.out.println("");
    print("view", "Ver campos");
    if (db.getLumberCamp().getSize() > 0) print("viewany <id>", "Ver campo de lenhador de qualquer imperio com *id* (0-%d)", db.getLumberCamp().getSize() - 1);
    if (db.getLumberCamp().getSize() > 0) print("viewall", "Ver todos campos de lenhador de todos imperios");
    if (db.getLumberCamp().getSize() > 0) print("destroy <id>", "Destroi o campo de lenhador *id* (0-%d)", db.getLumberCamp().getSize() - 1);
    print("send <amount>", "Envia *amount* trabalhadores pro campo");
    print("take <amount>", "Tira *amount* trabalhadores do campo");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        log(empire.getLumber().toString());
        break;
      case "viewany":
        Lumber lumber = (Lumber) db.getLumberCamp().findById(parseInt(cmd[1]));
        if (lumber == null) log("Campo de lenhador inexistente.");
        else log(lumber.toString());
        break;
      case "viewall":
        for (Entidade l : db.getLumberCamp().getEntidades())
          log(l.toString());
        break;
      case "destroy":
        lumber = (Lumber) db.getLumberCamp().findById(parseInt(cmd[1]));
        if (lumber == null) log("Campo de lenhador inexistente.");
        else {
          lumber.destroy();
          log(lumber.toString());
        }
        break;
      case "send":
        log(String.format("%d trabalhadores enviados.", empire.sendWorkersToLumber(parseInt(cmd[1]))));
        break;
      case "take":
        log(String.format("%d trabalhadores retirados.", empire.takeWorkersFromLumber(parseInt(cmd[1]))));
        break;
      case "back":
        return;
    }

    lumberMenu(empire, db);
  }

  public static void armyMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Exercitos - Usados pra atacar e se defender de outros imperios");
    System.out.println("Preco de construcao [Ferro: 50; Ouro: 20]");
    System.out.println("Preco de melhoria   [Ferro: 25; Ouro:  5]");
    System.out.println("");
    print("view", "Ver exercitos");
    if (db.getArmy().getSize() > 0) print("viewany <id>", "Ver exercito de qualquer imperio com *id* (0-%d)", db.getArmy().getSize() - 1);
    if (db.getArmy().getSize() > 0) print("viewall", "Ver todos exercitos de todos imperios");
    print("new", "Criar novo exercito");
    if (db.getArmy().getSize() > 0) print("destroy <id>", "Destroi o exercito *id* (0-%d)", db.getArmy().getSize() - 1);
    if (empire.getArmies().size() > 0) print("send <amount> <id>", "Envia *amount* tropas pro exercito *id*");
    if (empire.getArmies().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores do exercito *id*");
    if (empire.getArmies().size() > 0) print("upgrade <amount> <id>", "Melhora a armadura do exercito *id* em *amount* niveis");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Army a : empire.getArmies())
          log(a.toString());
        break;
      case "viewany":
        Army army = (Army) db.getArmy().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else log(army.toString());
        break;
      case "viewall":
        for (Entidade a : db.getArmy().getEntidades())
          log(a.toString());
        break;
      case "new":
        log(empire.createArmy() ? "Exercito criado." : "Recursos insuficientes.");
        break;
      case "destroy":
        army = (Army) db.getArmy().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else {
          army.destroy();
          log(army.toString());
        }
        break;
      case "send":
        army = (Army) db.getArmy().findById(parseInt(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpireId() != empire.getId())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores se tornaram soldados.", empire.sendWorkersToArmy(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        army = (Army) db.getArmy().findById(parseInt(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpireId() != empire.getId())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d tropas retiradas.", empire.takeWorkersFromArmy(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "upgrade":
        army = (Army) db.getArmy().findById(parseInt(cmd[2]));
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

  public static void warMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("GUERRAS");
    System.out.println("");
    if (db.getBattle().getSize() > 0) print("view", "Ver batalhas em andamento");
    if (db.getBattle().getSize() > 0) print("viewany <id>", "Ver batalha com *id* (0-%d)", db.getBattle().getSize() - 1);
    print("new <atk_id> <dfn_id>", "Iniciar nova batalha usando a tropa *atk_id* pra atacar *dfn_id*");
    if (db.getBattle().getSize() > 0) print("destroy <id>", "Destroi a batalha *id* (0-%d)", db.getBattle().getSize() - 1);
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (int i = db.getBattle().getSize() - 1; i >= 0; i--) {
          Battle batalhas = (Battle) db.getBattle().findById(i);
          String attackerName = "Army #" + batalhas.getAttacker().getId();
          String defenderName = "Army #" + batalhas.getDefender().getId();
          log("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          log("Soldados Atacantes vivos: " + batalhas.getAttackerSoldiersAlive());
          log("Soldados Defensores vivos: " + batalhas.getDefenderSoldiersAlive());
        }
        break;
      case "viewany":
        Battle battle = (Battle) db.getBattle().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else log(battle.toString());
        break;
      case "new":
        Army attackerArmy = (Army) db.getArmy().findById(parseInt(cmd[1]));
        if (attackerArmy == null || attackerArmy.getEmpireId() != empire.getId()) {
          log("Tropa atacante invalida ou nao pertence ao seu imperio.");
        } else {
          Army defenderArmy = (Army) db.getArmy().findById(parseInt(cmd[2]));
          if (defenderArmy == null || defenderArmy.getEmpireId() == empire.getId()) {
            log("Tropa defensora invalida ou pertence ao seu imperio.");
          } else {
            Battle newBattle = new Battle(attackerArmy, defenderArmy, db);
            db.getBattle().insert(newBattle);
            log("Batalha iniciada!");
          }
        }
        break;
      case "destroy":
        battle = (Battle) db.getBattle().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else {
          battle.destroy();
          log(battle.toString());
        }
        break;
      case "back":
        return;
    }

    warMenu(empire, db);
  }
}