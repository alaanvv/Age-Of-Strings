package Interface;

import javax.swing.*;
import persistencia.BancoDeDados;

public class Interface extends JFrame {

    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 900;

    private final MenuManager menuManager;

    // ----------------------------------------------------------------------------
    // ----------------------------- construtor -----------------------------------
    // ----------------------------------------------------------------------------
    public Interface(BancoDeDados db) {
        super("Age of Strings");

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuManager = new MenuManager(this);

        // Abre o menu inicial
        menuManager.switchMenu(new MenuMain(db, menuManager));

        setVisible(true);
    }
}
