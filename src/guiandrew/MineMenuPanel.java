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
      header = new String[]{"ID Mina", "Trabalhadores", "Ferro (extraído/total)", "Ouro (extraído/total)", "ID Império"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      minesTable = new JTable(tableModel);
      
      // Centralizar valores na tabela
      javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
      for(int i = 0; i < header.length; i++){
         minesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }
      
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
      Mine mine = getSelectedMine();
      if(mine == null) return;
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }

      // Criar popup com botões de adicionar e remover trabalhadores
      JPanel panel = new JPanel(new java.awt.GridLayout(2, 1, 5, 5));
      JButton addWorkersButton = new JButton("Adicionar trabalhadores");
      JButton removeWorkersButton = new JButton("Remover trabalhadores");
      
      addWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos trabalhadores adicionar? (Máx: 20 total)");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int sent = empire.sendWorkersToMine(amount, mine.getId());
            JOptionPane.showMessageDialog(this, "Trabalhadores adicionados: " + sent);
            updatePanel();
            gui.empireManagementMenu.updateInfoLabel();
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
            int taken = empire.takeWorkersFromMine(amount, mine.getId());
            JOptionPane.showMessageDialog(this, "Trabalhadores removidos: " + taken);
            updatePanel();
            gui.empireManagementMenu.updateInfoLabel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });
      
      panel.add(addWorkersButton);
      panel.add(removeWorkersButton);
      
      JOptionPane.showMessageDialog(this, panel, "Gerenciar trabalhadores - Mina #" + mine.getId(), JOptionPane.PLAIN_MESSAGE);
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
         row[2] = m.getExtractedIron() + "/" + m.getInitialIron();
         row[3] = m.getExtractedGold() + "/" + m.getInitialGold();
         row[4] = m.getEmpireId();
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
         "Minas: " + empire.getMines().size() + "<p>" +
         "<b>Informações:</b><br>" +
         "Custo de nova mina:<br>15 madeira, 5 ouro<br><br>" +
         "Máx trabalhadores por mina: 20<br>" +
         "Produção/turno: ~log(trab+1)* (ferro x3, ouro x2)<br>" +
         "Total extraível mostrado na tabela (ferro/ouro)." +
         "</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
