package Interface;

import persistencia.BancoDeDados;
import javax.swing.*;

import modelo.Empire;
import modelo.Entidade;

import java.awt.*;

public class Menus {

    // Painel para os botões
    private JPanel centrPanel; 
    private JPanel topPanel;

    // cores
    Color bgColor = new Color(25,30,35);
    Color btnColor = new Color(50, 50, 50);

    private Buttons buttons = new Buttons();

    
    // -----------------------------------------------------------------------------------
    // -------------------------------- Construtor ---------------------------------------
    // -----------------------------------------------------------------------------------
    public Menus() {
        // Painel superior
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bgColor); 

        // painel central com FlowLayout
        centrPanel = new JPanel();
        centrPanel.setLayout(new GridLayout(0, 4, 10, 10));
        centrPanel.setBackground(bgColor); 
    }

    // -----------------------------------------------------------------------------------
    // --------------------------------- Display Empires ---------------------------------
    // -----------------------------------------------------------------------------------
    private void displayEmpires(BancoDeDados db) {
        centrPanel.removeAll();
        
        if (db.hasEmpire()) {
            for (Entidade e : db.getEmpires().getEntidades().values()) {
                if (e instanceof Empire empire) {
                    String empireName = empire.getName();

                    JButton btnEmpire = new JButton(empireName);

                    btnEmpire.addActionListener(ae -> {

                        if (buttons.getIsDestroyMode()) {
                            db.destroyEmpire(empire);
                            buttons.switchDestroyMode();
                            displayEmpires(db);
                            updateMainMenu(db);
                            System.out.println("Destroyed empire: " + empireName);
                        } else {
                            System.out.println("Open empire menu: " + empireName);
                        }
                    });

                    btnEmpire.setBackground(btnColor);
                    btnEmpire.setForeground(Color.WHITE);

                    centrPanel.add(btnEmpire);
                }
            }
        }

    centrPanel.revalidate();
    centrPanel.repaint();
    
}
    // -----------------------------------------------------------------------------------
    // ------------------------------- Atualiza o mainMenu -------------------------------
    // -----------------------------------------------------------------------------------
    private void updateMainMenu(BancoDeDados db) {
        topPanel.removeAll();

        // --- Botão New Empire --- 
        JButton btnNew = new JButton("New Empire");
        btnNew.setBackground(btnColor);
        btnNew.setForeground(Color.WHITE);
        btnNew.addActionListener(e -> {
            buttons.createNewEmpireButton(db);
            displayEmpires(db);
            updateMainMenu(db);
        });
        topPanel.add(btnNew);

        if (db.sizeEmpires() >= 1) {
            // --- Botão Destroy Empire --- 
            JButton btnDestroy = new JButton("Destroy Empire");
            if (buttons.getIsDestroyMode()) {
                btnColor = new Color(150, 50, 50);
            } else {
                btnColor = new Color(50, 50, 50);
            }
            btnDestroy.setBackground(btnColor);
            btnDestroy.setForeground(Color.WHITE);
            btnDestroy.addActionListener(e -> {
                buttons.switchDestroyMode();
                displayEmpires(db);
                updateMainMenu(db);
            });
            topPanel.add(btnDestroy);
            btnColor = new Color(50, 50, 50);

            // --- Botao RunTurn ---
            JButton btnRun = new JButton("Run Turn");
            btnRun.setBackground(btnColor);
            btnRun.setForeground(Color.WHITE);
            btnRun.addActionListener(e -> {
                buttons.btnRunTurn(db);
                updateMainMenu(db);
            });
            topPanel.add(btnRun);

        }

        topPanel.revalidate();
        topPanel.repaint();
    }
    
    // -----------------------------------------------------------------------------------
    // -------------------------------- Main Menu ----------------------------------------
    // -----------------------------------------------------------------------------------
    public void MainMenu(BancoDeDados db, JFrame frame) {

        // Layout do frame
        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(centrPanel), BorderLayout.CENTER);

        // Inicializa os paineis conforme estado atual do DB
        updateMainMenu(db);
        displayEmpires(db);
    }
}

