package guiandrew;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import persistencia.BancoDeDados;
import persistencia.InexistentIdException;

public class BattleMenuPanel extends AbstractEntityMenuPanel<Battle>{

   JTable battlesTable;
   DefaultTableModel tableModel;
   String[] header;
   ArrayList<Battle> battlesInTableRow;

   public BattleMenuPanel(BancoDeDados db, persistencia.Persistente<Battle> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Batalhas", persistency, externalCardPanel, gui);
      battlesInTableRow = new ArrayList<>();
      JButton backButton = new JButton("Voltar");
      backButton.addActionListener(e -> gui.switchToMenuManageEmpire(gui.getViewingEmpire()));
      buttonsPanel.add(backButton);
   }

   @Override
   protected void createCentralPanel() {
      header = new String[]{"ID", "Atacante", "Defensor"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      battlesTable = new JTable(tableModel);
      contentCentralPanel = new JScrollPane(battlesTable);
   }

   private Battle getSelectedBattle(){
      int selected = battlesTable.getSelectedRow();
      if(selected == -1){
         JOptionPane.showMessageDialog(this, "Selecione uma batalha!");
         return null;
      }
      return battlesInTableRow.get(selected);
   }

   @Override
   protected void addAction(){
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      int attackerId = auxiliar.Input.getIntDialogue(this, "ID do exército atacante");
      int defenderId = auxiliar.Input.getIntDialogue(this, "ID do exército defensor");
      try{
         int battleId = db.createBattle(attackerId, defenderId);
         if(battleId == 0){
            JOptionPane.showMessageDialog(this, "Não foi possível criar batalha.");
         }
      }catch(InexistentIdException e){
         JOptionPane.showMessageDialog(this, e.getMessage());
      }
      updatePanel();
   }

   @Override
   protected void removeAction(){
      Battle removingBattle = getSelectedBattle();
      if(removingBattle == null) return;
      db.destroyEntity(removingBattle);
      updatePanel();
   }

   @Override
   protected void editAction(){
      JOptionPane.showMessageDialog(this, "Edição não disponível para batalhas.");
   }

   @Override
   protected void updatePanel(){
      updateBattleTable();
      updateLeftLabel();
   }

   private void updateBattleTable(){
      tableModel.setRowCount(0);
      battlesInTableRow.clear();
      Empire empire = gui.getViewingEmpire();
      if(empire == null) return;

      for(Battle b : persistency.getEntidades().values()){
         Army attacker = b.getAttacker();
         Army defender = b.getDefender();
         boolean related = attacker.getEmpireId() == empire.getId() || defender.getEmpireId() == empire.getId();
         if(!related) continue;

         Object[] row = new Object[header.length];
         battlesInTableRow.add(b);
         row[0] = b.getId();
         row[1] = attacker.getId();
         row[2] = defender.getId();
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
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>Batalhas ligadas: " + tableModel.getRowCount() + "</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
