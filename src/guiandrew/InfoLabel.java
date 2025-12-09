package guiandrew;

import javax.swing.*;
import java.awt.*;

public class InfoLabel extends JLabel{
   
   private static final int WIDTH = 200;
   private static final int HEIGHT = 0;

   public InfoLabel(){
      super();
      setLabelConfig();
   }

   public InfoLabel(String text){
      super(text);
      setLabelConfig();
   }

   private void setLabelConfig(){
      setPreferredSize(new Dimension(WIDTH, HEIGHT));
      setMinimumSize(new Dimension(WIDTH, 0)); // permite encolher verticalmente
      setMaximumSize(new Dimension(WIDTH, Integer.MAX_VALUE)); // permite crescer
      setHorizontalAlignment(SwingConstants.CENTER);
      setVerticalAlignment(SwingConstants.CENTER);
      setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
   }
}
