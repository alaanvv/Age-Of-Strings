package persistencia;

public class InexistentIdException extends Exception{
   public InexistentIdException(){
      super("Id inexiste.");
   }

   public InexistentIdException(String message){
      super(message);
   }
}
