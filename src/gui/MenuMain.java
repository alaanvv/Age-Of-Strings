package Interface;

import java.awt.Color;
import javax.swing.*;
import modelo.Empire;
import modelo.Entidade;
import persistencia.BancoDeDados;

public class MenuMain extends MenuBase {

    private final MenuManager manager;
    private final Buttons buttons;


    // -----------------------------------------------------------------------------------
    // ----------------------- Construtor do menu principal ------------------------------
    // -----------------------------------------------------------------------------------
    public MenuMain(BancoDeDados db, MenuManager manager) {
        super(db);
        this.manager = manager;
        this.buttons = manager.getButtons();
    }


    // -----------------------------------------------------------------------------------
    // --------------------------- Metodo Render da mae ----------------------------------
    // -----------------------------------------------------------------------------------
    @Override
    public void render() {

        topPanel.removeAll();
        centerPanel.removeAll();

        // ################
        // BOTÃO NEW EMPIRE
        // ################
        JButton newEmpireButton = new JButton("New Empire");
        newEmpireButton.setBackground(btnColor);
        newEmpireButton.setForeground(Color.WHITE);
        newEmpireButton.addActionListener(e -> {
            buttons.createEmpire(db);
            manager.switchMenu(new MenuMain(db, manager));
        });
        topPanel.add(newEmpireButton);

        if (db.sizeEmpires() >= 1) {

            // ####################
            // BOTAO DESTROY EMPIRE
            // ####################
            JButton destroyEmpireButton = new JButton("Destroy Empire");

                Color destroyColor = buttons.isDestroyMode()
                    ? new Color(150, 50, 50)
                    : BUTTON_COLOR;

            destroyEmpireButton.setBackground(destroyColor);
            destroyEmpireButton.setForeground(Color.WHITE);

            destroyEmpireButton.addActionListener(e -> {
                buttons.toggleDestroyMode();
                manager.switchMenu(new MenuMain(db, manager));
            });

            topPanel.add(destroyEmpireButton);

            // ##############
            // BOTAO RUN TURN
            // ##############
            JButton runTurnButton = new JButton("Run Turn");
            runTurnButton.setBackground(btnColor);
            runTurnButton.setForeground(Color.WHITE);
            runTurnButton.addActionListener(e -> {
                buttons.runTurn(db);
                manager.switchMenu(new MenuMain(db, manager));
            });
            topPanel.add(runTurnButton);
        }
        // ###########
        // BOTÃO EXIT
        // ###########
        JButton btnExit = new JButton("Exit");
        btnExit.setBackground(btnColor);
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                null,
                "Do you really want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (op == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        topPanel.add(btnExit);

        // ##########################
        // LISTA DE IMPÉRIOS COM AÇAO
        // ##########################
        for (Entidade e : db.getEmpires().getEntidades().values()) {
            if (e instanceof Empire empire) {

                JButton empireButton = new JButton(empire.getName());
                empireButton.setBackground(btnColor);
                empireButton.setForeground(Color.WHITE);

                if (buttons.isDestroyMode()) {
                    // ------------------- MODO DESTRUIR --------------------
                    empireButton.addActionListener(ae -> {

                        int op = JOptionPane.showConfirmDialog(
                                null,
                                "Destroy empire " + empire.getName() + "?",
                                "Confirm",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (op == JOptionPane.YES_OPTION) {
                        db.destroyEmpire(empire);
                        buttons.toggleDestroyMode();
                            manager.switchMenu(new MenuMain(db, manager));
                        }
                    });

                } else {
                    // ------------------- MODO NORMAL -----------------------
                    empireButton.addActionListener(ae ->
                            manager.switchMenu(new MenuEmpire(db, manager, empire))
                    );
                }

                centerPanel.add(empireButton);
            }
        }

        centerPanel.revalidate();
        centerPanel.repaint();
        topPanel.revalidate();
        topPanel.repaint();
    }
}
