package guiandrew;

import javax.swing.*;
import java.awt.*;
import persistencia.*;

/* Modelo do painel:
            \center nome do menu - nome do império
\left botões add, remover, editar, buscar

\center.fill tabela com cada subentidade e suas informações
*/

public abstract class SubEntityMenuPanel<T extends modelo.Entidade> extends JPanel{
   
   Persistente<T> subEntityPersistency;
   JPanel topButtonsPanel;
   JLabel infoLeftLabel;
   JScrollPane contentCenterPanel;

   // === BUTTONS ===
   JButton add;
   JButton remove;
   JButton edit;
   JButton search;

   public SubEntityMenuPanel(String subentityName, Persistente<T> persistency, JLabel infoLeftLabel){
      super(new BorderLayout());
      
      subEntityPersistency = persistency;

      this.infoLeftLabel = infoLeftLabel; //Label estático recebido na criação do painel.





   }
   
}
