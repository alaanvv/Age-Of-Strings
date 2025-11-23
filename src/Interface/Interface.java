package Interface;

import javax.swing.*;
import persistencia.BancoDeDados;

public class Interface extends JFrame {

    private MenuManager menuManager;
    private BancoDeDados db;

    public Interface(BancoDeDados db) {
        super("Age of Strings");
        this.db = db;

        setSize(1920, 1080);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuManager = new MenuManager(this);

        // Abre o menu inicial
        menuManager.switchMenu(new MenuMain(db, menuManager));

        setVisible(true);
    }
}
