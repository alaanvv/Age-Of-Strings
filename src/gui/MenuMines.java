package gui;

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

import modelo.Empire;
import modelo.Mine;
import persistencia.BancoDeDados;

/**
 * Menu de gerenciamento de minas: permite construir novas minas e distribuir
 * trabalhadores entre elas.
 */
public class MenuMines extends MenuBase {

	private final MenuManager manager;
	private final Empire empire;

	public MenuMines(BancoDeDados db, MenuManager manager, Empire empire) {
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

		JButton buildMineButton = new JButton("Build Mine");
		buildMineButton.setBackground(btnColor);
		buildMineButton.setForeground(Color.WHITE);
		buildMineButton.addActionListener(e -> {
			int id = db.createMine(empire.getId());
			if (id == 0) {
				JOptionPane.showMessageDialog(null, "Insufficient resources (Wood: 15, Gold: 5).");
			}
			manager.switchMenu(new MenuMines(db, manager, empire));
		});
		topPanel.add(buildMineButton);

		renderEmpireSidebar();
		renderMineCards();
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

		if (empire.getMines().isEmpty()) return;

		leftPanel.add(Box.createVerticalStrut(10));
		JLabel minesHeader = new JLabel("Mines:");
		minesHeader.setForeground(Color.WHITE);
		minesHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		leftPanel.add(minesHeader);

		for (Mine mine : empire.getMines().values()) {
			JLabel mineLabel = new JLabel(mine.toString());
			mineLabel.setForeground(Color.LIGHT_GRAY);
			mineLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			leftPanel.add(mineLabel);
		}
	}

	private void renderMineCards() {
		centerPanel.setLayout(new GridLayout(0, 2, 10, 10));

		if (empire.getMines().isEmpty()) {
			JLabel empty = new JLabel("No mines yet. Build one to gather iron and gold.");
			empty.setForeground(Color.WHITE);
			centerPanel.add(empty);
			return;
		}

		for (Map.Entry<Integer, Mine> entry : empire.getMines().entrySet()) {
			Mine mine = entry.getValue();
			JPanel card = new JPanel();
			card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
			card.setBackground(BACKGROUND_COLOR);

			JLabel title = new JLabel(String.format("Mine #%d", mine.getId()));
			title.setForeground(Color.WHITE);
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(title);

			JLabel workers = new JLabel(String.format("Workers: %d/20", mine.getWorkers()));
			workers.setForeground(Color.LIGHT_GRAY);
			workers.setAlignmentX(Component.CENTER_ALIGNMENT);
			card.add(workers);

			card.add(Box.createVerticalStrut(8));

			JButton sendBtn = new JButton("Send Workers");
			sendBtn.setBackground(btnColor);
			sendBtn.setForeground(Color.WHITE);
			sendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			sendBtn.addActionListener(e -> promptWorkers(
					"Workers to send to mine " + mine.getId(),
					amount -> {
						int sent = empire.sendWorkersToMine(amount, mine.getId());
						JOptionPane.showMessageDialog(null, sent + " workers sent.");
						manager.switchMenu(new MenuMines(db, manager, empire));
					}));
			card.add(sendBtn);

			JButton takeBtn = new JButton("Take Workers");
			takeBtn.setBackground(btnColor);
			takeBtn.setForeground(Color.WHITE);
			takeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			takeBtn.addActionListener(e -> promptWorkers(
					"Workers to take from mine " + mine.getId(),
					amount -> {
						int taken = empire.takeWorkersFromMine(amount, mine.getId());
						JOptionPane.showMessageDialog(null, taken + " workers taken.");
						manager.switchMenu(new MenuMines(db, manager, empire));
					}));
			card.add(takeBtn);

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
