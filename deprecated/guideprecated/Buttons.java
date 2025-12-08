package guideprecated;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import modelo.Empire;
import modelo.Entidade;
import persistencia.BancoDeDados;

/**
 * Centralizes Swing dialog flows for user-triggered actions.
 * Acts as a lightweight controller shared across menus so listeners reuse
 * the same business logic (create empire, run turn, adjust workers, etc.).
 */
public class Buttons {

    private boolean destroyMode = false;

    public boolean isDestroyMode() {
        return destroyMode;
    }

    public void toggleDestroyMode() {
        destroyMode = !destroyMode;
    }

    public void createEmpire(BancoDeDados database) {
        try {
            String empireName = JOptionPane.showInputDialog("Empire Name:");
            if (empireName == null) return;

            empireName = empireName.trim();
            if (empireName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Empire name cannot be empty!");
                return;
            }

            database.createEmpire(empireName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input!");
        }
    }

    public void runTurn(BancoDeDados database) {
        for (Entidade entity : database.getEmpires().getEntidades().values()) {
            if (entity instanceof Empire empire) {
                String result = empire.runTurn();
                System.out.println(result);
            }
        }
        System.out.println(" ------- Turn completed. ------- \n");
        JOptionPane.showMessageDialog(null, "Turn completed.");
    }

    public void buildHouse(Empire empire) {
        int selection = JOptionPane.showConfirmDialog(
                null,
                "It costs: 5 woods and 5 golds \n (increases population by 3)",
                "Build Houses ?",
                JOptionPane.YES_NO_OPTION
        );

        if (selection == JOptionPane.YES_OPTION) {
            boolean built = empire.buildHouse();
            if (!built) {
                JOptionPane.showMessageDialog(null, "Insufficient resources (Wood: 5, Gold: 5).");
            }
        }
    }

    public void adjustLumberWorkers(Empire empire) {
        JTextField workerField = new JTextField();

        Object[] dialogMessage = {
                "Amount of workers:", workerField
        };

        int option = JOptionPane.showOptionDialog(
                null,
                dialogMessage,
                "Assign workers to Lumber",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Send", "Take", "Cancel"},
                "Send"
        );

        if (option == 0) {
            try {
                int amount = Integer.parseInt(workerField.getText());
                int sent = empire.sendWorkersToLumber(amount);
                JOptionPane.showMessageDialog(null, sent + " workers sent to Lumber.");
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Invalid number!");
            }
        } else if (option == 1) {
            try {
                int amount = Integer.parseInt(workerField.getText());
                int taken = empire.takeWorkersFromLumber(amount);
                JOptionPane.showMessageDialog(null, taken + " workers taken from Lumber.");
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Invalid number!");
            }
        }
    }
}
