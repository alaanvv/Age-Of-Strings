package gui;

import javax.swing.*;
import persistencia.BancoDeDados;

/**
 * Swing bootstrap window for the game.
 * Configures the frame shell (size, title, layout) and delegates all screen
 * composition to {@link MenuManager}, starting with the main menu.
 */
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
