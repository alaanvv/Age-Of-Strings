package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.BancoDeDados;

import modelo.Empire;

public class Gui extends JFrame{

   public BancoDeDados db;
   
   private modelo.Empire empireViewing;
   
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
   private final EmpireMenuPanel empireMenu;
   
   

   public Gui(BancoDeDados db){
      
      // === ATRIBUTES SETUP ===
      this.db = db;
      cardLayout = new CardLayout();
      menusPanel = new JPanel(cardLayout);
      empireMenu = new EmpireMenuPanel(db, db.getEmpires(), "Menu Império", menusPanel, this);
      empireMenu.updatePanel();

      // === CARD LAYOUT PREP ===
      cardLayout.addLayoutComponent(empireMenu, "empireMenu");
      menusPanel.add(empireMenu, "empireMenu");

      // === WINDOW SETUP ===
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setContentPane(menusPanel);
      setSize(1150, 800);
      setLocationRelativeTo(null);
      
      cardLayout.show(menusPanel, "empireMenu");
      setVisible(true);
   }

   // === MENU TRANSITION METHODS ===
   public void switchToMenuEmpire(){
      
      empireMenu.updatePanel();
      cardLayout.show(menusPanel, "empireMenu");
   }

   public void switchToMenuManageEmpire(Empire empire){
      empireViewing = empire;
   }

}