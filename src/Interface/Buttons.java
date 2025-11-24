package Interface;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import modelo.Empire;
import modelo.Entidade;
import persistencia.BancoDeDados;

public class Buttons {
    
    // ----- destroy mode -----
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
          

        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Invalid input!");
        }
    }

    // -----------------------------------------------------------------------------------
    // ---------------------------- logica btn runTurn -----------------------------------
    // -----------------------------------------------------------------------------------
    public void btnRunTurn(BancoDeDados db){


        for (Entidade e : db.getEmpires().getEntidades().values()) {
            if (e instanceof Empire empire) {
                String res = empire.runTurn();
                System.out.println(res);
            }
        }
        System.out.println(" ------- Turn completed. ------- \n");
        JOptionPane.showMessageDialog(null, "Turn completed.");
    }

    // -----------------------------------------------------------------------------------
    // --------------------------- logica btn buildHouse----------------------------------
    // -----------------------------------------------------------------------------------
    public void btnBuildHouse(Empire empire) {
        int op = JOptionPane.showConfirmDialog(
                    null,
                    "It costs: 5 woods and 5 golds \n (increases population by 3)",
                    "Build Houses ?",
                    JOptionPane.YES_NO_OPTION
                );

        if (op == JOptionPane.YES_OPTION) {
            boolean built = empire.buildHouse();
            if (!built) {
                JOptionPane.showMessageDialog(null, "Insufficient resources (Wood: 5, Gold: 5).");
            }
        }
 
    }


    // -----------------------------------------------------------------------------------
    // ----------------------------- logica btn Lumber -----------------------------------
    // -----------------------------------------------------------------------------------
    public void btnLumber(Empire empire) {
    JTextField field = new JTextField();

    Object[] message = {
        "Amount of workers:", field
    };

    int option = JOptionPane.showOptionDialog(
            null,
            message,
            "Assign workers to Lumber",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            new String[]{"Send", "Take", "Cancel"},
            "Send"
    );

    if (option == 0) { // Send
        try {
            int amount = Integer.parseInt(field.getText());
            int sent = empire.sendWorkersToLumber(amount);
            JOptionPane.showMessageDialog(null, sent + " workers sent to Lumber.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number!");
        }
    } else if (option == 1) { // Take
        try {
            int amount = Integer.parseInt(field.getText());
            int taken = empire.takeWorkersFromLumber(amount);
            JOptionPane.showMessageDialog(null, taken + " workers taken from Lumber.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number!");
        }
    }
}

    
}
