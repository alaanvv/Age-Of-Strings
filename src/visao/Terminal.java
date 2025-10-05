package visao;

import java.util.Scanner;

import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import persistencia.BancoDeDados;

public class Terminal {

    public static void menuFarm(Empire empire, BancoDeDados banco,Scanner scanner){
        while (true){
            System.out.println("\n--- Gerenciamento de Fazendas ---");
            for (modelo.Entidade fazendas : banco.getFarm().getListaDeEntidades()){

                if (((modelo.Farm)fazendas).getEmpire_id() == empire.get_id()){
                    System.out.println(fazendas);
                }
            }
            System.out.println("PRECO | Madeira: 5 | Ouro: 3|");
            System.out.println("1-Criar nova fazenda\n2-Enviar trabalhadores\n3-Retirar trabalhadores\n0-Voltar");
            int option = scanner.nextInt();

            //CONSTRUIR
            if (option == 1){
                if(empire.build_farm()){
                    System.out.println("Farm id: " + (banco.getFarm().getSize()-1));
                }
                else{
                System.out.println("Recursos insuficientes.");
                }
            }
            //ENVIAR TRABALHADORES
            else if (option == 2){
                System.out.println("Digite o id da fazenda:");
                int id = scanner.nextInt();

                System.out.println("Quantos trabalhadores voce quer enviar para a fazenda? (Populacao atual: " + empire.get_population() + ")");
                int amount = scanner.nextInt();

                modelo.Entidade a_farm = banco.getFarm().buscarId(id);

                if (a_farm == null){
                    System.out.println("Essa fazenda nao existe.");
                }
                else if (((modelo.Farm) a_farm).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa fazenda nao pertence a esse imperio.");
                }
                else{
                    System.out.println(String.format("%d trabalhadores enviados pra Fazenda #%d.", empire.send_workers_to_farm(amount, id), id));
                } 
            }
            //RETIRAR TRABALHADORES
            else if (option == 3){
                System.out.println("Digite o id da fazenda:");
                int id = scanner.nextInt();

                System.out.println("Digite a quantidade a ser retirada:");
                int amount = scanner.nextInt();

                modelo.Entidade a_farm = banco.getFarm().buscarId(id);

                if (a_farm == null){
                    System.out.println("Essa fazenda nao existe.");
                }
                else if (((modelo.Farm) a_farm).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa fazenda nao pertence a esse imperio.");
                }
                else{
                    amount = Math.min(amount, ((modelo.Farm) a_farm).getWorkers());
                    System.out.println(String.format("%d trabalhadores retirados da Fazenda #%d.", empire.take_workers_from_farm(amount, id), id));
                }
            }
            else if (option == 0){
                break;
            }
        }
    }

    public static void menuMine(Empire empire, BancoDeDados banco,Scanner scanner){
        while (true){
            System.out.println("\n--- Gerenciamento de Minas ---");
            for (modelo.Entidade minas : banco.getMine().getListaDeEntidades()){

                if (((modelo.Mine)minas).getEmpire_id() == empire.get_id()){
                    System.out.println(minas);
                }
            }
            System.out.println("PRECO | Madeira: 15 | Ouro: 5|");
            System.out.println("1-Criar nova mina\n2-Enviar trabalhadores\n3-Retirar trabalhadores\n0-Voltar");
            int option = scanner.nextInt();

            //CONSTRUIR
            if (option == 1){
                if(empire.build_mine()){
                    System.out.println("Mine id: " + (banco.getMine().getSize()-1));
                }
                else{
                System.out.println("Recursos insuficientes.");
                }
            }
            //ENVIAR TRABALHADORES
            else if (option == 2){
                System.out.println("Digite o id da mina:");
                int id = scanner.nextInt();

                System.out.println("Quantos trabalhadores voce quer enviar para a mina? (Populacao atual: " + empire.get_population() + ")");
                int amount = scanner.nextInt();

                modelo.Entidade a_mine = banco.getMine().buscarId(id);

                if (a_mine == null){
                    System.out.println("Essa mina nao existe.");
                }
                else if (((modelo.Mine) a_mine).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa mina nao pertence a esse imperio.");
                }
                else{
                    System.out.println(String.format("%d trabalhadores enviados pra mina #%d.", empire.send_workers_to_mine(amount, id), id));
                } 
            }
            //RETIRAR TRABALHADORES
            else if (option == 3){
                System.out.println("Digite o id da mina:");
                int id = scanner.nextInt();

                System.out.println("Digite a quantidade a ser retirada:");
                int amount = scanner.nextInt();

                modelo.Entidade a_mine = banco.getMine().buscarId(id);

                if (a_mine == null){
                    System.out.println("Essa mina nao existe.");
                }
                else if (((modelo.Mine) a_mine).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa mina nao pertence a esse imperio.");
                }
                else{
                    amount = Math.min(amount, ((modelo.Mine) a_mine).getWorkers());
                    System.out.println(String.format("%d trabalhadores retirados da mina #%d.", empire.take_workers_from_mine(amount, id), id));
                }
            }
            else if (option == 0){
                break;
            }
        }
    }

    public static void menuLumber(Empire empire, BancoDeDados banco,Scanner scanner){
        while (true){
            System.out.println("\n--- Gerenciamento de campo de cortar madeira ---");
            for (modelo.Entidade lumber : banco.getLumberCamp().getListaDeEntidades()){

                if (((modelo.LumberCamp)lumber).getEmpire_id() == empire.get_id()){
                    System.out.println(lumber);
                }
            }
            System.out.println("1-Enviar trabalhadores\n2-Retirar trabalhadores\n0-Voltar");
            int option = scanner.nextInt();

            //ENVIAR TRABALHADORES
            if (option == 1){
                int id = empire.get_id();

                System.out.println("Quantos trabalhadores voce quer enviar para a floresta? (Populacao atual: " + empire.get_population() + ")");
                int amount = scanner.nextInt();

                modelo.Entidade a_lumber = banco.getLumberCamp().buscarId(id);

                System.out.println(String.format("%d trabalhadores enviados pra mina #%d.", empire.send_workers_to_lumber_camp(amount), id));
            }
            //RETIRAR TRABALHADORES
            else if (option == 2){
                int id = empire.get_id();

                System.out.println("Quantos trabalhadores voce quer retirar da floresta?");
                int amount = scanner.nextInt();

                modelo.Entidade a_lumber = banco.getLumberCamp().buscarId(id);
                    amount = Math.min(amount, ((modelo.LumberCamp) a_lumber).getWorkers());
                    System.out.println(String.format("%d trabalhadores retirados da floresta #%d.", empire.take_workers_from_lumber_camp(amount), id));
                }
            else if (option == 0){
                break;
            }
        }
    }
    
    public static void menuArmy(Empire empire, BancoDeDados banco, Scanner scanner){
        while (true){
            System.out.println("\n--- Gerenciamento do Exercito ---");
            for (modelo.Entidade armies : banco.getArmy().getListaDeEntidades()){

                if (((modelo.Army)armies).getEmpire_id() == empire.get_id()){
                    System.out.println(armies);
                }
            }

            System.out.println("PRECO | Ferro: 50 | Ouro: 20| Comida: 1 |");
            System.out.println("1-Criar nova tropa\n2-Contratar Soldados\n3-Liberar soldados\n4-Melhorar Armadura\n5-Reabastecer Comida\n0-Voltar");
            int option = scanner.nextInt();
            
            //CRIAR TROPA
            if (option == 1){
                if(empire.create_army()){
                    System.out.println("Army id: " + (banco.getArmy().getSize()-1));
                }
                else{
                System.out.println("Recursos insuficientes.");
                }
            }
            //CONTRATAR SOLDADOS
            else if (option == 2) {
                System.out.println("Digite o id da tropa:");
                int id = scanner.nextInt();

                System.out.println("Quantos trabalhadores voce quer alocar como soldados? (Populacao atual: " + empire.get_population() + ")");
                int amount = scanner.nextInt();

                modelo.Entidade a_army = banco.getArmy().buscarId(id);

                if (a_army == null){
                    System.out.println("Essa tropa nao existe.");
                }
                else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa tropa nao pertence a esse imperio.");
                }
                else{
                    if (((modelo.Army)a_army).allocate_work(empire, amount)) {
                        System.out.println(String.format("%d trabalhadores se tornaram soldados.", amount));
                    } else {
                        System.out.println("Populacao insuficiente.");
                    }
                } 
            } 
            //LIBERAR SOLDADOS
            else if (option == 3){
                System.out.println("Digite o id:");
                int id = scanner.nextInt();

                System.out.println("Quantos soldados voce quer liberar?");
                int amount = scanner.nextInt();

                modelo.Entidade a_army = banco.getArmy().buscarId(id);

                if (a_army == null){
                    System.out.println("Essa tropa nao existe.");
                }
                else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa tropa nao pertence a esse imperio.");
                }
                else{
                    amount = Math.min(amount, ((modelo.Army) a_army).getSoldiers_amt());
                    System.out.println(String.format("%d trabalhadores retirados da mina #%d.", empire.take_workers_from_army(amount, id), id));
                } 
            }
            //MELHORAR ARMADURA
            else if (option == 4){
                System.out.println("Digite o id da tropa:");
                int id = scanner.nextInt();

                modelo.Entidade a_army = banco.getArmy().buscarId(id);

                if (a_army == null){
                    System.out.println("Essa tropa nao existe.");
                }
                else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa tropa nao pertence a esse imperio.");
                }

                int costIron = ((modelo.Army)a_army).IRON_COST_ARMORY;
                int costGold = ((modelo.Army)a_army).GOLD_COST_ARMORY;

                System.out.println(String.format("Custo por nivel: %d Ferro e %d Ouro.", costIron, costGold));
                System.out.println("Quantos niveis voce deseja melhorar?");
                int pontos = scanner.nextInt();
                
                pontos = ((modelo.Army)a_army).upgrade_armory(pontos, empire);

                if (pontos > 0) {
                    System.out.println(String.format("Armadura melhorada em %d ponto(s).", pontos));
                } else {
                    System.out.println("Recursos (Ferro/Ouro) insuficientes para esta melhoria.");
                }
            }
            //REABASTECER COMIDA
            else if (option == 5){
                System.out.println("Digite o id da tropa:");
                int id = scanner.nextInt();

                modelo.Entidade a_army = banco.getArmy().buscarId(id);

                if (a_army == null){
                    System.out.println("Essa tropa nao existe.");
                }
                else if (((modelo.Army) a_army).getEmpire_id() != empire.get_id()){
                    System.out.println("Essa tropa nao pertence a esse imperio.");
                }

                System.out.println("Quanta comida voce quer transferir para o exercito?");
                int food_supply = scanner.nextInt();

                if (food_supply > empire.getFood() || food_supply < 0) {
                    System.out.println("Quantidade invalida.");
                    continue;
                }

                int food_spent = ((modelo.Army) a_army).supply_food(food_supply);
                empire.setFood(empire.getFood() - food_spent);

                System.out.println(String.format("%d de comida transferida.", food_spent));
            }
            //VOLTAR
            else if (option == 0){
                return;
            }
        }
    }

    public static void menuBatalhas(modelo.Empire empire, BancoDeDados banco, Scanner scanner){
        while (true){
            System.out.println("1-Comecar batalha\n2-Ver batalhas em andamento\n0-Voltar");
            int option = scanner.nextInt();

            if (option == 1){
                //TROPA ATACANTE
                System.out.println("Digite o ID da tropa atacante:");
                int attackerId = scanner.nextInt();
                Army attackerArmy = (Army) banco.getArmy().buscarId(attackerId);

                if (attackerArmy == null || attackerArmy.getEmpire_id() != empire.get_id()) {
                    System.out.println("Tropa atacante invalida ou nao pertence ao seu imperio.");
                }
                else{
                //TROPA DEFENSORA
                System.out.println("Digite o ID da tropa defensora:");
                int defenderId = scanner.nextInt();
                Army defenderArmy = (Army) banco.getArmy().buscarId(defenderId);

                if (defenderArmy == null || defenderArmy.getEmpire_id() == empire.get_id()) {
                    System.out.println("Tropa atacante invalida ou pertence ao seu imperio.");
                }
                else{
                    Battle new_battle = new Battle(attackerArmy, defenderArmy, banco);
                    banco.getBattles().inserir(new_battle);
                    System.out.println("Batalha iniciada!");
                }
            }      
            }
            else if (option == 2){
                if (banco.getBattles().getSize() == 0){
                    System.out.println("Sem batalhas");
                }
                else{
                    for (int i= banco.getBattles().getSize()-1; i >= 0; i--){
                        Battle batalhas = ((Battle)banco.getBattles().buscarId(i));
                        int result = batalhas.simulate_round();
                        String attackerName = "Army #" + batalhas.getAttacker().get_id();
                        String defenderName = "Army #" + batalhas.getDefender().get_id();

                        System.out.println("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
                        System.out.println("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
                        System.out.println("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());
                    
                        if (result == 1) {
                            System.out.println(attackerName + " Venceu a batalha! Vitoria dos atacantes.");
                            banco.getBattles().remover(batalhas.get_id());
                        } else if (result == -1) {
                            System.out.println(defenderName + " Venceu a batalha! Vitoria dos defensores.");
                            banco.getBattles().remover(batalhas.get_id());
                        } else {
                            System.out.println("A batalha continua... Nenhum vencedor nesta rodada.");
                        }
                    }
                }
            }
            else if (option == 0){
                break;
            }
        }
    }
}
