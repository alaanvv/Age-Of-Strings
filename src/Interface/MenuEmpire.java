package Interface;

import java.awt.Color;
import javax.swing.*;
import modelo.Empire;
import persistencia.BancoDeDados;

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
        JLabel title = new JLabel("     Empire: " + empire.getName());
        JLabel lb = new JLabel("\n");
        title.setForeground(Color.WHITE);
        leftPanel.add(lb);
        leftPanel.add(title);
        leftPanel.add(lb);

        String[] linhas = empire.statusReport().split("\n");

        for (String linha : linhas) {
            JLabel lbl = new JLabel(linha);
            lbl.setForeground(Color.WHITE);
            leftPanel.add(lbl);
        }

    }
}
