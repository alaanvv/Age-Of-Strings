package guiandrew;

import javax.swing.*;
import java.awt.*;


public class Frame extends JFrame{

   CardLayout cardLayout;
   JPanel menusPanel;
   

   public Frame(){
      cardLayout = new CardLayout();
      menusPanel = new JPanel(cardLayout);
   }

}