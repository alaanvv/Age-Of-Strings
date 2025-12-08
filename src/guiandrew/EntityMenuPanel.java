package guiandrew;

import javax.swing.*;
import java.awt.*;

import persistencia.*;


/**
 * Esta classe abstrata é um molde para todos os outros menus. <p>
 * O molde consiste em um painel superior, que contém o título do menu no topo e os botões padrões
 * (add, remover, etc.) em baixo, como um FlowLayout. <p>
 * Também um painel à esquerda (JLabel), para colocar informações envolvendo o menu. <p>
 * Finalmente, no centro, um JScrollPane para colocar o conteúdo principal do menu.
 */
public abstract class EntityMenuPanel<T extends modelo.Entidade> extends JPanel{
   
   
   // Data attributes
   protected BancoDeDados db;
   protected modelo.Empire empireViewing;
   protected Persistente<T> persistency;
   
   // -- Top Panel components
   protected JPanel topPanelMenuNameButtons;
   protected JPanel buttonsPanel;
   protected JLabel panelTitleLabel;
   
   // -- Left Panel component --
   protected JLabel infoLeftLabel;

   // -- Center Panel component
   protected JScrollPane contentCenterPanel;
   
   // === BUTTONS ===
   protected JButton addButton;
   protected JButton removeButton;
   protected JButton editButton;
   protected JButton searchButton;


   public EntityMenuPanel(
      BancoDeDados db,
      JLabel infoLeftLabel,
      String panelTitle,
      Persistente<T> persistency
   ){
      super(new BorderLayout());
      
      this.db = db;
      this.persistency = persistency;
      this.infoLeftLabel = infoLeftLabel;

      add(infoLeftLabel, BorderLayout.WEST);
      
      // === CRIAÇÃO BASE DOS BOTÕES ===
      addButton = new JButton("Adicionar");
      addButton.addActionListener(e -> {addAction();});

      removeButton = new JButton("Remover");
      removeButton.addActionListener(e -> {removeAction();});

      editButton = new JButton("Editar selecionado");
      editButton.addActionListener(e -> {editAction();});

      searchButton = new JButton("Pesquisar entidade por ID");
      searchButton.addActionListener(e -> searchAction());

      buttonsPanel = new JPanel(new FlowLayout());
      
      buttonsPanel.add(addButton);
      buttonsPanel.add(removeButton);
      buttonsPanel.add(editButton);
      buttonsPanel.add(searchButton);

      // === CRIAÇÃO PAINEL SUPERIOR ===
      topPanelMenuNameButtons = new JPanel(new BoxLayout(topPanelMenuNameButtons, BoxLayout.Y_AXIS));
      
      panelTitleLabel = new JLabel(panelTitle);
      topPanelMenuNameButtons.add(panelTitleLabel);

      topPanelMenuNameButtons.add(buttonsPanel);

      add(topPanelMenuNameButtons);

      // === CRIAÇÃO DO PAINEL CENTRAL ===
      createCentralPanel();
   }

   public abstract void addAction();
   public abstract void removeAction();
   public abstract void editAction();

   /**Finds any entity of type {@link T} by ID.  */
   public void searchAction(){
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

   public abstract void createCentralPanel();
}
