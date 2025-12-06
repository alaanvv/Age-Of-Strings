package Interface;

import java.awt.Color;
import javax.swing.*;
import modelo.Empire;
import persistencia.BancoDeDados;

public class MenuEmpire extends MenuBase {

    private final MenuManager manager;
    private final Empire empire;
    private final Buttons buttons;


    // -----------------------------------------------------------------------------------
    // --------------------------- Construtor MenuEpire ----------------------------------
    // -----------------------------------------------------------------------------------
    public MenuEmpire(BancoDeDados db, MenuManager manager, Empire empire) {
        super(db);
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
        JButton backButton = new JButton("Return");
        backButton.setBackground(btnColor);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        topPanel.add(backButton);

        
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
        JButton buildHouseButton = new JButton("Build Houses");
        buildHouseButton.setBackground(btnColor);
        buildHouseButton.setForeground(Color.WHITE);
        buildHouseButton.addActionListener(e -> {
            buttons.buildHouse(empire);
            manager.switchMenu(new MenuEmpire(db, manager, empire));
        });

        centerPanel.add(buildHouseButton);


        // ##############
        // Botao Fazendas
        // ##############
        JButton farmButton = new JButton("Farms");
        farmButton.setBackground(btnColor);
        farmButton.setForeground(Color.WHITE);
        farmButton.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(farmButton);


        // ###########
        // Botao Minas
        // ###########
        JButton mineButton = new JButton("Mines");
        mineButton.setBackground(btnColor);
        mineButton.setForeground(Color.WHITE);
        mineButton.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(mineButton);


        // ################
        // Botao Lenhadores
        // ################
        JButton lumberButton = new JButton("Set lumbers");
        lumberButton.setBackground(btnColor);
        lumberButton.setForeground(Color.WHITE);
        lumberButton.addActionListener(e -> {
            buttons.adjustLumberWorkers(empire);
            manager.switchMenu(new MenuEmpire(db, manager, empire));
        });

        centerPanel.add(lumberButton);


        // ##############
        // Botao Exercito
        // ##############
        JButton armyButton = new JButton("Army");
        armyButton.setBackground(btnColor);
        armyButton.setForeground(Color.WHITE);
        armyButton.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(armyButton);


        // #############
        // Botao Guerras
        // #############
        JButton warButton = new JButton("Wars");
        warButton.setBackground(btnColor);
        warButton.setForeground(Color.WHITE);
        warButton.addActionListener(e -> manager.switchMenu(new MenuMain(db, manager)));

        centerPanel.add(warButton);

    }
}
