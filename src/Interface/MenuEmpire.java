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


    // -----------------------------------------------------------------------------------
    // --------------------------- Construtor MenuEpire ----------------------------------
    // -----------------------------------------------------------------------------------
    public MenuEmpire(BancoDeDados db, MenuManager manager, Empire empire) {
        this.db = db;
        this.manager = manager;
        this.empire = empire;
        this.buttons = manager.getButtons();
    }


    // -----------------------------------------------------------------------------------
    // --------------------------- Classe Render da mae ----------------------------------
    // -----------------------------------------------------------------------------------
    @Override
    public void render() {

        topPanel.removeAll();
        centerPanel.removeAll();

        
        // ############
        // BOTAO VOLTAR
        // ############
        JButton btnBack = new JButton("Return");
        btnBack.setBackground(btnColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        topPanel.add(btnBack);

        
        // ###################
        // DETALHES DO IMPÉRIO
        // ###################

        // Título do império
        leftPanel.add(Box.createVerticalStrut(10));
        JLabel title = new JLabel("     Empire: " + empire.getName());
        title.setForeground(Color.WHITE);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        leftPanel.add(title);

        leftPanel.add(Box.createVerticalStrut(10));

        // Relatório de status do império
        String[] linhas = empire.statusReport().split("\n");
        for (String linha : linhas) {
            JLabel lbl = new JLabel(linha);
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            leftPanel.add(lbl);
            leftPanel.add(Box.createVerticalStrut(5));
        }


        // ###########
        // Botao Casas
        // ###########
        JButton btnHouse = new JButton("Build Houses");
        btnHouse.setBackground(btnColor);
        btnHouse.setForeground(Color.WHITE);
        btnHouse.addActionListener(e -> {
            buttons.btnBuildHouse(empire); // constrói
            manager.switchMenu(new MenuEmpire(db, manager, empire));
        });

        centerPanel.add(btnHouse);


        // ##############
        // Botao Fazendas
        // ##############
        JButton BtnFarm = new JButton("Farms");
        BtnFarm.setBackground(btnColor);
        BtnFarm.setForeground(Color.WHITE);
        BtnFarm.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(BtnFarm);


        // ###########
        // Botao Minas
        // ###########
        JButton btnMine = new JButton("Mines");
        btnMine.setBackground(btnColor);
        btnMine.setForeground(Color.WHITE);
        btnMine.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(btnMine);


        // ################
        // Botao Lenhadores
        // ################
        JButton btnLumber = new JButton("Lumber");
        btnLumber.setBackground(btnColor);
        btnLumber.setForeground(Color.WHITE);
        btnLumber.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(btnLumber);


        // ##############
        // Botao Exercito
        // ##############
        JButton btnArmy = new JButton("Army");
        btnArmy.setBackground(btnColor);
        btnArmy.setForeground(Color.WHITE);
        btnArmy.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(btnArmy);


        // #############
        // Botao Guerras
        // #############
        JButton btnWar = new JButton("Wars");
        btnWar.setBackground(btnColor);
        btnWar.setForeground(Color.WHITE);
        btnWar.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(btnWar);

    }
}
