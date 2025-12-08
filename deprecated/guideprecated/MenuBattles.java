package guideprecated;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import modelo.Army;
import modelo.Battle;
import modelo.Empire;
import persistencia.BancoDeDados;

/**
 * Menu de batalhas: exibe guerras ativas e permite iniciar ou encerrar
 * confrontos usando os exércitos disponíveis.
 */
public class MenuBattles extends MenuBase {

	private final MenuManager manager;
	private final Empire empire;

	public MenuBattles(BancoDeDados db, MenuManager manager, Empire empire) {
		super(db);
		this.manager = manager;
		this.empire = empire;
	}

	@Override
	public void render() {
		topPanel.removeAll();
		centerPanel.removeAll();

		JButton backButton = new JButton("Return");
		backButton.setBackground(btnColor);
		backButton.setForeground(Color.WHITE);
		backButton.addActionListener(e -> manager.switchMenu(new MenuEmpire(db, manager, empire)));
		topPanel.add(backButton);

		JButton newBattleButton = new JButton("Start Battle");
		newBattleButton.setBackground(btnColor);
		newBattleButton.setForeground(Color.WHITE);
		newBattleButton.addActionListener(e -> promptNewBattle());
		topPanel.add(newBattleButton);

		renderSidebar();
		renderBattleCards();
	}

	private void renderSidebar() {
		leftPanel.removeAll();

		leftPanel.add(Box.createVerticalStrut(10));
		JLabel title = new JLabel("     Empire: " + empire.getName());
		title.setForeground(Color.WHITE);
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		leftPanel.add(title);

		leftPanel.add(Box.createVerticalStrut(10));
		for (String line : empire.statusReport().split("\n")) {
			JLabel lbl = new JLabel(line);
			lbl.setForeground(Color.WHITE);
			lbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			leftPanel.add(lbl);
			leftPanel.add(Box.createVerticalStrut(3));
		}

		if (!empire.getArmies().isEmpty()) {
			leftPanel.add(Box.createVerticalStrut(8));
			JLabel armiesHeader = new JLabel("Armies available:");
			armiesHeader.setForeground(Color.WHITE);
			armiesHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			leftPanel.add(armiesHeader);

			for (Army army : empire.getArmies().values()) {
				JLabel armyLabel = new JLabel(army.toString());
				armyLabel.setForeground(Color.LIGHT_GRAY);
				armyLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				leftPanel.add(armyLabel);
			}
		}
	}

	private void renderBattleCards() {
		centerPanel.setLayout(new GridLayout(0, 1, 10, 10));

		if (db.getBattles().getEntidades().isEmpty()) {
			JLabel empty = new JLabel("No active battles. Start one using two armies.");
			empty.setForeground(Color.WHITE);
			centerPanel.add(empty);
			return;
		}

		for (Map.Entry<Integer, modelo.Entidade> entry : db.getBattles().getEntidades().entrySet()) {
			Battle battle = (Battle) entry.getValue();

			JPanel card = new JPanel();
			card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
			card.setBackground(BACKGROUND_COLOR);

			JLabel title = new JLabel(String.format("Battle #%d", battle.getId()));
			title.setForeground(Color.WHITE);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(title);

			JLabel matchup = new JLabel(String.format("Attacker: Army #%d | Defender: Army #%d", battle.getAttacker().getId(), battle.getDefender().getId()));
			matchup.setForeground(Color.LIGHT_GRAY);
			matchup.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(matchup);

			JLabel alive = new JLabel(String.format("Alive - Attacker: %d | Defender: %d", battle.getAttackerSoldiersAlive(), battle.getDefenderSoldiersAlive()));
			alive.setForeground(Color.LIGHT_GRAY);
			alive.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(alive);

			JButton destroyBtn = new JButton("End Battle");
			destroyBtn.setBackground(new Color(120, 40, 40));
			destroyBtn.setForeground(Color.WHITE);
			destroyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			destroyBtn.addActionListener(e -> {
				int op = JOptionPane.showConfirmDialog(null, "End battle #" + battle.getId() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION) {
					db.destroyEntity(battle);
					manager.switchMenu(new MenuBattles(db, manager, empire));
				}
			});
			card.add(destroyBtn);

			centerPanel.add(card);
		}
	}

	/**
	 * Solicita os IDs das tropas e abre uma nova batalha se as validações passarem.
	 */
	private void promptNewBattle() {
		String atkId = JOptionPane.showInputDialog(null, "Attacker army id:");
		if (atkId == null) return;
		String defId = JOptionPane.showInputDialog(null, "Defender army id:");
		if (defId == null) return;

		try {
			int attackerId = Integer.parseInt(atkId.trim());
			int defenderId = Integer.parseInt(defId.trim());

			Army attacker = (Army) db.getArmies().findById(attackerId);
			Army defender = (Army) db.getArmies().findById(defenderId);

			if (attacker == null || attacker.getEmpireId() != empire.getId()) {
				JOptionPane.showMessageDialog(null, "Attacker must belong to your empire.");
				return;
			}
			if (defender == null || defender.getEmpireId() == empire.getId()) {
				JOptionPane.showMessageDialog(null, "Choose a defender from another empire.");
				return;
			}

			Battle battle = new Battle(attacker, defender, db.nextBattle());
			db.getBattles().insert(battle);
			JOptionPane.showMessageDialog(null, "Battle started!");
			manager.switchMenu(new MenuBattles(db, manager, empire));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid id number.");
		}
	}
}
