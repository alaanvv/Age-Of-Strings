package guiandrew;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import modelo.Empire;
import modelo.Lumber;
import persistencia.BancoDeDados;

public class LumberMenuPanel extends AbstractEntityMenuPanel<Lumber>{

   JTable lumberTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Lumber> lumberInTableRow;

   public LumberMenuPanel(BancoDeDados db, persistencia.Persistente<Lumber> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Lenhadores", persistency, externalCardPanel, gui);
      lumberInTableRow = new ArrayList<>();
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
      lumberTable = new JTable(tableModel);
      contentCentralPanel = new JScrollPane(lumberTable);
   }

   @Override
   protected void addAction(){
      JOptionPane.showMessageDialog(this, "Cada império já possui um campo inicial.");
   }

   @Override
   protected void removeAction(){
      JOptionPane.showMessageDialog(this, "Não é possível remover o campo de lenhadores.");
   }

   @Override
   protected void editAction(){
      JOptionPane.showMessageDialog(this, "Edição não disponível para lenhadores.");
   }

   @Override
   protected void updatePanel(){
      updateLumberTable();
      updateLeftLabel();
   }

   private void updateLumberTable(){
      tableModel.setRowCount(0);
      lumberInTableRow.clear();
      Empire empire = gui.getViewingEmpire();
      if(empire == null) return;
      Lumber lumber = empire.getLumber();
      if(lumber == null) return;
      lumberInTableRow.add(lumber);
      Object[] row = new Object[header.length];
      row[0] = lumber.getId();
      row[1] = lumber.getWorkers();
      row[2] = lumber.getEmpireId();
      tableModel.addRow(row);
   }

   @Override
   protected void updateLeftLabel(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         infoLeftLabel.setText("<html><center>Nenhum império selecionado.</center></html>");
         return;
      }
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>Campo de lenhadores</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
