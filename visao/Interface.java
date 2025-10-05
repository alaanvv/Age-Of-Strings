package visao;

import java.util.Scanner;

public class Interface {
  Scanner scanner;
  persistency.BancoDeDados banco;
  modelo.Empire empire;

  /*
  Coloquei um atributo empire porque ainda não tenho certeza de como iniciar o imperio;
  Não sei se deixo do jeito que estava ou se crio um novo menu onde seja possível criar imperios e escolher
  um desses imperios para controlar.
  */

  public Interface(persistency.BancoDeDados banco){
    this.banco = banco;
    this.scanner = new Scanner(System.in);
    this.empire = new modelo.Empire(persistency.Persistente.generate_id());
  }

  public void opcao(){
    while(true){
      exibirMenuPrincipal();

      System.out.print("\n:");
      String line = scanner.nextLine();

      System.out.println();
      String[] parts = line.split(" ");

      switch (parts[0]) {
        case "price":
          if      (parts[1].equals("farm"))  System.out.println("Madeira: 5 \nOuro: 3");
          else if (parts[1].equals("mine"))  System.out.println("Madeira: 15\nOuro: 5");
          else if (parts[1].equals("house")) System.out.println("Madeira: 5 \nOuro: 5");
          break;

        case "build":
          if      (parts[1].equals("farm"))  System.out.println(empire.build_farm() ?  "Fazenda construída" : "Recursos insuficientes.\nUse `price farm`");
          else if (parts[1].equals("mine"))  System.out.println(empire.build_mine() ?  "Mina construída"    : "Recursos insuficientes.\nUse `price mine`");
          else if (parts[1].equals("house")) System.out.println(empire.build_house() ? "Casa construída"    : "Recursos insuficientes.\nUse `price mine`");
          break;

        case "send":
          int amount = Integer.parseInt(parts[2]);
          int id = Integer.parseInt(parts[3]);
          if (empire.get_population() < amount) {
            System.out.println("População insuficiente.");
            continue;
          }

          if      (parts[1].equals("lumber")) System.out.println(String.format("%d trabalhadores enviados pra cortar madeira.", empire.send_workers_to_lumber_camp(amount)));
          else if (parts[1].equals("farm")) {
            if (id >= empire.get_farm_count()) {
              System.out.println("Fazenda inexistente.");
              continue;
            }
            System.out.println(String.format("%d trabalhadores enviados pra Fazenda #%d.", empire.send_workers_to_farm(amount, id), id));
          }
          else if (parts[1].equals("mine")) {
            if (id >= empire.get_mine_count()) {
              System.out.println("Mina inexistente.");
              continue;
            }
            System.out.println(String.format("%d trabalhadores enviados pra Mina #%d.", empire.send_workers_to_mine(amount, id), id));
          }
          break;

        case "take":
          amount = Integer.parseInt(parts[2]);
          id = Integer.parseInt(parts[3]);

          if      (parts[1].equals("lumber")) System.out.println(String.format("%d trabalhadores retirados de cortar madeira.", empire.take_workers_from_lumber_camp(amount)));
          else if (parts[1].equals("farm"))   {
            if (id >= empire.get_farm_count()) {
              System.out.println("Fazenda inexistente.");
              continue;
            }
            System.out.println(String.format("%d trabalhadores retirados da Fazenda #%d.", empire.take_workers_from_farm(amount, id)));
          }
          else if (parts[1].equals("mine"))   {
            if (id >= empire.get_mine_count()) {
              System.out.println("Mina inexistente.");
              continue;
            }
            System.out.println(String.format("%d trabalhadores retirados da Mina #%d.", empire.take_workers_from_mine(amount, id)));
          }
          break;

        case "view":
          if      (parts[1].equals("empire")) System.out.println(empire);
          else if (parts[1].equals("lumber")) empire.view_lumber_camp();
          else if (parts[1].equals("farms"))  empire.view_farms();
          else if (parts[1].equals("mines"))  empire.view_mines();
          break;

        case "exit":
          scanner.close();
          return;
      }
    }
  }

  private static void exibirMenuPrincipal() {
    System.out.println("Bem vindo ao Age Of Strings!");
    System.out.println("price [farm|mine|house]             # Ver o custo de construção");
    System.out.println("build [farm|mine|house]             # Faz a construção");
    System.out.println("send [lumber|farm|mine] <qtd> <id>  # Enviar trabalhadores");
    System.out.println("take [lumber|farm|mine] <qtd> <id>  # Tirar trabalhadores");
    System.out.println("view [lumber|farms|mines|empire]    # Ver informações");
    System.out.println("exit                                # Sair do jogo");
  }
}