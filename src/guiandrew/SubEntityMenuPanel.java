package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.*;

/* Modelo do painel:
            \center nome do menu - nome do império
\left botões add, remover, editar, buscar

\center.fill tabela com cada subentidade e suas informações
*/

public abstract class SubEntityMenuPanel extends JPanel{
   
   Persistente subEntityPersistency;
   JPanel topButtonsPanel;
   JLabel infoLeftPanel;
   JScrollPane contentCenterPanel;

   public SubEntityMenuPanel(String subentityName, Persistente persistency){
      super(new BorderLayout());
   }
   
}
