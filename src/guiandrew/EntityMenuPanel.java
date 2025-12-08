package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.*;

/* Modelo do painel:
            \center left label estático
\top left botões add, remover, editar, buscar

\center.fill tabela com cada subentidade e suas informações
*/

public abstract class EntityMenuPanel<T extends modelo.Entidade> extends JPanel{
   
   
   modelo.Empire empireViewing;
   BancoDeDados db;
   Persistente<T> persistency;
   
   JPanel topPanelMenuNameButtons;
   JPanel topButtonsPanel;
   JLabel infoLeftLabel;
   JScrollPane contentCenterPanel;

   // === BUTTONS ===
   JButton add;
   JButton remove;
   JButton edit;
   JButton search;

   public EntityMenuPanel(
      BancoDeDados db,
      JLabel infoLeftLabel,
      Persistente<T> persistency
   ){
      super(new BorderLayout());
      
      this.db = db;
      this.persistency = persistency;
      this.infoLeftLabel = infoLeftLabel;
      
      // === CRIAÇÃO BASE DOS BOTÕES ===
      add = new JButton("Adicionar");
      add.addActionListener(e -> {add();});

      remove = new JButton("Remover");
      remove.addActionListener(e -> {remove();});

      edit = new JButton("Editar selecionado");
      edit.addActionListener(e -> {edit();});

      search = new JButton("Pesquisar entidade por ID");
      search.addActionListener(e -> search());

      topButtonsPanel = new JPanel(new FlowLayout());
      
      topButtonsPanel.add(add);
      topButtonsPanel.add(remove);
      topButtonsPanel.add(edit);
      topButtonsPanel.add(search);
   }

   public abstract void add();
   public abstract void remove();
   public abstract void edit();

   /**Finds any entity of type {@link T} by ID.  */
   public void search(){
      int id = auxiliar.Input.getIntDialogue(this, "Insira o ID a buscar");
      
      try{
          T entityFound = persistency.findById(id);
         JOptionPane.showMessageDialog(this, "Entidade: " + entityFound);
      } catch(persistencia.InexistentIdException e){
         JOptionPane.showMessageDialog(this, "ID inexistente.");
      }
   }
   
   public abstract void update();
   public abstract void updateLeftLabel();
   public abstract void updateContent();
}
