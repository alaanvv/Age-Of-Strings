package Interface;

import java.awt.*;
import javax.swing.*;

public class MenuManager {

    private final JFrame frame;
    private MenuBase currentMenu;

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // UM ÚNICO Buttons para TUDO
    public final Buttons buttons = new Buttons();
    // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public MenuManager(JFrame frame) {
        this.frame = frame;
        frame.setLayout(new BorderLayout());
    }

    public void switchMenu(MenuBase newMenu) {
        this.currentMenu = newMenu;

        // garante que cada menu usa sempre o mesmo buttons
        newMenu.setButtons(buttons);

        newMenu.render();

        frame.getContentPane().removeAll();
        frame.add(newMenu.getTopPanel(), BorderLayout.NORTH);
        frame.add(new JScrollPane(newMenu.getCenterPanel()), BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    public Buttons getButtons() {
        return buttons;
    }
}
