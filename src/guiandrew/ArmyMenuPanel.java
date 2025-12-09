package guiandrew;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import modelo.Army;
import modelo.Empire;
import persistencia.BancoDeDados;
import persistencia.InexistentIdException;

public class ArmyMenuPanel extends AbstractEntityMenuPanel<Army>{

   JTable armiesTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Army> armiesInTableRow;

   public ArmyMenuPanel(BancoDeDados db, persistencia.Persistente<Army> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Exércitos", persistency, externalCardPanel, gui);
      armiesInTableRow = new ArrayList<>();
      JButton backButton = new JButton("Voltar");
      backButton.addActionListener(e -> gui.switchToMenuManageEmpire(gui.getViewingEmpire()));
      buttonsPanel.add(backButton);
   }

   @Override
   protected void createCentralPanel() {
      header = new String[]{"ID Exército", "Soldados", "Nível Recrutamento", "Custo", "Em batalha?"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      armiesTable = new JTable(tableModel);
      
      // Centralizar valores na tabela
      javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
      for(int i = 0; i < header.length; i++){
         armiesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }
      
      contentCentralPanel = new JScrollPane(armiesTable);
   }

   private Army getSelectedArmy(){
      int selected = armiesTable.getSelectedRow();
      if(selected == -1){
         JOptionPane.showMessageDialog(this, "Selecione um exército!");
         return null;
      }
      return armiesInTableRow.get(selected);
   }

   @Override
   protected void addAction(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      try{
         int armyId = db.createArmy(empire.getId());
         if(armyId == 0){
            JOptionPane.showMessageDialog(this, "Recursos insuficientes para criar exército.");
         }
      }catch(InexistentIdException e){
         JOptionPane.showMessageDialog(this, "Império inexistente.");
      }
      updatePanel();
   }

   @Override
   protected void removeAction(){
      Army removingArmy = getSelectedArmy();
      if(removingArmy == null) return;
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      empire.getArmies().remove(removingArmy.getId());
      db.destroyEntity(removingArmy);
      updatePanel();
   }

   @Override
   protected void editAction(){
      Army army = getSelectedArmy();
      if(army == null) return;
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }

      JPanel panel = new JPanel(new java.awt.GridLayout(3, 1, 5, 5));
      JButton upgradeArmory = new JButton("Aumentar armoria");
      JButton addWorkersButton = new JButton("Adicionar soldados");
      JButton removeWorkersButton = new JButton("Remover soldados");

      upgradeArmory.addActionListener(e -> {
         String pointsStr = JOptionPane.showInputDialog(this, "Quantos pontos adicionar na armoria?");
         if(pointsStr == null) return;
         try{
            int points = Integer.parseInt(pointsStr.trim());
            if(points <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int added = army.upgradeArmory(points, empire);
            JOptionPane.showMessageDialog(this, "Pontos adicionados: " + added);
            updatePanel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });

      addWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos soldados adicionar?");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int sent = empire.sendWorkersToArmy(amount, army.getId());
            JOptionPane.showMessageDialog(this, "Soldados adicionados: " + sent);
            updatePanel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });

      removeWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos soldados remover?");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int taken = empire.takeWorkersFromArmy(amount, army.getId());
            JOptionPane.showMessageDialog(this, "Soldados removidos: " + taken);
            updatePanel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });

      panel.add(upgradeArmory);
      panel.add(addWorkersButton);
      panel.add(removeWorkersButton);

      JOptionPane.showMessageDialog(this, panel, "Gerenciar Exército #" + army.getId(), JOptionPane.PLAIN_MESSAGE);
   }

   @Override
   protected void updatePanel(){
      updateArmyTable();
      updateLeftLabel();
   }

   private void updateArmyTable(){
      tableModel.setRowCount(0);
      armiesInTableRow.clear();
      Empire empire = gui.getViewingEmpire();
      if(empire == null) return;

      for(Army a : persistency.getEntidades().values()){
         if(a.getEmpireId() != empire.getId()) continue;
         Object[] row = new Object[header.length];
         armiesInTableRow.add(a);
         row[0] = a.getId();
         row[1] = a.getSoldiersAmount();
         row[2] = a.getHiringLevel();
         row[3] = a.getHiringCost();
         row[4] = a.isBattling();
         tableModel.addRow(row);
      }
   }

   @Override
   protected void updateLeftLabel(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         infoLeftLabel.setText("<html><center>Nenhum império selecionado.</center></html>");
         return;
      }
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>" +
         "Exércitos: " + empire.getArmies().size() + "<p>" +
         "<b>Informações:</b><br>" +
         "Custo de novo exército:<br>50 ferro, 20 ouro<br><br>" +
         "Custo de melhoria armaria:<br>25 ferro, 5 ouro por nível<br><br>" +
         "Soldados custam ouro por turno</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
