package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.BancoDeDados;

import modelo.Empire;

public class Gui extends JFrame{

   public BancoDeDados db;
   
   private modelo.Empire viewingEmpire;

   // === CARD LAYOUT ===
   /** Panel names: <p>
    * - empireMenu <p>
    * - empireManagementMenu <p>
    * - farmMenu <p>
    * - mineMenu <p>
    * - armyMenu <p>
    * - battleMenu <p>
      * - lumberMenu <p>
    */
   private final CardLayout cardLayout;
   private final JPanel menusPanel;
   private final EmpireMenuPanel empireMenu;
   private final EmpireManagementMenu empireManagementMenu;
      private final FarmMenuPanel farmMenu;
      private final MineMenuPanel mineMenu;
      private final ArmyMenuPanel armyMenu;
      private final BattleMenuPanel battleMenu;
      private final LumberMenuPanel lumberMenu;
   
   

   public Gui(BancoDeDados db){
      
      // === ATRIBUTES SETUP ===
      this.db = db;
      cardLayout = new CardLayout();
      menusPanel = new JPanel(cardLayout);

      empireMenu = new EmpireMenuPanel(db, db.getEmpires(), menusPanel, this);
      empireMenu.updatePanel();

      empireManagementMenu = new EmpireManagementMenu(this);
      farmMenu = new FarmMenuPanel(db, db.getFarms(), menusPanel, this);
      mineMenu = new MineMenuPanel(db, db.getMines(), menusPanel, this);
      armyMenu = new ArmyMenuPanel(db, db.getArmies(), menusPanel, this);
      battleMenu = new BattleMenuPanel(db, db.getBattles(), menusPanel, this);
      lumberMenu = new LumberMenuPanel(db, db.getLumbers(), menusPanel, this);

      // === CARD LAYOUT PREP ===
      cardLayout.addLayoutComponent(empireMenu, "empireMenu");
      menusPanel.add(empireMenu, "empireMenu");
      cardLayout.addLayoutComponent(empireManagementMenu, "empireManagementMenu");
      menusPanel.add(empireManagementMenu, "empireManagementMenu");
      cardLayout.addLayoutComponent(farmMenu, "farmMenu");
      menusPanel.add(farmMenu, "farmMenu");
      cardLayout.addLayoutComponent(mineMenu, "mineMenu");
      menusPanel.add(mineMenu, "mineMenu");
      cardLayout.addLayoutComponent(armyMenu, "armyMenu");
      menusPanel.add(armyMenu, "armyMenu");
      cardLayout.addLayoutComponent(battleMenu, "battleMenu");
      menusPanel.add(battleMenu, "battleMenu");
      cardLayout.addLayoutComponent(lumberMenu, "lumberMenu");
      menusPanel.add(lumberMenu, "lumberMenu");


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
      viewingEmpire = empire;

      empireManagementMenu.updatePanel();
      cardLayout.show(menusPanel, "empireManagementMenu");
   }

   public void switchToFarmMenu(){
      farmMenu.updatePanel();
      cardLayout.show(menusPanel, "farmMenu");
   }

   public void switchToMineMenu(){
      mineMenu.updatePanel();
      cardLayout.show(menusPanel, "mineMenu");
   }

   public void switchToArmyMenu(){
      armyMenu.updatePanel();
      cardLayout.show(menusPanel, "armyMenu");
   }

   public void switchToBattleMenu(){
      battleMenu.updatePanel();
      cardLayout.show(menusPanel, "battleMenu");
   }

   public void switchToLumberMenu(){
      lumberMenu.updatePanel();
      cardLayout.show(menusPanel, "lumberMenu");
   }

   public Empire getViewingEmpire(){
      return viewingEmpire;
   }

}