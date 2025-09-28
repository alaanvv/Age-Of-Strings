package modelo;
import java.util.HashSet;
import java.util.Set;



public abstract class Entidade {
   private int id;
   
   static int max_id = 0;


   

   public Entidade(){
      this.id = -1;
   }

   public Entidade(int id){
      this.id = id;
   }

   public int get_id(){
      return id;
   }

   public void set_id(int id){
      this.id = id;
   }

   @Override
   public String toString(){
      return "[id=" + id + "]";
   }
}
