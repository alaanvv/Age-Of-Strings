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

  // ---

  public static void mainMenu(BancoDeDados db) {
    print("empire", "Ver menu de imperio");
    print("house", "Ver menu de casa");
    print("farm", "Ver menu de fazenda");
    print("mine", "Ver menu de mina");
    print("lumber", "Ver menu de campo de lenhador");
    print("army", "Ver menu de exercito");
    print("battle", "Ver menu de batalha");
    print("run", "Roda o turno");
    print("exit", "Sair do jogo");
    String[] cmd = read();

    switch (cmd[0]) {
      case "empire":
        empireMenu(db);
        break;
      case "house":
        houseMenu(db);
        break;
      case "farm":
        farmMenu(db);
        break;
      case "mine":
        mineMenu(db);
        break;
      case "lumber":
        lumberMenu(db);
        break;
      case "army":
        armyMenu(db);
        break;
      case "battle":
        battleMenu(db);
        break;
      case "run":
        for (Entidade e : db.getEmpires().getEntidades()) log(((Empire) e).runTurn());
        log("Turno rodado.");
        break;
      case "exit":
        sc.close();
        return;
    }

    mainMenu(db);
  }

  public static void empireMenu(BancoDeDados db) {
    print("new <name>", "Criar imperio");
    print("view <id>", "Ver imperio *id*");
    print("viewall", "Ver todos imperios");
    print("rename <id> <name>", "Renomeia o imperio *id*");
    print("destroy <id>", "Destroi o imperio *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = new Empire(db, cmd[1]);
        db.getEmpires().insert(empire);
        log("Imperio criado.");
        break;
      case "view":
        empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else log(empire.toString());
        break;
      case "viewall":
        if (!db.hasEmpire()) log("Nao existem imperios.");
        for (Entidade e : db.getEmpires().getEntidades()) log(e.toString());
        break;
      case "rename":
        empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else {
          empire.setName(cmd[2]);
          log("Imperio renomeado.");
        }
        break;
      case "destroy":
        empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else {
          empire.destroy();
          log("Imperio destruido.");
        }
        break;
      case "back":
        return;
    }

    empireMenu(db);
  }

  public static void houseMenu(BancoDeDados db) {
    System.out.println("Casa - Aumenta a populacao");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 5]");
    System.out.println("");
    print("new <id>", "Construir uma casa no imperio *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else log(empire.buildHouse() ? "Casa construida." : "Recursos insuficientes.");
        break;
      case "back":
        return;
    }

    houseMenu(db);
  }

  public static void farmMenu(BancoDeDados db) {
    System.out.println("Fazenda - Produz comida");
    System.out.println("Preco de construcao [Madeira: 5; Ouro: 3]");
    System.out.println("");
    print("new <empire>", "Construir nova fazenda");
    print("view <id>", "Ver fazenda *id*");
    print("viewall", "Ver todas fazendas");
    print("send <amount> <id>", "Envia *amount* trabalhadores pra fazenda *id*");
    print("take <amount> <id>", "Tira *amount* trabalhadores da fazenda *id*");
    print("destroy <id>", "Destroi a fazenda *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else log(empire.buildFarm() ? "Fazenda construida." : "Recursos insuficientes.");
        break;
      case "view":
        Farm farm = (Farm) db.getFarms().findById(parseInt(cmd[1]));
        if (farm == null) log("Fazenda inexistente.");
        else log(farm.toString());
        break;
      case "viewall":
        if (!db.hasFarm()) log("Nao existem fazendas.");
        for (Entidade f : db.getFarms().getEntidades()) log(f.toString());
        break;
      case "send":
        farm = (Farm) db.getFarms().findById(parseInt(cmd[2]));
        if (farm == null) log("Fazenda inexistente.");
        else log(String.format("%d trabalhadores enviados.", ((Empire) db.getEmpires().findById(farm.getEmpireId())).sendWorkersToFarm(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        farm = (Farm) db.getFarms().findById(parseInt(cmd[2]));
        if (farm == null) log("Fazenda inexistente.");
        else log(String.format("%d trabalhadores retirados.", ((Empire) db.getEmpires().findById(farm.getEmpireId())).takeWorkersFromFarm(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "destroy":
        farm = (Farm) db.getFarms().findById(parseInt(cmd[1]));
        if (farm == null) log("Fazenda inexistente.");
        else {
          farm.destroy();
          log("Fazenda destruida.");
        }
        break;
      case "back":
        return;
    }

    farmMenu(db);
  }

  public static void mineMenu(BancoDeDados db) {
    System.out.println("Mina - Produz ferro e ouro");
    System.out.println("Preco de construcao [Madeira: 15; Ouro: 5]");
    System.out.println("");
    print("new <empire>", "Construir nova mina");
    print("view <id>", "Ver mina *id*");
    print("viewall", "Ver todas minas");
    print("send <amount> <id>", "Envia *amount* trabalhadores pra mina *id*");
    print("take <amount> <id>", "Tira *amount* trabalhadores da mina *id*");
    print("destroy <id>", "Destroi a mina");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = (Empire) db.getEmpires().findById(parseInt(cmd[1])); 
        if (empire == null) log("Imperio inexistente.");
        else log(empire.buildMine() ? "Mina construida." : "Recursos insuficientes.");
        break;
      case "view":
        Mine mine = (Mine) db.getMines().findById(parseInt(cmd[1]));
        if (mine == null) log("Mina inexistente.");
        else log(mine.toString());
        break;
      case "viewall":
        if (!db.hasMine()) log("Nao existem minas.");
        for (Entidade m : db.getMines().getEntidades()) log(m.toString());
        break;
      case "send":
        mine = (Mine) db.getMines().findById(parseInt(cmd[2]));
        if (mine == null) log("Mina inexistente.");
        else log(String.format("%d trabalhadores enviados.", ((Empire) db.getEmpires().findById(mine.getEmpireId())).sendWorkersToMine(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        mine = (Mine) db.getMines().findById(parseInt(cmd[2]));
        if (mine == null) log("Mina inexistente.");
        else log(String.format("%d trabalhadores retirados.", ((Empire) db.getEmpires().findById(mine.getEmpireId())).takeWorkersFromMine(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "destroy":
        mine = (Mine) db.getMines().findById(parseInt(cmd[1]));
        if (mine == null) log("Mina inexistente.");
        else {
          mine.destroy();
          log("Mina destruida.");
        }
        break;
      case "back":
        return;
    }

    mineMenu(db);
  }

  public static void lumberMenu(BancoDeDados db) {
    System.out.println("Campo de lenhador - Produz madeira");
    System.out.println("");
    print("view <id>", "Ver campo *id*");
    print("viewall", "Ver todos campos");
    print("send <amount> <id>", "Envia *amount* trabalhadores pro campo *id*");
    print("take <amount> <id>", "Tira *amount* trabalhadores do campo *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "view":
        Lumber lumber = (Lumber) db.getLumbers().findById(parseInt(cmd[1]));
        if (lumber == null) log("Campo inexistente.");
        else log(lumber.toString());
        break;
      case "viewall":
        if (!db.hasLumber()) log("Nao existem campos.");
        for (Entidade l : db.getLumbers().getEntidades()) log(l.toString());
        break;
      case "send":
        lumber = (Lumber) db.getLumbers().findById(parseInt(cmd[2]));
        if (lumber == null) log("Campo inexistente.");
        else log(String.format("%d trabalhadores enviados.", ((Empire) db.getEmpires().findById(lumber.getEmpireId())).sendWorkersToLumber(parseInt(cmd[1]))));
        break;
      case "take":
        lumber = (Lumber) db.getLumbers().findById(parseInt(cmd[2]));
        if (lumber == null) log("Campo inexistente.");
        else log(String.format("%d trabalhadores retirados.", ((Empire) db.getEmpires().findById(lumber.getEmpireId())).takeWorkersFromLumber(parseInt(cmd[1]))));
        break;
      case "back":
        return;
    }

    lumberMenu(db);
  }

  public static void armyMenu(BancoDeDados db) {
    System.out.println("Exercitos - Usados pra atacar e se defender de outros imperios");
    System.out.println("Preco de construcao [Ferro: 50; Ouro: 20]");
    System.out.println("Preco de melhoria   [Ferro: 25; Ouro:  5]");
    System.out.println("");
    print("new <empire>", "Criar novo exercito");
    print("view <id>", "Ver exercito *id*");
    print("viewall", "Ver todos exercitos");
    print("send <amount> <id>", "Envia *amount* tropas pro exercito *id*");
    print("take <amount> <id>", "Tira *amount* trabalhadores do exercito *id*");
    print("upgrade <amount> <id>", "Melhora a armadura do exercito *id* em *amount* niveis");
    print("destroy <id>", "Destroi o exercito *id*");
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "new":
        Empire empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) log("Imperio inexistente.");
        else log(empire.createArmy() ? "Exercito criado." : "Recursos insuficientes.");
        break;
      case "view":
        Army army = (Army) db.getArmies().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else log(army.toString());
        break;
      case "viewall":
        if (!db.hasArmy()) log("Nao existem exercitos.");
        for (Entidade a : db.getArmies().getEntidades()) log(a.toString());
        break;
      case "send":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null) log("Exercito inexistente.");
        else log(String.format("%d trabalhadores enviados.", ((Empire) db.getEmpires().findById(army.getEmpireId())).sendWorkersToArmy(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "take":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null) log("Exercito inexistente.");
        else log(String.format("%d trabalhadores retirados.", ((Empire) db.getEmpires().findById(army.getEmpireId())).takeWorkersFromArmy(parseInt(cmd[1]), parseInt(cmd[2]))));
        break;
      case "destroy":
        army = (Army) db.getArmies().findById(parseInt(cmd[1]));
        if (army == null) log("Exercito inexistente.");
        else {
          army.destroy();
          log("Exercito destruido.");
        }
        break;
      case "upgrade":
        army = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (army == null) log("Exercito inexistente.");
        else {
          empire = (Empire) db.getEmpires().findById(army.getEmpireId());
          int pontos = army.upgradeArmory(parseInt(cmd[1]), empire);
          log(pontos > 0 ? String.format("Armadura melhorada em %d ponto(s).", pontos) : "Recursos insuficientes para esta melhoria.");
        }
        break;
      case "back":
        return;
    }

    armyMenu(db);
  }

  public static void battleMenu(BancoDeDados db) {
    System.out.println("Batalhas");
    System.out.println("");
    print("new <empire> <atk_id> <dfn_id>", "Iniciar nova batalha usando a tropa *atk_id* pra atacar *dfn_id*");
    print("insertsoldier <battle_id> <attacker/defender> <amt>", "Inserir soldados temporários no exército *attacker/defender*");
    print("deletesoldier <battle_id> <attacker/defender> <pos>", "Deletar soldados no exército *attacker/defender*");
    print("view <id>", "Ver batalha com *id*");
    print("viewall", "Ver todas batalhas");
    print("destroy <id>", "Destroi a batalha *id* (0-%d)", db.getBattles().getSize() - 1);
    print("back", "Voltar pro menu anterior");
    String[] cmd = read();

    switch (cmd[0]) {
      case "viewall":
        for (int i = db.getBattles().getSize() - 1; i >= 0; i--) {
          Battle batalhas = (Battle) db.getBattles().findById(i);
          String attackerName = "Army #" + batalhas.getAttacker().getId();
          String defenderName = "Army #" + batalhas.getDefender().getId();
          log("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
          log("Soldados Atacantes vivos: " + batalhas.getAttackerSoldiersAlive());
          log("Soldados Defensores vivos: " + batalhas.getDefenderSoldiersAlive());
        }
        break;
      case "view":
        Battle battle = (Battle) db.getBattles().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else log(battle.toString());
        break;
        
        case "new":
        Empire empire = (Empire) db.getEmpires().findById(parseInt(cmd[1]));
        if (empire == null) {log("Imperio inexistente."); break;}
        Army attackerArmy = (Army) db.getArmies().findById(parseInt(cmd[2]));
        if (attackerArmy == null || attackerArmy.getEmpireId() != empire.getId()) {
          log("Tropa atacante invalida ou nao pertence ao imperio selecionado.");
        } else {
          Army defenderArmy = (Army) db.getArmies().findById(parseInt(cmd[3]));
          if (defenderArmy == null || defenderArmy.getEmpireId() == empire.getId()) {
            log("Tropa defensora invalida ou pertence ao seu imperio.");
          } else {
            Battle newBattle = new Battle(attackerArmy, defenderArmy, db);
            db.getBattles().insert(newBattle);
            log("Batalha iniciada!");
          }
        }
        break;
      
        case "insertsoldier":
          Battle battle_ = (Battle) db.getBattles().findById(parseInt(cmd[1]));
          if (battle_ == null) {log("Batalha inexistente."); break;}
          if(cmd[2].equals("attacker") || cmd[2].equals("defender")){
            battle_.insertSoldier(cmd[2].equals("attacker"), parseInt(cmd[3]));
            log("Soldados inseridos com sucesso!.");
          } else{
            log("Não foi possível ler o exército em que será adicionado os soldados.");
          }
          break;
          
        case "deletesoldier":
          battle_ = (Battle) db.getBattles().findById(parseInt(cmd[1]));

          if (battle_ == null) {log("Batalha inexistente."); break;}

          if(cmd[2].equals("attacker") || cmd[2].equals("defender")){
            if(battle_.deleteSoldier(cmd[2].equals("attacker"), parseInt(cmd[3]))){
              log("Soldados removidos com sucesso!.");
            } else {
              log("Posição deve estar entre 0 e " + (battle_.getSoldiersSize(cmd[2].equals("attacker"))-1) + ".");
            }
            
          } else{
            log("Não foi possível ler o exército em que será adicionado os soldados.");
          }
          break;
        
      case "destroy":
        battle = (Battle) db.getBattles().findById(parseInt(cmd[1]));
        if (battle == null) log("Batalha inexistente.");
        else {
          battle.destroy();
          log(battle.toString());
        }
        break;
      case "back":
        return;
    }

    battleMenu(db);
  }
}
