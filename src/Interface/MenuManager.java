package Interface;

import java.awt.*;
import javax.swing.*;

public class MenuManager {

    private final JFrame frame;
    private MenuBase currentMenu;

    private final Buttons buttons = new Buttons();
 


    // -----------------------------------------------------------------------------------
    // -------------------------- Construtor menu manager --------------------------------
    // -----------------------------------------------------------------------------------
    public MenuManager(JFrame frame) {
        this.frame = frame;
        frame.setLayout(new BorderLayout());
    }

    // -----------------------------------------------------------------------------------
    // ------------------------ Controla a trocas de menus -------------------------------
    // -----------------------------------------------------------------------------------

    public void switchMenu(MenuBase newMenu) {
        this.currentMenu = newMenu;
        
        newMenu.setButtons(buttons);

        newMenu.render();

        frame.getContentPane().removeAll();


        frame.add(newMenu.getTopPanel(), BorderLayout.NORTH);
        frame.add(new JScrollPane(newMenu.getCenterPanel()), BorderLayout.CENTER);
        frame.add(new JScrollPane(newMenu.getLeftPanel()), BorderLayout.WEST);
        

        frame.revalidate();
        frame.repaint();
    }
    
    // ----- getter buttons -----
    public Buttons getButtons() { return buttons; }
}
