package visao;

import java.util.Scanner;
import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import modelo.Entidade;
import modelo.Farm;
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

  private static int toint(String n) {
    return Integer.parseInt(n);
  }

  public static void mainMenu(BancoDeDados db) {
    print("new", "Criar Imperio");
    if (db.getEmpire().getSize() > 0) print("control <id>", "Controlar Imperio *id* (0-%d)", db.getEmpire().getSize() - 1);
    if (db.getEmpire().getSize() > 0) print("run", "Roda o turno");
    print("exit", "Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = new Empire(db);
        db.getEmpire().inserir(empire);
        log(String.format("Imperio #%d criado", empire.get_id()));
        break;
      case "control":
        empire = (Empire) db.getEmpire().buscarId(toint(cmd[1]));
        if (empire == null) log("Imperio nao encontrado.");
        else empireMenu(empire, db);
        break;
      case "run":
        for (Entidade e : db.getEmpire().getEntidades())
          log(((Empire) e).run_turn());
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
        log(empire.build_house() ? "Casa construida." : "Recursos insuficientes.");
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
    print("build", "Construir nova fazenda");
    if (empire.getFarms().size() > 0) print("send <amount> <id>", "Envia *amount* trabalhadores pra fazenda *id*");
    if (empire.getFarms().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores da fazenda *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Farm farm : empire.getFarms())
          log(farm.toString());
        break;
      case "build":
        log(empire.build_farm() ? "Fazenda construida." : "Recursos insuficientes.");
        break;
      case "send":
        Farm farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.get_empire_id() != empire.get_id())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.send_workers_to_farm(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null)
          log("Fazenda inexistente.");
        else if (farm.get_empire_id() != empire.get_id())
          log("Essa fazenda nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.take_workers_from_farm(toint(cmd[1]), toint(cmd[2]))));
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
    print("build", "Construir nova mina");
    if (empire.getMines().size() > 0) print("send <amount> <id>", "Envia *amount* trabalhadores pra mina *id*");
    if (empire.getMines().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores da mina *id*");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Mine mine : empire.getMines())
          log(mine.toString());
        break;
      case "build":
        log(empire.build_mine() ? "Mina construida." : "Recursos insuficientes.");
        break;
      case "send":
        Mine mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.get_empire_id() != empire.get_id())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores enviados.", empire.send_workers_to_mine(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null)
          log("Mina inexistente.");
        else if (mine.get_empire_id() != empire.get_id())
          log("Essa mina nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores retirados.", empire.take_workers_from_mine(toint(cmd[1]), toint(cmd[2]))));
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
    print("send <amount>", "Envia *amount* trabalhadores pro campo");
    print("take <amount>", "Tira *amount* trabalhadores do campo");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        log(empire.getLumber().toString());
        break;
      case "send":
        log(String.format("%d trabalhadores enviados.", empire.send_workers_to_lumber(toint(cmd[1]))));
        break;
      case "take":
        log(String.format("%d trabalhadores retirados.", empire.take_workers_from_lumber(toint(cmd[1]))));
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
    print("new", "Criar novo exercito");
    if (empire.getArmies().size() > 0) print("send <amount> <id>", "Envia *amount* tropas pro exercito *id*");
    if (empire.getArmies().size() > 0) print("take <amount> <id>", "Tira *amount* trabalhadores do exercito *id*");
    if (empire.getArmies().size() > 0) print("upgrade <amount> <id>", "Melhora a armadura do exercito *id* em *amount* niveis");
    print("back", "Voltar pro menu anterior");

    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Army army : empire.getArmies())
          log(army.toString());
        break;
      case "new":
        log(empire.create_army() ? "Exercito criado." : "Recursos insuficientes.");
        break;
      case "send":
        Army army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d trabalhadores se tornaram soldados.", empire.send_workers_to_army(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id())
          log("Esse exercito nao pertence a esse imperio.");
        else
          log(String.format("%d tropas retiradas.", empire.take_workers_from_army(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "upgrade":
        army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null)
          log("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id())
          log("Esse exercito nao pertence a esse imperio.");
        else {
          int pontos = army.upgrade_armory(toint(cmd[1]), empire);
          log(pontos > 0 ? String.format("Armadura melhorada em %d ponto(s).", pontos)
              : "Recursos insuficientes para esta melhoria.");
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
    print("new <atk_id> <dfn_id>", "Iniciar nova batalha usando a tropa *atk_id* pra atacar *dfn_id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (int i = db.getBattle().getSize() - 1; i >= 0; i--) {
          Battle batalhas = (Battle) db.getBattle().buscarId(i);
          String attackerName = "Army #" + batalhas.getAttacker().get_id();
          String defenderName = "Army #" + batalhas.getDefender().get_id();
          log("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          log("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
          log("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());
        }
        break;
      case "new":
        Army attackerArmy = (Army) db.getArmy().buscarId(toint(cmd[1]));
        if (attackerArmy == null || attackerArmy.getEmpire_id() != empire.get_id()) {
          log("Tropa atacante invalida ou nao pertence ao seu imperio.");
        } else {
          Army defenderArmy = (Army) db.getArmy().buscarId(toint(cmd[2]));
          if (defenderArmy == null || defenderArmy.getEmpire_id() == empire.get_id()) {
            log("Tropa defensora invalida ou pertence ao seu imperio.");
          } else {
            Battle new_battle = new Battle(attackerArmy, defenderArmy, db);
            db.getBattle().inserir(new_battle);
            log("Batalha iniciada!");
          }
        }
        break;
      case "back":
        return;
    }

    warMenu(empire, db);
  }
}

