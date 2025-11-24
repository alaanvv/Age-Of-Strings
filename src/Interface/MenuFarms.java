package Interface;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;

import modelo.Empire;
import persistencia.BancoDeDados;

public class MenuFarms extends MenuBase {

    private MenuManager manager;
    private Empire empire;

    public MenuFarms(BancoDeDados db, MenuManager manager, Empire empire) {
        super(db);
        this.manager = manager;
        this.empire = empire;
    }   


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
        btnBack.addActionListener(e -> manager.switchMenu(new MenuEmpire(db, manager, empire)));

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
        
    }
}
