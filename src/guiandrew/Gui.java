package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.BancoDeDados;

import modelo.Empire;

public class Gui extends JFrame{

   public BancoDeDados db;

   // === CARD LAYOUT ===
   /** Panel names: <p>
    * - empireMenu <p>
    * - ManageEmpireMenu <p>
    * - farmMenu <p>
    * - mineMenu <p>
    * - armyMenu <p>
    * - battleMenu <p>
    */
   private final CardLayout cardLayout;
   private final JPanel menusPanel;
   private final EmpireMenu empireMenu;
   

   
   private modelo.Empire empireViewing;
   

   public Gui(BancoDeDados db){
      
      // === ATRIBUTES SETUP ===
      this.db = db;
      cardLayout = new CardLayout();
      menusPanel = new JPanel(cardLayout);
      empireMenu = new EmpireMenu(db, db.getEmpires(), new JLabel("Loading..."), "Menu Império", menusPanel, this);
      empireMenu.updatePanel();

      // === CARD LAYOUT PREP ===
      cardLayout.addLayoutComponent(empireMenu, "empireMenu");
      menusPanel.add(empireMenu, "empireMenu");

      // === WINDOW SETUP ===
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setContentPane(menusPanel);
      setSize(800, 600);
      setLocationRelativeTo(null);
      
      cardLayout.show(menusPanel, "empireMenu");
      setVisible(true);
   }

   // === MENU TRANSITION METHODS ===
   public void switchToMenuEmpire(){
      
      JLabel leftEmpireMenuLabel = new JLabel();

      String labelText = "Bem-vindo ao <b>Age of Strings!</b><p>Quantidade de impérios: " + db.getEmpires().getSize() + ".";
      leftEmpireMenuLabel.setText(labelText);
   }

   public void switchToMenuManageEmpire(Empire empire){
      empireViewing = empire;

      
   }

}