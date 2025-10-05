package modelo;




public abstract class Entidade {
   private int id;

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
