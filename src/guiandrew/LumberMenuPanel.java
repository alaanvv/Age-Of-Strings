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
      header = new String[]{"ID Lenhadores", "Trabalhadores", "ID Império"};

      tableModel = new DefaultTableModel(){
         @Override
         public boolean isCellEditable(int row, int column){
            return false;
         }
      };

      tableModel.setColumnIdentifiers(header);
      lumberTable = new JTable(tableModel);
      
      // Centralizar valores na tabela
      javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
      for(int i = 0; i < header.length; i++){
         lumberTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }
      
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
      Empire empire = gui.getViewingEmpire();
      if(empire == null){
         JOptionPane.showMessageDialog(this, "Nenhum império selecionado.");
         return;
      }
      Lumber lumber = empire.getLumber();
      if(lumber == null){
         JOptionPane.showMessageDialog(this, "Campo de lenhadores não encontrado.");
         return;
      }

      // Criar popup com botões de adicionar e remover trabalhadores
      JPanel panel = new JPanel(new java.awt.GridLayout(2, 1, 5, 5));
      JButton addWorkersButton = new JButton("Adicionar trabalhadores");
      JButton removeWorkersButton = new JButton("Remover trabalhadores");
      
      addWorkersButton.addActionListener(e -> {
         String amountStr = JOptionPane.showInputDialog(this, "Quantos trabalhadores adicionar?");
         if(amountStr == null) return;
         try{
            int amount = Integer.parseInt(amountStr.trim());
            if(amount <= 0){
               JOptionPane.showMessageDialog(this, "Insira um número positivo.");
               return;
            }
            int sent = empire.sendWorkersToLumber(amount);
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
            int taken = empire.takeWorkersFromLumber(amount);
            JOptionPane.showMessageDialog(this, "Trabalhadores removidos: " + taken);
            updatePanel();
            gui.empireManagementMenu.updateInfoLabel();
         }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
         }
      });
      
      panel.add(addWorkersButton);
      panel.add(removeWorkersButton);
      
      JOptionPane.showMessageDialog(this, panel, "Gerenciar trabalhadores - Campo de Lenhadores", JOptionPane.PLAIN_MESSAGE);
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
      Lumber lumber = empire.getLumber();
      int workers = lumber != null ? lumber.getWorkers() : 0;
      String labelText = "<html><center>Império: <b>" + empire.getName() + "</b><p>" +
         "Campo de lenhadores<p>" +
         "Trabalhadores: " + workers + "<p>" +
         "<b>Informações:</b><br>" +
         "Cada império possui um<br>campo de lenhadores inicial<br><br>" +
         "Sem limite de trabalhadores<br><br>" +
         "Produção de madeira:<br>baseada em log(trabalhadores+1)</center></html>";
      infoLeftLabel.setText(labelText);
   }
}
