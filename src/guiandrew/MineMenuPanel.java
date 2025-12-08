package guiandrew;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import modelo.Empire;
import modelo.Mine;
import persistencia.BancoDeDados;
import persistencia.InexistentIdException;

public class MineMenuPanel extends AbstractEntityMenuPanel<Mine>{

   JTable minesTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Mine> minesInTableRow;

   public MineMenuPanel(BancoDeDados db, persistencia.Persistente<Mine> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Minas", persistency, externalCardPanel, gui);
      minesInTableRow = new ArrayList<>();
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
      minesTable = new JTable(tableModel);
      contentCentralPanel = new JScrollPane(minesTable);
   }

   private Mine getSelectedMine(){
      int selected = minesTable.getSelectedRow();
      if(selected == -1){
         JOptionPane.showMessageDialog(this, "Selecione uma mina!");
         return null;
      }
      return minesInTableRow.get(selected);
   }

   @Override
   protected void addAction(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      try{
         int mineId = db.createMine(empire.getId());
         if(mineId == 0){
            JOptionPane.showMessageDialog(this, "Recursos insuficientes para criar mina.");
         }
      }catch(InexistentIdException e){
         JOptionPane.showMessageDialog(this, "Império inexistente.");
      }
      updatePanel();
   }

   @Override
   protected void removeAction(){
      Mine removingMine = getSelectedMine();
      if(removingMine == null) return;
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      empire.getMines().remove(removingMine.getId());
      db.destroyEntity(removingMine);
      updatePanel();
   }

   @Override
   protected void editAction(){
      JOptionPane.showMessageDialog(this, "Edição não disponível para minas.");
   }

   @Override
   protected void updatePanel(){
      updateMineTable();
      updateLeftLabel();
   }

   private void updateMineTable(){
      tableModel.setRowCount(0);
      minesInTableRow.clear();
      Empire empire = gui.getViewingEmpire();
      if(empire == null) return;

      for(Mine m : persistency.getEntidades().values()){
         if(m.getEmpireId() != empire.getId()) continue;
         Object[] row = new Object[header.length];
         minesInTableRow.add(m);
         row[0] = m.getId();
         row[1] = m.getWorkers();
         row[2] = m.getEmpireId();
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
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>Minas: " + empire.getMines().size() + "</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
