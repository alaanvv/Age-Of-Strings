package Interface;

import persistencia.BancoDeDados;
import javax.swing.*;

import modelo.Empire;
import modelo.Entidade;

import java.awt.*;

public class Menus {

    private static final Color BACKGROUND_COLOR = new Color(25, 30, 35);
    private static final Color BUTTON_DEFAULT_COLOR = new Color(50, 50, 50);
    private static final Color BUTTON_DESTROY_COLOR = new Color(150, 50, 50);

    private final JPanel centerPanel;
    private final JPanel topPanel;
    private final Buttons buttons = new Buttons();

    public Menus() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BACKGROUND_COLOR);

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 4, 10, 10));
        centerPanel.setBackground(BACKGROUND_COLOR);
    }

    private void displayEmpires(BancoDeDados database) {
        centerPanel.removeAll();

        if (database.hasEmpire()) {
            for (Entidade entity : database.getEmpires().getEntidades().values()) {
                if (entity instanceof Empire empire) {
                    String empireName = empire.getName();
                    JButton empireButton = new JButton(empireName);

                    empireButton.addActionListener(actionEvent -> {
                        if (buttons.isDestroyMode()) {
                            database.destroyEmpire(empire);
                            buttons.toggleDestroyMode();
                            displayEmpires(database);
                            updateMainMenu(database);
                            System.out.println("Destroyed empire: " + empireName);
                        } else {
                            System.out.println("Open empire menu: " + empireName);
                        }
                    });

                    empireButton.setBackground(BUTTON_DEFAULT_COLOR);
                    empireButton.setForeground(Color.WHITE);

                    centerPanel.add(empireButton);
                }
            }
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void updateMainMenu(BancoDeDados database) {
        topPanel.removeAll();

        JButton newEmpireButton = new JButton("New Empire");
        newEmpireButton.setBackground(BUTTON_DEFAULT_COLOR);
        newEmpireButton.setForeground(Color.WHITE);
        newEmpireButton.addActionListener(actionEvent -> {
            buttons.createEmpire(database);
            displayEmpires(database);
            updateMainMenu(database);
        });
        topPanel.add(newEmpireButton);

        if (database.sizeEmpires() >= 1) {
            JButton destroyEmpireButton = new JButton("Destroy Empire");
            Color destroyButtonColor = buttons.isDestroyMode() ? BUTTON_DESTROY_COLOR : BUTTON_DEFAULT_COLOR;
            destroyEmpireButton.setBackground(destroyButtonColor);
            destroyEmpireButton.setForeground(Color.WHITE);
            destroyEmpireButton.addActionListener(actionEvent -> {
                buttons.toggleDestroyMode();
                displayEmpires(database);
                updateMainMenu(database);
            });
            topPanel.add(destroyEmpireButton);

            JButton runTurnButton = new JButton("Run Turn");
            runTurnButton.setBackground(BUTTON_DEFAULT_COLOR);
            runTurnButton.setForeground(Color.WHITE);
            runTurnButton.addActionListener(actionEvent -> {
                buttons.runTurn(database);
                updateMainMenu(database);
            });
            topPanel.add(runTurnButton);
        }

        topPanel.revalidate();
        topPanel.repaint();
    }

    public void renderMainMenu(BancoDeDados database, JFrame frame) {
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(centerPanel), BorderLayout.CENTER);

        updateMainMenu(database);
        displayEmpires(database);
    }

    public void MainMenu(BancoDeDados database, JFrame frame) {
        renderMainMenu(database, frame);
    }
}

