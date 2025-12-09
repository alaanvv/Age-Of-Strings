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
      header = new String[]{"ID Fazenda", "Trabalhadores", "ID Império"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      farmsTable = new JTable(tableModel);
      
      // Centralizar valores na tabela
      javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
      for(int i = 0; i < header.length; i++){
         farmsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }
      
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
      // Devolve os trabalhadores para a população antes de remover
      empire.takeWorkersFromFarm(removingFarm.getWorkers(), removingFarm.getId());
      empire.getFarms().remove(removingFarm.getId());
      db.destroyEntity(removingFarm);
      updatePanel();
   }

   @Override
   protected void editAction(){
      Farm farm = getSelectedFarm();
      if(farm == null) return;
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
         String amountStr = JOptionPane.showInputDialog(this, "Quantos trabalhadores adicionar? (Máx: 10 total)");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int sent = empire.sendWorkersToFarm(amount, farm.getId());
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
            int taken = empire.takeWorkersFromFarm(amount, farm.getId());
            JOptionPane.showMessageDialog(this, "Trabalhadores removidos: " + taken);
            updatePanel();
            gui.empireManagementMenu.updateInfoLabel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });
      
      panel.add(addWorkersButton);
      panel.add(removeWorkersButton);
      
      JOptionPane.showMessageDialog(this, panel, "Gerenciar trabalhadores - Fazenda #" + farm.getId(), JOptionPane.PLAIN_MESSAGE);
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
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>" +
         "Fazendas: " + empire.getFarms().size() + "<p>" +
         "<b>Informações:</b><br>" +
         "Custo de nova fazenda:<br>5 madeira, 2 ouro<br><br>" +
         "Máx trabalhadores por fazenda: 10<br><br>" +
         "Produção: baseada em log(trabalhadores+1)</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
