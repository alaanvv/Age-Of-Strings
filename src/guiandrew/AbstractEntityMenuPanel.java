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
public abstract class AbstractEntityMenuPanel<T extends modelo.Entidade> extends JPanel{
   
   Gui gui;

   // Data attributes
   protected BancoDeDados db;
   protected modelo.Empire empireViewing;
   protected Persistente<T> persistency;
   
   // -- Top Panel components
   protected JPanel topPanelMenuNameButtons;
   protected JPanel buttonsPanel;
   protected JLabel panelTitleLabel;

   // -- Main Panel --
   protected JPanel mainPanel;
   
   // -- Left Panel component --
   protected InfoLabel infoLeftLabel;

   // -- Center Panel component
   protected JScrollPane contentCentralPanel;
   
   // === BUTTONS ===
   protected JButton addButton;
   protected JButton removeButton;
   protected JButton editButton;
   protected JButton searchButton;

   // === EXTERNAL CONTROL ===
   JPanel externalCardPanel;

   public AbstractEntityMenuPanel(
      BancoDeDados db,
      String panelTitle,
      Persistente<T> persistency,
      JPanel externalCardPanel,
      Gui gui
   ){
      super(new BorderLayout());
      
      this.gui = gui;
      this.db = db;
      this.persistency = persistency;
      this.infoLeftLabel = new InfoLabel();
      this.externalCardPanel = externalCardPanel;

      
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
      topPanelMenuNameButtons = new JPanel();
      topPanelMenuNameButtons.setLayout(new BoxLayout(topPanelMenuNameButtons, BoxLayout.Y_AXIS));
      
      panelTitleLabel = new JLabel(panelTitle);
      topPanelMenuNameButtons.add(panelTitleLabel);
      
      JPanel toLeftButtonsPanel = new JPanel(new BorderLayout());
      toLeftButtonsPanel.add(buttonsPanel, BorderLayout.WEST);
      topPanelMenuNameButtons.add(toLeftButtonsPanel);
      
      add(topPanelMenuNameButtons, BorderLayout.NORTH);
      
      // === CRIAÇÃO DO PAINEL CENTRAL ===
      mainPanel = new JPanel(new BorderLayout());
      add(mainPanel);

      mainPanel.add(infoLeftLabel, BorderLayout.WEST);
      
      createCentralPanel();
      mainPanel.add(contentCentralPanel);
   }
   
   
   /**Finds any entity of type {@link T} by ID.  */
   protected void searchAction(){
      int id = auxiliar.Input.getIntDialogue(this, "Insira o ID a buscar");
      
      try{
          T entityFound = persistency.findById(id);
          JOptionPane.showMessageDialog(this, "Entidade: " + entityFound);
      } catch(persistencia.InexistentIdException e){
         JOptionPane.showMessageDialog(this, "ID inexistente.");
      }
   }
   
   protected abstract void addAction();
   protected abstract void removeAction();
   protected abstract void editAction();

   protected abstract void updatePanel();
   protected abstract void updateLeftLabel();

   protected abstract void createCentralPanel();
}
