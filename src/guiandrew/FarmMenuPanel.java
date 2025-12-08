package guiandrew;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import modelo.Empire;
import modelo.Farm;
import persistencia.BancoDeDados;
import persistencia.InexistentIdException;

public class FarmMenuPanel extends AbstractEntityMenuPanel<Farm>{

   JTable farmsTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Farm> farmsInTableRow;

   public FarmMenuPanel(BancoDeDados db, persistencia.Persistente<Farm> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Fazendas", persistency, externalCardPanel, gui);
      farmsInTableRow = new ArrayList<>();
      JButton backButton = new JButton("Voltar");
      backButton.addActionListener(e -> gui.switchToMenuManageEmpire(gui.getViewingEmpire()));
      buttonsPanel.add(backButton);
   }

   @Override
   protected void createCentralPanel() {
      header = new String[]{"ID", "Trabalhadores", "Império"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      farmsTable = new JTable(tableModel);
      contentCentralPanel = new JScrollPane(farmsTable);
   }

   private Farm getSelectedFarm(){
      int selected = farmsTable.getSelectedRow();
      if(selected == -1){
         JOptionPane.showMessageDialog(this, "Selecione uma fazenda!");
         return null;
      }
      return farmsInTableRow.get(selected);
   }

   @Override
   protected void addAction(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      try{
         int farmId = db.createFarm(empire.getId());
         if(farmId == 0){
            JOptionPane.showMessageDialog(this, "Recursos insuficientes para criar fazenda.");
         }
      }catch(InexistentIdException e){
         JOptionPane.showMessageDialog(this, "Império inexistente.");
      }
      updatePanel();
   }

   @Override
   protected void removeAction(){
      Farm removingFarm = getSelectedFarm();
      if(removingFarm == null) return;
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      empire.getFarms().remove(removingFarm.getId());
      db.destroyEntity(removingFarm);
      updatePanel();
   }

   @Override
   protected void editAction(){
      JOptionPane.showMessageDialog(this, "Edição não disponível para fazendas.");
   }

   @Override
   protected void updatePanel(){
      updateFarmTable();
      updateLeftLabel();
   }

   private void updateFarmTable(){
      tableModel.setRowCount(0);
      farmsInTableRow.clear();
      Empire empire = gui.getViewingEmpire();
      if(empire == null) return;

      for(Farm f : persistency.getEntidades().values()){
         if(f.getEmpireId() != empire.getId()) continue;
         Object[] row = new Object[header.length];
         farmsInTableRow.add(f);
         row[0] = f.getId();
         row[1] = f.getWorkers();
         row[2] = f.getEmpireId();
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
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>Fazendas: " + empire.getFarms().size() + "</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
