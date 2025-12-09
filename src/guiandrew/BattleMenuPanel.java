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
      header = new String[]{"ID Batalha", "ID Atacante", "ID Defensor", "Soldados Atk (vivos/inicial)", "Soldados Def (vivos/inicial)", "General Atk vivo?", "General Def vivo?", "Turnos", "Vencedor"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      battlesTable = new JTable(tableModel);
      
      // Centralizar valores na tabela
      javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
      for(int i = 0; i < header.length; i++){
         battlesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }
      
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
      
      // Mostrar lista de exércitos do império atual para selecionar atacante
      java.util.ArrayList<Army> currentEmpireArmies = new java.util.ArrayList<>();
      for(Army a : db.getArmies().getEntidades().values()){
         if(a.getEmpireId() == empire.getId() && !a.isBattling()){
            currentEmpireArmies.add(a);
         }
      }
      
      if(currentEmpireArmies.isEmpty()){
         JOptionPane.showMessageDialog(this, "Nenhum exército disponível para atacar (sem batalha).");
         return;
      }
      
      // Criar lista de opções para atacante
      String[] attackerOptions = new String[currentEmpireArmies.size()];
      for(int i = 0; i < currentEmpireArmies.size(); i++){
         Army a = currentEmpireArmies.get(i);
         attackerOptions[i] = "ID " + a.getId() + " - Soldados: " + a.getSoldiersAmount();
      }
      
      String attackerChoice = (String) JOptionPane.showInputDialog(
         this,
         "Selecione o exército atacante:",
         "Escolher Atacante",
         JOptionPane.QUESTION_MESSAGE,
         null,
         attackerOptions,
         attackerOptions[0]
      );
      
      if(attackerChoice == null) return;
      
      int attackerIndex = java.util.Arrays.asList(attackerOptions).indexOf(attackerChoice);
      Army attackerArmy = currentEmpireArmies.get(attackerIndex);
      
      // Mostrar lista de exércitos de outros impérios para selecionar defensor
      java.util.ArrayList<Army> otherEmpireArmies = new java.util.ArrayList<>();
      for(Army a : db.getArmies().getEntidades().values()){
         if(a.getEmpireId() != empire.getId() && !a.isBattling()){
            otherEmpireArmies.add(a);
         }
      }
      
      if(otherEmpireArmies.isEmpty()){
         JOptionPane.showMessageDialog(this, "Nenhum exército inimigo disponível para batalha.");
         return;
      }
      
      // Criar lista de opções para defensor
      String[] defenderOptions = new String[otherEmpireArmies.size()];
      for(int i = 0; i < otherEmpireArmies.size(); i++){
         Army a = otherEmpireArmies.get(i);
         try{
            modelo.Empire armyEmpire = db.getEmpires().findById(a.getEmpireId());
            defenderOptions[i] = "ID " + a.getId() + " (Império: " + armyEmpire.getName() + ") - Soldados: " + a.getSoldiersAmount();
         }catch(InexistentIdException e){
            defenderOptions[i] = "ID " + a.getId() + " - Soldados: " + a.getSoldiersAmount();
         }
      }
      
      String defenderChoice = (String) JOptionPane.showInputDialog(
         this,
         "Selecione o exército defensor:",
         "Escolher Defensor",
         JOptionPane.QUESTION_MESSAGE,
         null,
         defenderOptions,
         defenderOptions[0]
      );
      
      if(defenderChoice == null) return;
      
      int defenderIndex = java.util.Arrays.asList(defenderOptions).indexOf(defenderChoice);
      Army defenderArmy = otherEmpireArmies.get(defenderIndex);
      
      try{
         int battleId = db.createBattle(attackerArmy.getId(), defenderArmy.getId());
         if(battleId == 0){
            JOptionPane.showMessageDialog(this, "Não foi possível criar batalha.");
         }else{
            JOptionPane.showMessageDialog(this, "Batalha criada com sucesso! ID: " + battleId);
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
      // Libera os exércitos participantes antes de remover a batalha
      Army attacker = removingBattle.getAttacker();
      Army defender = removingBattle.getDefender();
      if(attacker != null){
         attacker.setInBattle(false);
         attacker.setCurrentBattle(null);
      }
      if(defender != null){
         defender.setInBattle(false);
         defender.setCurrentBattle(null);
      }
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
         row[3] = b.getAttackerSoldiersAlive() + "/" + b.getInitialAttackerSoldiers();
         row[4] = b.getDefenderSoldiersAlive() + "/" + b.getInitialDefenderSoldiers();
         row[5] = b.isAttackerGeneralAlive() ? "Sim" : "Não";
         row[6] = b.isDefenderGeneralAlive() ? "Sim" : "Não";
         row[7] = b.getTurnCount();
         row[8] = b.getWinner() == 1 ? "Atacante" : (b.getWinner() == -1 ? "Defensor" : "Em andamento");
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
         "Batalhas ligadas: " + tableModel.getRowCount() + "<p>" +
         "<b>Informações:</b><br>" +
         "Exércitos em batalha não podem<br>ser atacados novamente<br>" +
         "Exército derrotado é destruído.<br>" +
         "General vivo influencia moral.<br>" +
         "Turnos simulados até o fim." +
         "</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
