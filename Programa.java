import persistency.BancoDeDados;
import visao.Interface;

public class Programa {
    public static void main(String[] args) {
      
      persistency.BancoDeDados banco = new BancoDeDados();
      visao.Interface in = new Interface(banco);

      in.opcao();
    } 
}

