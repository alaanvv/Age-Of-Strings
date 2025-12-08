package guideprecated;

import java.awt.*;
import javax.swing.*;

/**
 * MenuManager centraliza a troca de telas do GUI.
 * Mantem uma unica instancia de botoes compartilhada, injeta-a em cada menu
 * concreto, e recompõe o JFrame sempre que um novo MenuBase é ativado.
 */
public class MenuManager {

    // Frame raiz fornecido pela aplicação principal; usado para ancorar os painéis
    private final JFrame frame;
    // Referência do menu atualmente ativo, útil para futuro acesso/consulta
    private MenuBase currentMenu;

    // Botoes reutilizáveis: evita recriar listeners e objetos entre trocas de menu
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
        // Guarda referência para depuração e potencial recuperação futura
        this.currentMenu = newMenu;
        
        // Compartilha a instância de Buttons para preservar estado e listeners
        newMenu.setButtons(buttons);

        // Permite que o menu construa seus painéis antes de inseri-los no frame
        newMenu.render();

        // Limpa o conteúdo anterior antes de adicionar os novos painéis
        frame.getContentPane().removeAll();


        frame.add(newMenu.getTopPanel(), BorderLayout.NORTH);
        frame.add(new JScrollPane(newMenu.getCenterPanel()), BorderLayout.CENTER);
        frame.add(new JScrollPane(newMenu.getLeftPanel()), BorderLayout.WEST);
        

        frame.revalidate();
        frame.repaint();
    }
    
    // ----- getter buttons -----
    public Buttons getButtons() { return buttons; }

    // Exposto para consultas externas sem comprometer encapsulamento
    public MenuBase getCurrentMenu() { return currentMenu; }
}
