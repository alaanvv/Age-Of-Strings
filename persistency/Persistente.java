package persistency;

public class Persistente {
   
   private static int count = 0;

   public static int generate_id(){
      return count++;
   }
   
}
