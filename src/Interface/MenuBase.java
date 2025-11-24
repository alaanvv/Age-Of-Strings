package Interface;

import java.awt.*;
import javax.swing.*;

public abstract class MenuBase {

    protected JPanel topPanel;
    protected JPanel centerPanel;
    protected JPanel leftPanel;
    protected Color bgColor = new Color(25,30,35);
    protected Color btnColor = new Color(50,50,50);

    protected Buttons buttons;

    public MenuBase() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(bgColor);

        centerPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        centerPanel.setBackground(bgColor);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(bgColor);
        leftPanel.setPreferredSize(new Dimension(200, 0)); 
    }

    public void setButtons(Buttons buttons) {
        this.buttons = buttons;
    }

    public JPanel getTopPanel()  { return topPanel; }
    public JPanel getCenterPanel() { return centerPanel; }
    public JPanel getLeftPanel() { return leftPanel; }

    // Cada menu vai implementar isto
    public abstract void render();
}
