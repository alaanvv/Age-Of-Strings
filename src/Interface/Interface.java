package Interface;


import javax.swing.JFrame;

import persistencia.BancoDeDados;


public class Interface extends JFrame {

    private Menus menu = new Menus();
    private BancoDeDados db;


    //tamanho da janela
    public static final int WIDTH = 1920, HEIGHT = 1080;

    // ----- Construtor -----
    public Interface(BancoDeDados db) {
        super("Age Of Strings");
        this.db = db;

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setVisible(true);

        menu.MainMenu(db, this);
    }
}
