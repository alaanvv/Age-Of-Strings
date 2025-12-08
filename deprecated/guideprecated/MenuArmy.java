package guideprecated;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Map;
import java.util.function.IntConsumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import modelo.Army;
import modelo.Empire;
import persistencia.BancoDeDados;

/**
 * Menu dedicado a exércitos: criar, reforçar, melhorar armadura e remover
 * exércitos de um império.
 */
public class MenuArmy extends MenuBase {

	private final MenuManager manager;
	private final Empire empire;

	public MenuArmy(BancoDeDados db, MenuManager manager, Empire empire) {
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

		JButton newArmyButton = new JButton("New Army");
		newArmyButton.setBackground(btnColor);
		newArmyButton.setForeground(Color.WHITE);
		newArmyButton.addActionListener(e -> {
			int id = db.createArmy(empire.getId());
			if (id == 0) {
				JOptionPane.showMessageDialog(null, "Insufficient resources (Iron: 50, Gold: 20).");
			}
			manager.switchMenu(new MenuArmy(db, manager, empire));
		});
		topPanel.add(newArmyButton);

		renderEmpireSidebar();
		renderArmyCards();
	}

	private void renderEmpireSidebar() {
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
	}

	private void renderArmyCards() {
		centerPanel.setLayout(new GridLayout(0, 2, 10, 10));

		if (empire.getArmies().isEmpty()) {
			JLabel empty = new JLabel("No armies yet. Recruit troops to defend or attack.");
			empty.setForeground(Color.WHITE);
			centerPanel.add(empty);
			return;
		}

		for (Map.Entry<Integer, Army> entry : empire.getArmies().entrySet()) {
			Army army = entry.getValue();

			JPanel card = new JPanel();
			card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
			card.setBackground(BACKGROUND_COLOR);

			JLabel title = new JLabel(String.format("Army #%d", army.getId()));
			title.setForeground(Color.WHITE);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(title);

			String statsText = String.format("Soldiers: %d | Hiring cost: %d | Hiring level: %d", army.getSoldiersAmount(), army.getHiringCost(), army.getHiringLevel());
			JLabel stats = new JLabel(statsText);
			stats.setForeground(Color.LIGHT_GRAY);
			stats.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(stats);

			card.add(Box.createVerticalStrut(8));

			JButton sendBtn = new JButton("Send Soldiers");
			sendBtn.setBackground(btnColor);
			sendBtn.setForeground(Color.WHITE);
			sendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			sendBtn.addActionListener(e -> promptWorkers(
					"Workers to enlist in army " + army.getId(),
					amount -> {
						int sent = empire.sendWorkersToArmy(amount, army.getId());
						JOptionPane.showMessageDialog(null, sent + " workers became soldiers.");
						manager.switchMenu(new MenuArmy(db, manager, empire));
					}));
			card.add(sendBtn);

			JButton takeBtn = new JButton("Take Soldiers");
			takeBtn.setBackground(btnColor);
			takeBtn.setForeground(Color.WHITE);
			takeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			takeBtn.addActionListener(e -> promptWorkers(
					"Soldiers to dismiss from army " + army.getId(),
					amount -> {
						int taken = empire.takeWorkersFromArmy(amount, army.getId());
						JOptionPane.showMessageDialog(null, taken + " soldiers dismissed.");
						manager.switchMenu(new MenuArmy(db, manager, empire));
					}));
			card.add(takeBtn);

			JButton upgradeBtn = new JButton("Upgrade Armory");
			upgradeBtn.setBackground(btnColor);
			upgradeBtn.setForeground(Color.WHITE);
			upgradeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			upgradeBtn.addActionListener(e -> promptWorkers(
					"Armory levels to add (cost Iron 25, Gold 5 each)",
					amount -> {
						int added = army.upgradeArmory(amount, empire);
						String message = added > 0
								? String.format("Armory upgraded by %d level(s).", added)
								: "Insufficient resources for this upgrade.";
						JOptionPane.showMessageDialog(null, message);
						manager.switchMenu(new MenuArmy(db, manager, empire));
					}));
			card.add(upgradeBtn);

			JButton destroyBtn = new JButton("Disband Army");
			destroyBtn.setBackground(new Color(120, 40, 40));
			destroyBtn.setForeground(Color.WHITE);
			destroyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			destroyBtn.addActionListener(e -> {
				int op = JOptionPane.showConfirmDialog(null, "Disband army #" + army.getId() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION) {
					db.destroyEntity(army);
					manager.switchMenu(new MenuArmy(db, manager, empire));
				}
			});
			card.add(destroyBtn);

			centerPanel.add(card);
		}
	}

	private void promptWorkers(String title, IntConsumer onConfirm) {
		String input = JOptionPane.showInputDialog(null, title);
		if (input == null) return;
		try {
			int amount = Integer.parseInt(input.trim());
			if (amount < 0) {
				JOptionPane.showMessageDialog(null, "Please enter a positive number.");
				return;
			}
			onConfirm.accept(amount);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Invalid number!");
		}
	}
}