import java.util.Scanner;
import modelo.Army;
import modelo.Empire;
import persistency.BancoDeDados;

public class Programa {
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

                        System.out.println("1-Construir casa\n2-Farm\n3-Mine\n4-Lumber Camp\n5-Army\n6-Batalha\n0-Voltar");
                        option = scanner.nextInt();

                        //CONSTRUIR CASA
                        if (option == 1){
                            while (true){
                                System.out.println("PRECO | Madeira: 5 | Ouro: 5 |");
                                System.out.println("1-Construir casa\n0-Voltar");
                                option = scanner.nextInt();

                                if (option == 1){
                                    if(((Empire) imperio_atual).build_house()){
                                        System.out.println("Casa construida.");
                                    }
                                    else{
                                        System.out.println("Recursos insuficientes.");
                                    }
                                }
                                else if (option == 0){
                                    break;
                                }
                            }
                        }
                        //FARM
                        else if (option == 2){
                            visao.Terminal.menuFarm(((Empire)imperio_atual), banco, scanner);
                        }
                        //MINE
                        else if (option == 3){
                            visao.Terminal.menuMine(((Empire)imperio_atual), banco, scanner);
                        }
                        //LUMBER
                        else if (option == 4){
                            visao.Terminal.menuLumber(((Empire)imperio_atual), banco, scanner);
                        }
                        //ARMY
                        else if (option == 5){
                            visao.Terminal.menuArmy((Empire)imperio_atual, banco, scanner);
                        }
                        //BATALHA
                        else if (option == 6){
                            visao.Terminal.menuBatalhas((Empire)imperio_atual, banco, scanner);
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
}
