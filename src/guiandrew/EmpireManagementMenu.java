package guiandrew;

import java.awt.*;

import javax.swing.*;
import javax.swing.SwingConstants;

import modelo.Lumber;

public class EmpireManagementMenu extends JPanel{
   Gui gui;

   // === CONSTANTS ===
   private final int ROWS = 0;
   private final int COLUMNS = 3;

   // === COMPONENTS ===
   JLabel menuTitle;
   JPanel mainPanel;
   JPanel subMenusPanel;
   InfoLabel empireInfoLabel;


   public EmpireManagementMenu(Gui gui){
      super(new BorderLayout());

      this.gui = gui;
      
      menuTitle = new JLabel();
      mainPanel = new JPanel(new BorderLayout());
      subMenusPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
      empireInfoLabel = new InfoLabel();

      // === MENU TITLE SETUP ===
      menuTitle.setHorizontalAlignment(SwingConstants.CENTER);
      menuTitle.setText("Menu do Império");
      add(menuTitle, BorderLayout.NORTH);

      // === Sub Menus Panel Settings ===
      JButton farmMenuButton = new JButton("Fazendas");
      farmMenuButton.addActionListener(e -> {farmButtonAction();});
      
      JButton mineMenuButton = new JButton("Minas");
      mineMenuButton.addActionListener(e -> {mineButtonAction();});
      
      JButton armyMenuButton = new JButton("Exércitos");
      armyMenuButton.addActionListener(e -> {armyButtonAction();});
      
      JButton battleMenuButton = new JButton("Batalhas");
      battleMenuButton.addActionListener(e -> {battleButtonAction();});
      
      JButton lumberButton = new JButton("Produção de Madeira");
      lumberButton.addActionListener(e -> {lumberButtonAction();});
      
      JButton houseButton = new JButton("Construir casas");
      houseButton.addActionListener(e -> {houseButtonAction();});

      JButton backButton = new JButton("Voltar para lista de impérios");
      backButton.addActionListener(e -> gui.switchToMenuEmpire());

      subMenusPanel.add(farmMenuButton);
      subMenusPanel.add(mineMenuButton);
      subMenusPanel.add(armyMenuButton);
      subMenusPanel.add(battleMenuButton);
      subMenusPanel.add(lumberButton);
      subMenusPanel.add(houseButton);
      subMenusPanel.add(backButton);

      // === EMPIRE INFO LABEL SETUP ===
      updateInfoLabel();

      // === MAIN PANEL SETUP ===
      mainPanel.add(empireInfoLabel, BorderLayout.WEST);
      mainPanel.add(subMenusPanel, BorderLayout.CENTER);

      add(mainPanel, BorderLayout.CENTER);
   }

   public void updatePanel(){
      updateInfoLabel();
      modelo.Empire empire = gui.getViewingEmpire();
      if(empire != null){
         menuTitle.setText("Menu " + empire.getName());
      }
   }

   public void updateInfoLabel(){
      modelo.Empire empire = gui.getViewingEmpire();
      if(empire == null){
         empireInfoLabel.setText("<html><center>Nenhum império selecionado.</center></html>");
         return;
      }
      String text = (
         "<html><center>" +
         "<b>Menu de " + empire.getName() + "</b><p>" +
         "Ouro: " + empire.getGold() + "<br>" +
         "Madeira: " + empire.getWood() + "<br>" +
         "Ferro: " + empire.getIron() + "<br>" +
         "Comida: " + empire.getFood() + "<br>" +
         "População: " + empire.getPopulation() + "<br>" +
         "Trabalhadores: " + empire.getWorkers() + "<br>" +
         "Batalhas: " + empire.getBattleCount() +
         "</center></html>"
      );
      empireInfoLabel.setText(text);
   }

   // === BUTTON ACTIONS ===
   private void farmButtonAction(){
      gui.switchToFarmMenu();
      
   }
   private void mineButtonAction(){
      gui.switchToMineMenu();
   }
   private void armyButtonAction(){
      gui.switchToArmyMenu();
   }
   private void battleButtonAction(){
      gui.switchToBattleMenu();
   }
   private void lumberButtonAction(){
      modelo.Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      Lumber lumber = empire.getLumber();
      if(lumber == null){
         JOptionPane.showMessageDialog(this, "Campo de lenhadores não encontrado.");
         return;
      }

      // Criar popup com botões de adicionar e remover trabalhadores
      JPanel panel = new JPanel(new java.awt.GridLayout(3, 1, 5, 5));
      JLabel infoLabel = new JLabel("<html><center>Campo de Lenhadores<br>Trabalhadores atuais: " + lumber.getWorkers() + "</center></html>");
      JButton addWorkersButton = new JButton("Adicionar trabalhadores");
      JButton removeWorkersButton = new JButton("Remover trabalhadores");
      
      addWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos trabalhadores adicionar?");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int sent = empire.sendWorkersToLumber(amount);
            JOptionPane.showMessageDialog(this, "Trabalhadores adicionados: " + sent);
            updateInfoLabel();
            infoLabel.setText("<html><center>Campo de Lenhadores<br>Trabalhadores atuais: " + lumber.getWorkers() + "</center></html>");
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });
      
      removeWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos trabalhadores remover?");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int taken = empire.takeWorkersFromLumber(amount);
            JOptionPane.showMessageDialog(this, "Trabalhadores removidos: " + taken);
            updateInfoLabel();
            infoLabel.setText("<html><center>Campo de Lenhadores<br>Trabalhadores atuais: " + lumber.getWorkers() + "</center></html>");
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });
      
      panel.add(infoLabel);
      panel.add(addWorkersButton);
      panel.add(removeWorkersButton);
      
      JOptionPane.showMessageDialog(this, panel, "Gerenciar Produção de Madeira", JOptionPane.PLAIN_MESSAGE);
   }
   private void houseButtonAction(){
      modelo.Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      boolean built = empire.buildHouse();
      if(!built){
         JOptionPane.showMessageDialog(this, "Recursos insuficientes para construir casa (5 ouro, 5 madeira).");
      }else{
         JOptionPane.showMessageDialog(this, "Casa construída! População +3.");
         updateInfoLabel();
      }
   }
}
