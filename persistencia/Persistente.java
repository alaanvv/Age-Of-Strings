package persistencia;

import java.util.HashSet;
import java.util.Set;

public class Persistente {
   
   private static int count = 0;

   public static int generate_id(){
      return count++;
   }
   
}
