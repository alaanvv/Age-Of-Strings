package visao;

import java.util.Scanner;
import persistencia.BancoDeDados;
import modelo.Entidade;
import modelo.Farm;
import modelo.Mine;
import modelo.LumberCamp;
import modelo.Battle;
import modelo.Empire;
import modelo.Army;

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
    System.out.println("army    # Ver menu de exército");
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
    System.out.println("Preço de construcao [Madeira: 5; Ouro: 5]");
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
    System.out.println("Preço de construcao [Madeira: 5; Ouro: 3]");
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
    System.out.println("Preço de construcao [Madeira: 15; Ouro: 5]");
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

  public static void armyMenu(Empire empire, BancoDeDados banco) {
    while (true) {
      System.out.println("\n--- Gerenciamento do Exercito ---");
      for (modelo.Entidade armies : banco.getArmy().getEntidades()) {

        if (((modelo.Army) armies).getEmpire_id() == empire.get_id()) {
          System.out.println(armies);
        }
      }

      System.out.println("PRECO | Ferro: 50 | Ouro: 20| Comida: 1 |");
      System.out.println(
          "1-Criar nova tropa\n2-Contratar Soldados\n3-Liberar soldados\n4-Melhorar Armadura\n5-Reabastecer Comida\n0-Voltar");
      int option = sc.nextInt();

      // CRIAR TROPA
      if (option == 1) {
        if (empire.create_army()) {
          System.out.println("Army id: " + (banco.getArmy().getSize() - 1));
        } else {
          System.out.println("Recursos insuficientes.");
        }
      }
      // CONTRATAR SOLDADOS
      else if (option == 2) {
        System.out.println("Digite o id da tropa:");
        int id = sc.nextInt();

        System.out.println(
            "Quantos trabalhadores voce quer alocar como soldados? (Populacao atual: " + empire.get_population() + ")");
        int amount = sc.nextInt();

        modelo.Entidade a_army = banco.getArmy().buscarId(id);

        if (a_army == null) {
          System.out.println("Essa tropa nao existe.");
        } else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()) {
          System.out.println("Essa tropa nao pertence a esse imperio.");
        } else {
          if (((modelo.Army) a_army).allocate_work(empire, amount)) {
            System.out.println(String.format("%d trabalhadores se tornaram soldados.", amount));
          } else {
            System.out.println("Populacao insuficiente.");
          }
        }
      }
      // LIBERAR SOLDADOS
      else if (option == 3) {
        System.out.println("Digite o id:");
        int id = sc.nextInt();

        System.out.println("Quantos soldados voce quer liberar?");
        int amount = sc.nextInt();

        modelo.Entidade a_army = banco.getArmy().buscarId(id);

        if (a_army == null) {
          System.out.println("Essa tropa nao existe.");
        } else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()) {
          System.out.println("Essa tropa nao pertence a esse imperio.");
        } else {
          amount = Math.min(amount, ((modelo.Army) a_army).getSoldiers_amt());
          System.out.println(
              String.format("%d trabalhadores retirados da mina #%d.", empire.take_workers_from_army(amount, id), id));
        }
      }
      // MELHORAR ARMADURA
      else if (option == 4) {
        System.out.println("Digite o id da tropa:");
        int id = sc.nextInt();

        modelo.Entidade a_army = banco.getArmy().buscarId(id);

        if (a_army == null) {
          System.out.println("Essa tropa nao existe.");
        } else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()) {
          System.out.println("Essa tropa nao pertence a esse imperio.");
        }

        int costIron = ((modelo.Army) a_army).IRON_COST_ARMORY;
        int costGold = ((modelo.Army) a_army).GOLD_COST_ARMORY;

        System.out.println(String.format("Custo por nivel: %d Ferro e %d Ouro.", costIron, costGold));
        System.out.println("Quantos niveis voce deseja melhorar?");
        int pontos = sc.nextInt();

        pontos = ((modelo.Army) a_army).upgrade_armory(pontos, empire);

        if (pontos > 0) {
          System.out.println(String.format("Armadura melhorada em %d ponto(s).", pontos));
        } else {
          System.out.println("Recursos (Ferro/Ouro) insuficientes para esta melhoria.");
        }
      }
      // REABASTECER COMIDA
      else if (option == 5) {
        System.out.println("Digite o id da tropa:");
        int id = sc.nextInt();

        modelo.Entidade a_army = banco.getArmy().buscarId(id);

        if (a_army == null) {
          System.out.println("Essa tropa nao existe.");
        } else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()) {
          System.out.println("Essa tropa nao pertence a esse imperio.");
        }

        System.out.println("Quanta comida voce quer transferir para o exercito?");
        int food_supply = sc.nextInt();

        if (food_supply > empire.getFood() || food_supply < 0) {
          System.out.println("Quantidade invalida.");
          continue;
        }

        int food_spent = ((modelo.Army) a_army).supply_food(food_supply);
        empire.setFood(empire.getFood() - food_spent);

        System.out.println(String.format("%d de comida transferida.", food_spent));
      }
      // VOLTAR
      else if (option == 0) {
        return;
      }
    }
  }

  public static void warMenu(modelo.Empire empire, BancoDeDados banco) {
    while (true) {
      System.out.println("1-Comecar batalha\n2-Ver batalhas em andamento\n0-Voltar");
      int option = sc.nextInt();

      if (option == 1) {
        // TROPA ATACANTE
        System.out.println("Digite o ID da tropa atacante:");
        int attackerId = sc.nextInt();
        Army attackerArmy = (Army) banco.getArmy().buscarId(attackerId);

        if (attackerArmy == null || attackerArmy.getEmpire_id() != empire.get_id()) {
          System.out.println("Tropa atacante invalida ou nao pertence ao seu imperio.");
        } else {
          // TROPA DEFENSORA
          System.out.println("Digite o ID da tropa defensora:");
          int defenderId = sc.nextInt();
          Army defenderArmy = (Army) banco.getArmy().buscarId(defenderId);

          if (defenderArmy == null || defenderArmy.getEmpire_id() == empire.get_id()) {
            System.out.println("Tropa atacante invalida ou pertence ao seu imperio.");
          } else {
            Battle new_battle = new Battle(attackerArmy, defenderArmy, banco);
            banco.getBattle().inserir(new_battle);
            System.out.println("Batalha iniciada!");
          }
        }
      } else if (option == 2) {
        if (banco.getBattle().getSize() == 0) {
          System.out.println("Sem batalhas");
        } else {
          for (int i = banco.getBattle().getSize()-1; i >= 0; i--) {
            Battle batalhas = ((Battle)banco.getBattle().buscarId(i));
            int result = batalhas.simulate_round();
            String attackerName = "Army #" + batalhas.getAttacker().get_id();
            String defenderName = "Army #" + batalhas.getDefender().get_id();

            System.out.println("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
            System.out.println("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
            System.out.println("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());

            if (result == 1) {
              System.out.println(attackerName + " Venceu a batalha! Vitoria dos atacantes.");
              banco.getBattle().remover(batalhas.get_id());
            } else if (result == -1) {
              System.out.println(defenderName + " Venceu a batalha! Vitoria dos defensores.");
              banco.getBattle().remover(batalhas.get_id());
            } else {
              System.out.println("A batalha continua... Nenhum vencedor nesta rodada.");
            }
          }
        }
      } else if (option == 0) {
        break;
      }
    }
  }
}
