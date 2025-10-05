package persistency;

import java.util.ArrayList;
import java.util.List;
import modelo.Entidade;

public class Persistente {

   private List<modelo.Entidade> listaDeEntidades;

   public Persistente(){
      this.listaDeEntidades = new ArrayList<>();
   }

   public void inserir(modelo.Entidade objeto){
      this.listaDeEntidades.add(objeto);
   }

   public Boolean alterar(int id, modelo.Entidade objeto){
      for (int i = 0; i < listaDeEntidades.size(); i++){
         Entidade entidade_atual = listaDeEntidades.get(i);
         if (entidade_atual.get_id() == id){
            listaDeEntidades.set(i, objeto);
            return true;
         }
      }
      return false;
   }

   public Boolean remover(int id){
      for (Entidade entidade : listaDeEntidades){
         if (entidade.get_id() == id){
            listaDeEntidades.remove(entidade);
            return true;
         }
      }
      return false;
   }

   public modelo.Entidade buscarId(int id){
      for (Entidade entidade : listaDeEntidades){
         if (entidade.get_id() == id){
            return entidade;
         }
      }
      return null;
   }

   public int getSize(){
      return listaDeEntidades.size();
   }

   public List<modelo.Entidade> getListaDeEntidades() {
       return listaDeEntidades;
   }

   @Override
   public String toString(){
      StringBuilder s = new StringBuilder();
      for (Entidade entidade : listaDeEntidades){
         s.append(entidade.toString()).append("\n");
      }
      return s.toString();
   }
}
