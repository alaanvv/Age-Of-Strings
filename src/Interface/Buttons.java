package Interface;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import persistencia.BancoDeDados;

public class Buttons {
    
    private boolean isDestroyMode = false;

    public boolean getIsDestroyMode() {
        return isDestroyMode;
    }

    // ----- Logica do botão New Empire -----
    public void createNewEmpireButton(BancoDeDados db) {
        try {

            // verifica nome do imperio
            String s1 = javax.swing.JOptionPane.showInputDialog("Empire Name:");
            if (s1 == null) return;
            s1 = s1.trim();
            if (s1.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Empire name cannot be empty!");
                return;
            }

            //cria imperio
            db.createEmpire(s1);

            System.out.println("Empire created: " + s1 + " #" + db.sizeEmpires());
          

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Invalid input!");
        }
    }

    public void switchDestroyMode() {
        isDestroyMode = !isDestroyMode;
    }
    
}
