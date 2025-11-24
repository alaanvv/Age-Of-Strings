package Interface;

import javax.swing.*;
import persistencia.BancoDeDados;

public class Interface extends JFrame {

    private MenuManager menuManager;
    private BancoDeDados db;


    //tamanho da janela (padrão: 1920x1080)
    private int WIDTH = 1200, HEIGHT = 900;

    public Interface(BancoDeDados db) {
        super("Age of Strings");
        this.db = db;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuManager = new MenuManager(this);

        // Abre o menu inicial
        menuManager.switchMenu(new MenuMain(db, menuManager));

        setVisible(true);
    }
}
