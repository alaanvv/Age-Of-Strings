package visao;

import java.util.Scanner;
import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import modelo.Entidade;
import modelo.Farm;
import modelo.LumberCamp;
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
    System.out.println("exit          # Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "create":
        Empire empire = new Empire(db);
        db.getEmpire().inserir(empire);
        // TO DO Verificar pq que tem q fazer isso
        db.getLumberCamp().inserir(empire.getLumber_camp());
        System.out.println(String.format("Imperio #%d criado", empire.get_id()));
        break;
      case "control":
        Empire controlled_empire = (Empire) db.getEmpire().buscarId(toint(cmd[1]));
        if (controlled_empire == null) {
          System.out.println("Imperio nao encontrado");
          break;
        }
        empireMenu(controlled_empire, db);
        break;
      case "exit":
        sc.close();
        return;
    }

    mainMenu(db);
  }

  public static void empireMenu(Empire empire, BancoDeDados db) {
    System.out.println(String.format("IMPERIO #%d", empire.get_id()));
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
    System.out.println("CASA");
    System.out.println("Aumenta a populacao");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 5]");
    System.out.println("");
    System.out.println("build  # Construir");
    System.out.println("back   # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "build":
        System.out.println(empire.build_house() ? "Casa construida." : "Recursos insuficientes.");
        break;
      case "back":
        return;
    }

    houseMenu(empire, db);
  }

  public static void farmMenu(Empire empire, BancoDeDados db) {
    System.out.println("FAZENDAS");
    System.out.println("Produzem comida");
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
        for (Entidade fazendas : db.getFarm().getEntidades())
          if (((Farm) fazendas).getEmpire_id() == empire.get_id())
            System.out.println(fazendas);
        break;
      case "build":
        if (empire.build_farm()) System.out.println(String.format("Construida Fazenda #%d", db.getFarm().getSize() - 1));
        else System.out.println("Recursos insuficientes.");
        break;
      case "send":
        Farm farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null) System.out.println("Fazenda inexistente.");
        else if (farm.getEmpire_id() != empire.get_id()) System.out.println("Essa fazenda nao pertence a esse imperio.");
        else System.out.println(String.format("%d trabalhadores enviados.", empire.send_workers_to_farm(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        farm = (Farm) db.getFarm().buscarId(toint(cmd[2]));
        if (farm == null) System.out.println("Fazenda inexistente.");
        else if (farm.getEmpire_id() != empire.get_id()) System.out.println("Essa fazenda nao pertence a esse imperio.");
        else System.out.println(String.format("%d trabalhadores retirados.", empire.take_workers_from_farm(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "back":
        return;
    }

    farmMenu(empire, db);
  }

  public static void mineMenu(Empire empire, BancoDeDados db) {
    System.out.println("MINAS");
    System.out.println("Produzem minerios");
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
        for (Entidade mines : db.getMine().getEntidades())
          if (((Mine) mines).getEmpire_id() == empire.get_id())
            System.out.println(mines);
        break;
      case "build":
        if (empire.build_mine()) System.out.println(String.format("Construida Mina #%d", db.getMine().getSize() - 1));
        else System.out.println("Recursos insuficientes.");
        break;
      case "send":
        Mine mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null) System.out.println("Mina inexistente.");
        else if (mine.getEmpire_id() != empire.get_id()) System.out.println("Essa mina nao pertence a esse imperio.");
        else System.out.println(String.format("%d trabalhadores enviados.", empire.send_workers_to_mine(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "take":
        mine = (Mine) db.getMine().buscarId(toint(cmd[2]));
        if (mine == null) System.out.println("Mina inexistente.");
        else if (mine.getEmpire_id() != empire.get_id()) System.out.println("Essa mina nao pertence a esse imperio.");
        else System.out.println(String.format("%d trabalhadores retirados.", empire.take_workers_from_mine(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "back":
        return;
    }

    mineMenu(empire, db);
  }

  public static void lumberMenu(Empire empire, BancoDeDados db) {
    System.out.println("CAMPOS DE LENHADOR");
    System.out.println("Produzem madeira");
    System.out.println("");
    System.out.println("view           # Ver campos");
    System.out.println("send <amount>  # Envia *amount* trabalhadores pro campo");
    System.out.println("take <amount>  # Tira *amount* trabalhadores do campo");
    System.out.println("back           # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Entidade lumbers : db.getLumberCamp().getEntidades())
          if (((LumberCamp) lumbers).getEmpire_id() == empire.get_id())
            System.out.println(lumbers);
        break;
      case "send":
        System.out.println(String.format("%d trabalhadores enviados.", empire.send_workers_to_lumber_camp(toint(cmd[1]))));
        break;
      case "take":
        System.out.println(String.format("%d trabalhadores retirados.", empire.take_workers_from_lumber_camp(toint(cmd[1]))));
        break;
      case "back":
        return;
    }

    lumberMenu(empire, db);
  }

  public static void armyMenu(Empire empire, BancoDeDados db) {
    System.out.println("EXERCITOS");
    System.out.println("Usados pra atacar e se defender de outros imperios");
    System.out.println("Preco de construcao [Ferro: 50; Ouro: 20; Comida: 1]");
    System.out.println("");
    System.out.println("view                # Ver exercitos");
    System.out.println("new                 # Criar novo exercito");
    System.out.println("send <amount> <id>  # Envia *amount* tropas pro exercito *id*");
    System.out.println("take <amount> <id>  # Tira *amount* trabalhadores do exercito *id*");
    System.out.println("upgrade <id>        # Melhora a armadura do exercito *id*");
    System.out.println("feed <amount> <id>  # Envia *amount* comida pro exercito *id*");
    System.out.println("back                # Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        for (Entidade armies : db.getArmy().getEntidades())
          if (((Army) armies).getEmpire_id() == empire.get_id())
            System.out.println(armies);
        break;
      case "new":
        if (empire.create_army()) System.out.println(String.format("Criado Exercito #%d", db.getArmy().getSize() - 1));
        else System.out.println("Recursos insuficientes.");
        break;
      case "send":
        Army army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null) System.out.println("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.");
        else {
          if (army.allocate_work(empire, toint(cmd[1]))) System.out.println(String.format("%d trabalhadores se tornaram soldados.", toint(cmd[1])));
          else System.out.println("Populacao insuficiente.");
        }
        break;
      case "take":
        army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null) System.out.println("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.");
        else System.out.println(String.format("%d tropas retiradas.", empire.take_workers_from_army(toint(cmd[1]), toint(cmd[2]))));
        break;
      case "upgrade":
        army = (Army) db.getArmy().buscarId(toint(cmd[1]));

        if (army == null) System.out.println("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.");

        int costIron = ((modelo.Army) army).IRON_COST_ARMORY;
        int costGold = ((modelo.Army) army).GOLD_COST_ARMORY;

        System.out.println(String.format("Custo por nivel: %d Ferro e %d Ouro.", costIron, costGold));
        System.out.println("Quantos niveis voce deseja melhorar?");

        int pontos = sc.nextInt();
        pontos = ((modelo.Army) army).upgrade_armory(pontos, empire);

        if (pontos > 0) System.out.println(String.format("Armadura melhorada em %d ponto(s).", pontos));
        else System.out.println("Recursos (Ferro/Ouro) insuficientes para esta melhoria.");
        break;

      case "feed":
        army = (Army) db.getArmy().buscarId(toint(cmd[2]));
        if (army == null) System.out.println("Esse exercito nao existe.");
        else if (army.getEmpire_id() != empire.get_id()) System.out.println("Esse exercito nao pertence a esse imperio.");
        
        int food_supply = toint(cmd[1]);
        if (food_supply > empire.getFood() || food_supply < 0) {
          System.out.println("Quantidade invalida.");
          break;
        }
        int food_spent = ((modelo.Army) army).supply_food(food_supply);
        empire.setFood(empire.getFood() - food_spent);
        System.out.println(String.format("%d de comida transferida.", food_spent));
        break;
      case "back":
        return;
    }

    armyMenu(empire, db);
  }

  public static void warMenu(Empire empire, BancoDeDados db) {
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
          int result = batalhas.simulate_round();
          String attackerName = "Army #" + batalhas.getAttacker().get_id();
          String defenderName = "Army #" + batalhas.getDefender().get_id();
          System.out.println("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          System.out.println("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
          System.out.println("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());
          if (result == 1) {
            System.out.println(attackerName + " Venceu a batalha! Vitoria dos atacantes.");
            db.getBattle().remover(batalhas.get_id());
          } else if (result == -1) {
            System.out.println(defenderName + " Venceu a batalha! Vitoria dos defensores.");
            db.getBattle().remover(batalhas.get_id());
          } else {
            System.out.println("A batalha continua... Nenhum vencedor nesta rodada.");
          }
        }

        for (Entidade armies : db.getArmy().getEntidades())
          if (((Army) armies).getEmpire_id() == empire.get_id())
            System.out.println(armies);
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
