package guiandrew;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//import java.awt.*;
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
   JButton runTurnButton;

   public EmpireMenuPanel(BancoDeDados db, persistencia.Persistente<Empire> persistency, JPanel externalCardPanel, Gui gui){
      super(db, "Menu Império", persistency, externalCardPanel, gui);

      empireInTableRow = new ArrayList<Empire>();

      // Adiciona o botão que acessa o menu do império selecionado
      acessButton = new JButton("Gerenciar Império");
      acessButton.addActionListener(e -> {acessAction();});
      buttonsPanel.add(acessButton);

      runTurnButton = new JButton("Rodar Turno");
      runTurnButton.addActionListener(e -> runTurnAction());
      buttonsPanel.add(runTurnButton);

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

   private void runTurnAction(){
      StringBuilder log = new StringBuilder();

      // Recursos/população
      for(Empire e : db.getEmpires().getEntidades().values()){
         String result = e.runTurn();
         if(!result.isEmpty()) log.append(result);
      }

      // Batalhas: roda um turno para cada e aplica vencedor
      java.util.List<modelo.Battle> battlesSnapshot = new java.util.ArrayList<>(db.getBattles().getEntidades().values());
      for(modelo.Battle b : battlesSnapshot){
         int res = b.processTurn(db);
         if(res != 0){
            // Gera relatório detalhado
            modelo.Army winnerArmy = res == 1 ? b.getAttacker() : b.getDefender();
            modelo.Army loserArmy = res == 1 ? b.getDefender() : b.getAttacker();
            String winnerSide = res == 1 ? "Atacante" : "Defensor";
            
            try{
               modelo.Empire winnerEmpire = db.getEmpires().findById(winnerArmy.getEmpireId());
               modelo.Empire loserEmpire = db.getEmpires().findById(loserArmy.getEmpireId());
               
               log.append(String.format("%n===== BATALHA #%d CONCLUÍDA =====%n", b.getId()));
               log.append(String.format("Vencedor: %s (Exército #%d do Império %s)%n", 
                  winnerSide, winnerArmy.getId(), winnerEmpire.getName()));
               log.append(String.format("Perdedor: Exército #%d do Império %s (DESTRUÍDO)%n", 
                  loserArmy.getId(), loserEmpire.getName()));
               log.append(String.format("Soldados vencedores sobreviventes: %d de %d iniciais%n", 
                  res == 1 ? b.getAttackerSoldiersAlive() : b.getDefenderSoldiersAlive(),
                  res == 1 ? b.getInitialAttackerSoldiers() : b.getInitialDefenderSoldiers()));
               log.append(String.format("Turnos de batalha: %d%n", b.getTurnCount()));
               log.append("============================\n\n");
            }catch(persistencia.InexistentIdException ignored){
               log.append(String.format("Batalha #%d terminou. Vencedor: %s.%n", b.getId(), winnerSide));
            }
            
            db.destroyEntity(b);
         }
      }

      updatePanel();
      if(log.length() > 0){
         JOptionPane.showMessageDialog(this, log.toString());
      } else {
         JOptionPane.showMessageDialog(this, "Turno concluído.");
      }
   }

   // === BUTTONS ACTIONS ===

   @Override
   protected void addAction(){
      String name = JOptionPane.showInputDialog(this, "Nome do império:");
      
      if(name == null || name.trim().isEmpty()){
         return;
      }

      db.createEmpire(name.trim());

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
         rowEmpire[6] = e.getPopulation();
         rowEmpire[7] = e.getWorkers();
         rowEmpire[8] = e.getBattleCount();

         tableModel.addRow(rowEmpire);
      }
   }

   @Override
   protected void updateLeftLabel(){
      String labelText = "<html><center>Bem-vindo ao <b>Age of Strings!</b><p>Quantidade de impérios: " + db.getEmpires().getSize() + ".</center></html>";
      infoLeftLabel.setText(labelText);
   };

}
