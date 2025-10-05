import java.util.Scanner;
import modelo.Army;
import modelo.Empire;
import persistency.BancoDeDados;

public class Milagre {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        BancoDeDados banco = new BancoDeDados();

        while(true){
            System.out.println("1-Criar Imperio\n2-Controlar Imperio\n0-Sair");
            int option = scanner.nextInt();

            if (option == 1){
                Empire novo = new Empire(banco);
                banco.getEmpire().inserir(novo);
                banco.getLumberCamp().inserir(novo.getLumber_camp());
                System.out.println("ID: " + novo.get_id());
            }
            else if (option == 2){
                System.out.println("Digite o id do imperio:");
                int idm = scanner.nextInt();

                modelo.Entidade imperio_atual = banco.getEmpire().buscarId(idm);

                if (imperio_atual == null){

                    System.out.println("Imperio nao encontrado");

                } else {

                    while(true){

                        System.out.println("1-Ver precos\n2-Construir\n3-Enviar\n4-Retirar\n5-Verificar\n6-Guerra\n7-Army\n0-Voltar");
                        option = scanner.nextInt();

                        //VER PREÇOS
                        if (option == 1){
                            while (true){
                                System.out.println("1-Farm\n2-Mine\n3-House\n4-Army\n0-Voltar");
                                option = scanner.nextInt();

                                if (option == 1){
                                    System.out.println("Madeira: 5 \nOuro: 3");
                                }
                                else if (option == 2){
                                    System.out.println("Madeira: 15\nOuro: 5");
                                }
                                else if (option == 3){
                                    System.out.println("Madeira: 5 \nOuro: 5");
                                }
                                else if (option == 4){
                                    System.out.println("Comida: 1 \nOuro: 20\nFerro: 50");
                                }
                                else if (option == 0){
                                    break;
                                }
                            }
                        }
                        //CONSTRUIR
                        else if (option == 2){
                            System.out.println("1-Farm\n2-Mine\n3-House\n4-Army\n0-Voltar");

                            option = scanner.nextInt();

                                if (option == 1){

                                   if(((Empire) imperio_atual).build_farm()){
                                        System.out.println("Farm id: " + (banco.getFarm().getSize()-1));
                                   }
                                   else{
                                    System.out.println("Recursos insuficientes.");
                                   }
                                }
                                else if (option == 2){
                                    if(((Empire) imperio_atual).build_mine()){
                                        System.out.println("Mine id: " + (banco.getMine().getSize()-1));
                                   }
                                   else{
                                    System.out.println("Recursos insuficientes.");
                                   }
                                }
                                else if (option == 3){
                                    if(((Empire) imperio_atual).build_house()){
                                        System.out.println("Casa construida.");
                                   }
                                   else{
                                    System.out.println("Recursos insuficientes.");
                                   }
                                }
                                else if (option == 4){

                                   if(((Empire) imperio_atual).create_army()){
                                        System.out.println("Army id: " + (banco.getArmy().getSize()-1));
                                   }
                                   else{
                                    System.out.println("Recursos insuficientes.");
                                   }
                                }
                                else if (option == 0){
                                    break;
                                }
                        }
                        //ENVIAR
                        else if (option == 3){
                            while(true){
                            System.out.println("1-Farm\n2-Mine\n3-Lumber\n4-Army\n0-Voltar");

                            option = scanner.nextInt();

                            if (option == 0){
                                break;
                            }

                            System.out.println("Digite a quantidade a ser enviada:");
                            int amount = scanner.nextInt();


                            if (option == 1){
                                System.out.println("Digite o id:");
                                int id = scanner.nextInt();

                                modelo.Entidade a_farm = banco.getFarm().buscarId(id);

                                if (a_farm == null){
                                    System.out.println("Essa fazenda nao existe.");
                                }
                                else if (((modelo.Farm) a_farm).getEmpire_id() != imperio_atual.get_id()){
                                    System.out.println("Essa fazenda nao pertence a esse imperio.");
                                }
                                else{
                                    System.out.println(String.format("%d trabalhadores enviados pra Fazenda #%d.", ((Empire)imperio_atual).send_workers_to_farm(amount, id), id));
                                }                    
                            }
                                else if (option == 2){
                                    System.out.println("Digite o id:");
                                    int id = scanner.nextInt();

                                    modelo.Entidade a_mine = banco.getMine().buscarId(id);

                                    if (a_mine == null){
                                        System.out.println("Essa mina nao existe.");
                                    }
                                    else if (((modelo.Mine) a_mine).getEmpire_id() != imperio_atual.get_id()){
                                        System.out.println("Essa mina nao pertence a esse imperio.");
                                    }
                                    else{
                                        System.out.println(String.format("%d trabalhadores enviados pra mina #%d.", ((Empire)imperio_atual).send_workers_to_mine(amount, id), id));
                                    } 
                                }
                                else if (option == 3){
                                    System.out.println(String.format("%d trabalhadores enviados pra cortar madeira.", ((Empire)imperio_atual).send_workers_to_lumber_camp(amount)));
                                }
                                else if (option == 4){
                                    System.out.println("Digite o id:");
                                    int id = scanner.nextInt();

                                    modelo.Entidade a_army = banco.getArmy().buscarId(id);

                                    if (a_army == null){
                                        System.out.println("Essa tropa nao existe.");
                                    }
                                    else if (((modelo.Army) a_army).getEmpire_id() != imperio_atual.get_id()){
                                        System.out.println("Essa tropa nao pertence a esse imperio.");
                                    }
                                    else{
                                        amount = Math.min(amount, ((Empire)imperio_atual).get_population());
                                        if (((modelo.Army) a_army).allocate_work((Empire)imperio_atual, amount)){
                                            System.out.println(String.format("%d trabalhadores enviados pra tropa #%d.", amount, id));
                                        }
                                    } 
                                }
                            }
                        }
                        //RETIRAR
                        else if (option == 4){
                            while(true){
                            System.out.println("1-Farm\n2-Mine\n3-Lumber\n4-Army\n0-Voltar");

                            option = scanner.nextInt();

                            if (option == 0){
                                break;
                            }

                            System.out.println("Digite a quantidade a ser retirada:");
                            int amount = scanner.nextInt();


                            if (option == 1){
                                System.out.println("Digite o id:");
                                int id = scanner.nextInt();

                                modelo.Entidade a_farm = banco.getFarm().buscarId(id);

                                if (a_farm == null){
                                    System.out.println("Essa fazenda nao existe.");
                                }
                                else if (((modelo.Farm) a_farm).getEmpire_id() != imperio_atual.get_id()){
                                    System.out.println("Essa fazenda nao pertence a esse imperio.");
                                }
                                else{
                                    amount = Math.min(amount, ((modelo.Farm) a_farm).getWorkers());
                                    System.out.println(String.format("%d trabalhadores retirados da Fazenda #%d.", ((Empire)imperio_atual).take_workers_from_farm(amount, id), id));
                                }                    
                            }
                                else if (option == 2){
                                    System.out.println("Digite o id:");
                                    int id = scanner.nextInt();

                                    modelo.Entidade a_mine = banco.getMine().buscarId(id);

                                    if (a_mine == null){
                                        System.out.println("Essa mina nao existe.");
                                    }
                                    else if (((modelo.Mine) a_mine).getEmpire_id() != imperio_atual.get_id()){
                                        System.out.println("Essa mina nao pertence a esse imperio.");
                                    }
                                    else{
                                        amount = Math.min(amount, ((modelo.Mine) a_mine).getWorkers());
                                        System.out.println(String.format("%d trabalhadores retirados da mina #%d.", ((Empire)imperio_atual).take_workers_from_mine(amount, id), id));
                                    } 
                                }
                                else if (option == 3){
                                    int lumberW = ((Empire) imperio_atual).getLumber_camp().getWorkers();
                                    amount = Math.min(amount, lumberW);
                                    System.out.println(String.format("%d trabalhadores retirados de cortar madeira.", ((Empire)imperio_atual).take_workers_from_lumber_camp(amount)));
                                }
                                else if (option == 4){
                                    System.out.println("Digite o id:");
                                    int id = scanner.nextInt();

                                    modelo.Entidade a_army = banco.getArmy().buscarId(id);

                                    if (a_army == null){
                                        System.out.println("Essa tropa nao existe.");
                                    }
                                    else if (((modelo.Army) a_army).getEmpire_id() != imperio_atual.get_id()){
                                        System.out.println("Essa tropa nao pertence a esse imperio.");
                                    }
                                    else{
                                        amount = Math.min(amount, ((modelo.Army) a_army).getSoldiers_amt());
                                        System.out.println(String.format("%d trabalhadores retirados da mina #%d.", ((Empire)imperio_atual).take_workers_from_army(amount, id), id));
                                    } 
                                }
                                else if (option == 0){
                                    break;
                                }
                            }
                        }
                        //VISUALIZAR
                        else if (option == 5){
                            System.out.println("1-Imperio\n2-Fazendas\n3-Minas\n4-Lumber\n5-Army\n0-sair");
                            
                            option = scanner.nextInt();

                            if (option == 1){
                                System.out.println(imperio_atual);
                            }
                            else if (option == 2){
                                for (modelo.Entidade fazendas : banco.getFarm().getListaDeEntidades()){

                                    if (((modelo.Farm)fazendas).getEmpire_id() == imperio_atual.get_id())
                                        System.out.println(fazendas);
                                }
                            }
                            else if (option == 3){
                                for (modelo.Entidade minas : banco.getMine().getListaDeEntidades()){
                                    
                                    if (((modelo.Mine)minas).getEmpire_id() == imperio_atual.get_id())
                                        System.out.println(minas);
                                }
                            }
                            else if (option == 4){
                                for (modelo.Entidade lumber : banco.getLumberCamp().getListaDeEntidades()){
                                    
                                    if (((modelo.LumberCamp)lumber).getEmpire_id() == imperio_atual.get_id())
                                        System.out.println(lumber);
                                }
                            }
                            else if (option == 5){
                                for (modelo.Entidade tropas : banco.getArmy().getListaDeEntidades()){
                                    
                                    if (((modelo.Army)tropas).getEmpire_id() == imperio_atual.get_id())
                                        System.out.println(tropas);
                                }
                            }
                            else if (option == 0){
                                break;
                            }
                        }
                        //GUERRA
                        else if (option == 6){
                            menuWar((Empire)imperio_atual, banco, scanner);
                        }
                        //ARMY
                        else if (option == 7){
                            System.out.println("Digite o id da tropa:");
                            int id = scanner.nextInt();

                            modelo.Entidade tropa = banco.getArmy().buscarId(idm);

                            if (tropa != null && ((Army)tropa).getEmpire_id() == ((Empire)imperio_atual).get_id()){
                                menuExercitos((Empire)imperio_atual, banco, scanner, (Army)tropa);
                            }
                            else{
                                System.out.println("Tropa nao existe ou nao pertence a este imperio.");
                            }
                        }
                        else if (option == 0){
                            break;
                        }
                    }
                }
            }

            else if(option == 0){
                break;
            }
        }
    }

    public static void menuWar(Empire empire, BancoDeDados banco, Scanner scanner){
        while (true){
            System.out.println("1-Iniciar Guerra\n2-Planejar guerra\n0-Voltar");
            int option = scanner.nextInt();

            if (option == 1){
                empire.start_war();
                System.out.println("ID: " + (banco.getWar().getSize()-1));
            }
            else if (option == 2){
                System.out.println("Digite o id da guerra:");
                int idm = scanner.nextInt();

                modelo.Entidade guerra_atual = banco.getWar().buscarId(idm);

                if (guerra_atual == null){
                    System.out.println("Essa guerra nao existe");
                }
                else{
                    menuBatalhas(empire, ((modelo.War)guerra_atual), banco, scanner);
                }
            }
            else if (option == 0){
                break;
            }
        }
    }

    public static void menuBatalhas(Empire empire, modelo.War guerra, BancoDeDados banco, Scanner scanner){
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
                    guerra.criaBatalha(attackerArmy, defenderArmy);
                }
            }      
            }
            else if (option == 2){
                if (guerra.getCurrent_battles().isEmpty()){
                    System.out.println("Sem batalhas");
                }
                else{
                    for (modelo.War.Battle batalhas : guerra.getCurrent_battles()){
                        int result = batalhas.simulate_round();
                        String attackerName = "Army #" + batalhas.getAttacker().get_id();
                        String defenderName = "Army #" + batalhas.getDefender().get_id();

                        System.out.println("\nBatalha: " + attackerName + " (Atacante) vs " + defenderName + " (Defensor)");
                        System.out.println("Soldados Atacantes vivos: " + batalhas.getAttacker_soldiers_alive());
                        System.out.println("Soldados Defensores vivos: " + batalhas.getDefender_soldiers_alive());
                    
                        if (result == 1) {
                            System.out.println(attackerName + " Venceu a batalha! Vitoria dos atacantes.");
                            guerra.getCurrent_battles().remove(batalhas);
                        } else if (result == -1) {
                            System.out.println(defenderName + " Venceu a batalha! Vitoria dos defensores.");
                            guerra.getCurrent_battles().remove(batalhas);
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

    public static void menuExercitos(Empire empire, BancoDeDados banco, Scanner scanner, Army army){
        while (true){
            System.out.println("\n--- Gerenciamento do Exercito ---");
            System.out.println(army);

            System.out.println("1-Contratar Soldados\n2-Melhorar Armadura\n3-Reabastecer Comida\n0-Voltar");
            int option = scanner.nextInt();

            if (option == 1) {
                System.out.println("Quantos trabalhadores voce quer alocar como soldados? (População atual: " + empire.get_population() + ")");
                int amount = scanner.nextInt();

                if (army.allocate_work(empire, amount)) {
                    System.out.println(String.format("%d trabalhadores se tornaram soldados.", amount));
                } else {
                    System.out.println("Populacao insuficiente.");
                }
            } 
            else if (option == 2){
                int costIron = army.IRON_COST_ARMORY;
                int costGold = army.GOLD_COST_ARMORY;

                System.out.println(String.format("Custo por nivel: %d Ferro e %d Ouro.", costIron, costGold));
                System.out.println("Quantos niveis voce deseja melhorar?");
                int pontos = scanner.nextInt();
                
                pontos = army.upgrade_armory(pontos, empire);

                if (pontos > 0) {
                    System.out.println(String.format("Armadura melhorada em %d ponto(s).", pontos));
                } else {
                    System.out.println("Recursos (Ferro/Ouro) insuficientes para esta melhoria.");
                }
            }
            else if (option == 3){
                System.out.println("Quanta comida voce quer transferir para o exercito?");
                int food_supply = scanner.nextInt();

                if (food_supply > empire.getFood() || food_supply < 0) {
                    System.out.println("Quantidade invalida.");
                    continue;
                }

                int food_spent = army.supply_food(food_supply);
                empire.setFood(empire.getFood() - food_spent);

                System.out.println(String.format("%d de comida transferida.", food_spent));
            }
            else if (option == 0){
                break;
            }
        }
    }
}
