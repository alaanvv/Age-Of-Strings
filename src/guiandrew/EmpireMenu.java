package guiandrew;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import persistencia.BancoDeDados;

public class EmpireMenu extends EntityMenuPanel<modelo.Empire>{
   
   public EmpireMenu(BancoDeDados db, JLabel leftLabel, String panelTitle){
      super(db, leftLabel, panelTitle, db.getEmpires());

   }
}
