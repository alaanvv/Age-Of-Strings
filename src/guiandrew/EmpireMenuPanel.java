package guiandrew;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.ArrayList;

import modelo.Empire;

import persistencia.BancoDeDados;

public class EmpireMenuPanel extends AbstractEntityMenuPanel<modelo.Empire>{
   
   // === CENTER PANEL ===
   JTable empiresTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Empire> empireInTableRow;

   // === EMPIRE BUTTONS ===
   JButton acessButton;

   public EmpireMenuPanel(BancoDeDados db, persistencia.Persistente<Empire> persistency, String panelTitle, JPanel externalCardPanel, Gui gui){
      super(db, panelTitle, persistency, externalCardPanel, gui);

      empireInTableRow = new ArrayList<Empire>();

      // Adiciona o botão que acessa o menu do império selecionado
      acessButton = new JButton("Gerenciar Império");
      acessButton.addActionListener(e -> {acessAction();});
      buttonsPanel.add(acessButton);

   }

   @Override
   protected void createCentralPanel() {
      
      //Colunas da tabela de impérios
      String[] columns = {"ID", "Nome", "Madeira", "Ferro", "Ouro", "Comida",  "População livre", "Trabalhadores", "Batalhas"};
      header = columns;

      //Cria uma tabela em que todas as células são não editáveis.
      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };
      
      tableModel.setColumnIdentifiers(header);
      tableModel.addRow(columns);
      
      updateEmpireTable();
      
      empiresTable = new JTable(tableModel);

      contentCentralPanel = new JScrollPane(empiresTable);
   }

   
   private Empire getSelectedEmpire(){
      int selected = empiresTable.getSelectedRow();

      if(selected == -1){
         JOptionPane.showMessageDialog(this, "Selecione um império!");
         return null;
      }

      return empireInTableRow.get(selected);
   }

   // === BUTTONS ACTIONS ===

   @Override
   protected void addAction(){
      String name = JOptionPane.showInputDialog(this, "Nome do império:");

      db.createEmpire(name);

      JOptionPane.showMessageDialog(this, "Império criado com sucesso!");
      updatePanel();
   };
   
   @Override
   protected void removeAction(){

      Empire removingEmpire = getSelectedEmpire();
      if (removingEmpire == null) return;

      db.destroyEmpire(removingEmpire);
      empireInTableRow.remove(removingEmpire);
      
      updatePanel();
   };
   
   @Override
   protected void editAction(){
      Empire selectedEmpire = getSelectedEmpire();
      if (selectedEmpire == null) return;

      String newEmpireName = JOptionPane.showInputDialog("Digite o novo nome:");
      newEmpireName = newEmpireName.trim();
      
      if(newEmpireName.isEmpty()){
         JOptionPane.showMessageDialog(this, "Insira um nome válido.");
         return;
      }

      selectedEmpire.setName(newEmpireName);
      
      updatePanel();
   };

   private void acessAction(){
      Empire selectedEmpire = getSelectedEmpire();
      if (selectedEmpire == null) return;

      gui.switchToMenuManageEmpire(selectedEmpire);
   }
   

   // === UPDATE METHODS ===
   @Override
   protected void updatePanel(){
      updateEmpireTable();
      updateLeftLabel();
   };
   
   private void updateEmpireTable(){
      tableModel.setRowCount(0);

      if(empireInTableRow == null) return;

      empireInTableRow.clear();

      for(Empire e: persistency.getEntidades().values()){
         Object[] rowEmpire = new Object[header.length];
         empireInTableRow.add(e);
         
         rowEmpire[0] = e.getId();
         rowEmpire[1] = e.getName();
         rowEmpire[2] = e.getWood();
         rowEmpire[3] = e.getIron();
         rowEmpire[4] = e.getGold();
         rowEmpire[5] = e.getFood();
         rowEmpire[5] = e.getPopulation();
         rowEmpire[6] = e.getWorkers();
         rowEmpire[7] = e.getBattleCount();

         assert(header.length == 8);

         tableModel.addRow(rowEmpire);
      }
   }

   @Override
   protected void updateLeftLabel(){
      String labelText = "<html><center>Bem-vindo ao <b>Age of Strings!</b><p>Quantidade de impérios: " + db.getEmpires().getSize() + ".</center></html>";
      infoLeftLabel.setText(labelText);
   };

}
