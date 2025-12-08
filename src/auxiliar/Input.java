package auxiliar;

import javax.swing.*;
import java.awt.*;

public abstract class Input {
   
   public static int getIntDialogue(Component component, String message){
      while(true){
         try{
            String idInput = JOptionPane.showInputDialog(component, message);

            idInput = idInput.trim();
            int result = Integer.parseInt(idInput);
            
            return result; 
         } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(component, "Insira um valor válido!");
         }
      }
   }
}
