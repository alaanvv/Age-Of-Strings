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
  public static Scanner sc = new Scanner(System.in);

  private static String[] read() {
    System.out.print("\n:");
    String line = sc.nextLine();
    System.out.println();
    return line.split(" ");
  }

  private static int toint(String n) {
    return Integer.parseInt(n);
  }

  public static void mainMenu(BancoDeDados db) {
    System.out.println("create        # Criar Imperio");
    System.out.println("control <id>  # Controlar Imperio");
    System.out.println("run           # Roda o turno");
    System.out.println("exit          # Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "create":
        Empire empire = new Empire(db);
        db.getEmpire().inserir(empire);
        System.out.println(String.format("Imperio #%d criado\n", empire.get_id()));
        break;
      case "control":
        empire = (Empire) db.getEmpire().buscarId(toint(cmd[1]));
        if (empire == null) System.out.println("Imperio nao encontrado.\n");
        else empireMenu(empire, db);
        break;
      case "run":
        for (Entidade e : db.getEmpire().getEntidades()) 
          ((Empire) e).run_turn();
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
    System.out.println("house   # Ver menu de casas");
    System.out.println("farm    # Ver menu de fazendas");
    System.out.println("mine    # Ver menu de minas");
    System.out.println("lumber  # Ver menu de campos de lenhador");
    System.out.println("army    # Ver menu de exercito");
    System.out.println("war     # Ver menu de guerra");
    System.out.println("back    # Voltar pro menu anterior");
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
    System.out.println("build  # Construir");
    System.out.println("back   # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "build":
        System.out.println(empire.build_house() ? "Casa construida.\n" : "Recursos insuficientes.\n");
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
    System.out.println("view                # Ver fazendas");
    System.out.println("build               # Construir nova fazenda");
    System.out.println("send <amount> <id>  # Envia *amount* trabalhadores pra fazenda *id*");
    System.out.println("take <amount> <id>  # Tira *amount* trabalhadores da fazenda *id*");
    System.out.println("back                # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Farm farm : empire.getFarms()) System.out.println(farm);
        System.out.println();
        break;
      case "build":
        System.out.println(empire.build_farm() ? "Fazenda construida.\n" : "Recursos insuficientes.\n");
        break;
      case "send":
        Farm farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null) System.out.println("Fazenda inexistente.\n");
        else if (farm.get_empire_id() != empire.get_id()) System.out.println("Essa fazenda nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d trabalhadores enviados.\n", empire.send_workers_to_farm(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null) System.out.println("Fazenda inexistente.\n");
        else if (farm.get_empire_id() != empire.get_id()) System.out.println("Essa fazenda nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d trabalhadores retirados.\n", empire.take_workers_from_farm(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "back":
        return;
    }

    farmMenu(empire, db);
  }

  public static void mineMenu(Empire empire, BancoDeDados db) {
    System.out.println(empire);
    System.out.println("");
    System.out.println("Minas - Produzem minerios");
    System.out.println("Preco de construcao [Madeira: 15; Ouro: 5]");
    System.out.println("");
    System.out.println("view                # Ver minas");
    System.out.println("build               # Construir nova mina");
    System.out.println("send <amount> <id>  # Envia *amount* trabalhadores pra mina *id*");
    System.out.println("take <amount> <id>  # Tira *amount* trabalhadores da mina *id*");
    System.out.println("back                # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Mine mine : empire.getMines()) System.out.println(mine);
        System.out.println();
        break;
      case "build":
        System.out.println(empire.build_mine() ? "Mina construida.\n" : "Recursos insuficientes.\n");
        break;
      case "send":
        Mine mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null) System.out.println("Mina inexistente.\n");
        else if (mine.get_empire_id() != empire.get_id()) System.out.println("Essa mina nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d trabalhadores enviados.\n", empire.send_workers_to_mine(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null) System.out.println("Mina inexistente.\n");
        else if (mine.get_empire_id() != empire.get_id()) System.out.println("Essa mina nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d trabalhadores retirados.\n", empire.take_workers_from_mine(toint(cmd[1]), toint(cmd[2]))));
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
    System.out.println("view           # Ver campos");
    System.out.println("send <amount>  # Envia *amount* trabalhadores pro campo");
    System.out.println("take <amount>  # Tira *amount* trabalhadores do campo");
    System.out.println("back           # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        System.out.println(empire.getLumber());
        System.out.println();
        break;
      case "send":
        System.out.println(String.format("%d trabalhadores enviados.\n", empire.send_workers_to_lumber(toint(cmd[1]))));
        break;
      case "take":
        System.out.println(String.format("%d trabalhadores retirados.\n", empire.take_workers_from_lumber(toint(cmd[1]))));
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
    System.out.println("Preço de construcao [Ferro: 50; Ouro: 20]");
    System.out.println("");
    System.out.println("view                # Ver exercitos");
    System.out.println("new                 # Criar novo exercito");
    System.out.println("send <amount> <id>  # Envia *amount* tropas pro exercito *id*");
    System.out.println("take <amount> <id>  # Tira *amount* trabalhadores do exercito *id*");
    System.out.println("upgrade <id>        # Melhora a armadura do exercito *id*");
    System.out.println("back                # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Army army : empire.getArmies()) System.out.println(army);
        System.out.println();
        break;
      case "new":
        System.out.println(empire.create_army() ? "Exercito criado.\n" : "Recursos insuficientes.\n");
        break;
      case "send":
        Army army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null) System.out.println("Esse exercito nao existe.\n");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d trabalhadores se tornaram soldados.\n", empire.send_workers_to_army(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null) System.out.println("Esse exercito nao existe.\n");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.\n");
        else System.out.println(String.format("%d tropas retiradas.\n", empire.take_workers_from_army(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "upgrade":
        army = (Army) db.getArmy().buscarId(toint(cmd[1]));
        if (army == null) System.out.println("Esse exercito nao existe.\n");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.\n");
        int costIron = ((modelo.Army) army).IRON_COST_ARMORY;
        int costGold = ((modelo.Army) army).GOLD_COST_ARMORY;
        System.out.println(String.format("Custo por nivel: %d Ferro e %d Ouro.\n", costIron, costGold));
        System.out.println("Quantos niveis voce deseja melhorar?");

        int pontos = sc.nextInt();
        pontos = ((modelo.Army) army).upgrade_armory(pontos, empire);
        if (pontos > 0) System.out.println(String.format("Armadura melhorada em %d ponto(s).\n", pontos));
        else System.out.println("Recursos (Ferro/Ouro) insuficientes para esta melhoria.\n");
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
    System.out.println("view  # Ver batalhas em andamento");
    System.out.println("new   # Iniciar nova batalha");
    System.out.println("back  # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        if (db.getBattle().getSize() == 0) System.out.println("Sem batalhas");
        
        for (int i = db.getBattle().getSize()-1; i >= 0; i--) {
          Battle batalhas = ((Battle)db.getBattle().buscarId(i));

          String attackerName = "Army #" + batalhas.getAttacker().get_id();
          String defenderName = "Army #" + batalhas.getDefender().get_id();
          System.out.println("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          System.out.println("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
          System.out.println("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());
        }
        break;
      case "new":

        // TROPA ATACANTE
        System.out.println("Digite o ID da tropa atacante:");
        int attackerId = sc.nextInt();
        Army attackerArmy = (Army) db.getArmy().buscarId(attackerId);

        if (attackerArmy == null || attackerArmy.getEmpire_id() != empire.get_id()) {
          System.out.println("Tropa atacante invalida ou nao pertence ao seu imperio.");
        } else {
          // TROPA DEFENSORA
          System.out.println("Digite o ID da tropa defensora:");
          int defenderId = sc.nextInt();
          Army defenderArmy = (Army) db.getArmy().buscarId(defenderId);

          if (defenderArmy == null || defenderArmy.getEmpire_id() == empire.get_id()) {
            System.out.println("Tropa atacante invalida ou pertence ao seu imperio.");
          } else {
            Battle new_battle = new Battle(attackerArmy, defenderArmy, db);
            db.getBattle().inserir(new_battle);
            System.out.println("Batalha iniciada!");
          }
        }
        break;
      case "back":
        return;
    }

    warMenu(empire, db);
  }

  
}
