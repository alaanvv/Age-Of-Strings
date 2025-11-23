package Interface;

import modelo.Empire;
import persistencia.BancoDeDados;
import java.awt.Color;

import javax.swing.*;

public class MenuEmpire extends MenuBase {

    private BancoDeDados db;
    private MenuManager manager;
    private Empire empire;

    private Buttons buttons;


    public MenuEmpire(BancoDeDados db, MenuManager manager, Empire empire) {
        this.db = db;
        this.manager = manager;
        this.empire = empire;
    }

    @Override
    public void render() {

        topPanel.removeAll();
        centerPanel.removeAll();

        // Botão voltar
        JButton btnBack = new JButton("Return");
        btnBack.setBackground(btnColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        topPanel.add(btnBack);

        // Título
        JLabel title = new JLabel("Empire: " + empire.getName());
        title.setForeground(Color.WHITE);
        centerPanel.add(title);

        // Exemplo: botão de stats
        JButton btnStats = new JButton("View Stats");
        btnStats.setBackground(btnColor);
        btnStats.setForeground(Color.WHITE);
        btnStats.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, empire.statusReport());
        });

        centerPanel.add(btnStats);
    }
}
