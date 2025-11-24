package Interface;

import java.awt.Color;
import javax.swing.*;
import modelo.Empire;
import modelo.Entidade;
import persistencia.BancoDeDados;

public class MenuMain extends MenuBase {

    private final BancoDeDados db;
    private final MenuManager manager;
    private Buttons buttons;


    // -----------------------------------------------------------------------------------
    // ----------------------- Construtor do menu principal ------------------------------
    // -----------------------------------------------------------------------------------
    public MenuMain(BancoDeDados db, MenuManager manager) {
        this.db = db;
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
        JButton btnNew = new JButton("New Empire");
        btnNew.setBackground(btnColor);
        btnNew.setForeground(Color.WHITE);
        btnNew.addActionListener(e -> {
            buttons.createNewEmpireButton(db);
            manager.switchMenu(new MenuMain(db, manager)); // recarregar
        });
        topPanel.add(btnNew);

        if (db.sizeEmpires() >= 1) {

            // ####################
            // BOTAO DESTROY EMPIRE
            // ####################
            JButton btnDestroy = new JButton("Destroy Empire");

            Color destroyColor = buttons.getIsDestroyMode()
                    ? new Color(150, 50, 50)     // modo ON
                    : new Color(50, 50, 50);     // modo OFF

            btnDestroy.setBackground(destroyColor);
            btnDestroy.setForeground(Color.WHITE);

            btnDestroy.addActionListener(e -> {
                buttons.switchDestroyMode();
                manager.switchMenu(new MenuMain(db, manager)); // recarregar
            });

            topPanel.add(btnDestroy);

            // ##############
            // BOTAO RUN TURN
            // ##############
            JButton btnRun = new JButton("Run Turn");
            btnRun.setBackground(btnColor);
            btnRun.setForeground(Color.WHITE);
            btnRun.addActionListener(e -> {
                buttons.btnRunTurn(db);
                manager.switchMenu(new MenuMain(db, manager));
            });
            topPanel.add(btnRun);
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

                JButton btnEmpire = new JButton(empire.getName());
                btnEmpire.setBackground(btnColor);
                btnEmpire.setForeground(Color.WHITE);

                if (buttons.getIsDestroyMode()) {
                    // ------------------- MODO DESTRUIR --------------------
                    btnEmpire.addActionListener(ae -> {

                        int op = JOptionPane.showConfirmDialog(
                                null,
                                "Destroy empire " + empire.getName() + "?",
                                "Confirm",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (op == JOptionPane.YES_OPTION) {
                            db.destroyEmpire(empire); // remove por ID
                            buttons.switchDestroyMode();      // volta ao normal
                            manager.switchMenu(new MenuMain(db, manager));
                        }
                    });

                } else {
                    // ------------------- MODO NORMAL -----------------------
                    btnEmpire.addActionListener(ae ->
                            manager.switchMenu(new MenuEmpire(db, manager, empire))
                    );
                }

                centerPanel.add(btnEmpire);
            }
        }

        centerPanel.revalidate();
        centerPanel.repaint();
        topPanel.revalidate();
        topPanel.repaint();
    }
}
