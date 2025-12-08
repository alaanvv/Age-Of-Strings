package guiandrew;

import javax.swing.*;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import modelo.Empire;

import persistencia.BancoDeDados;

public class EmpireMenu extends AbstractEntityMenuPanel<modelo.Empire>{
   
   // === CENTER PANEL ===
   JTable empiresTable;
   DefaultTableModel tableModel;
   String[] header;

   public EmpireMenu(BancoDeDados db, JLabel leftLabel, String panelTitle){
      super(db, leftLabel, panelTitle, db.getEmpires());

   }

   @Override
   protected void createCentralPanel() {
      String[] columns = {"ID", "Nome", "Madeira", "Ferro", "Ouro", "Comida",  "População livre", "População empregada", "Batalhas"};
      header = columns;

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };
      
      tableModel.setColumnIdentifiers(header);
      
      updateEmpireTable();
   }

   private void updateEmpireTable(){
      tableModel.setRowCount(0);

      for(Empire e: persistency.getEntidades().values()){
         Object[] rowEmpire = new Object[header.length];

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
   protected void addAction(){};
   
   @Override
   protected void removeAction(){};
   
   @Override
   protected void editAction(){};
   
   @Override
   protected void update(){};
   
   @Override
   protected void updateLeftLabel(){};
   
   @Override
   protected void updateContent(){};

}
