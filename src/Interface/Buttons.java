package Interface;

import javax.swing.JOptionPane;

import modelo.Empire;
import modelo.Entidade;
import persistencia.BancoDeDados;

public class Buttons {
    
    //destroy mode
    private boolean isDestroyMode = false;
    public boolean getIsDestroyMode() { return isDestroyMode; }
    public void switchDestroyMode() { isDestroyMode = !isDestroyMode; }

    // -----------------------------------------------------------------------------------
    // --------------------------- logica btn newEmpire ----------------------------------
    // -----------------------------------------------------------------------------------
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

    // -----------------------------------------------------------------------------------
    // --------------------------- logica btn runTurn ----------------------------------
    // -----------------------------------------------------------------------------------
    public void btnRunTurn(BancoDeDados db){


        for (Entidade e : db.getEmpires().getEntidades().values()) {
            if (e instanceof Empire) {
                String res = ((Empire) e).runTurn();
                System.out.println(res);
            }
        }
        System.out.println(" ------- Turn completed. ------- \n");
        JOptionPane.showMessageDialog(null, "Turn completed.");
    }

    
}
