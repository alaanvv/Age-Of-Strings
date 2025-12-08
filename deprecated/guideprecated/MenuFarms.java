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

import modelo.Farm;
import modelo.Empire;
import persistencia.BancoDeDados;

/**
 * Focused menu for farm management inside an empire.
 * Presents status and navigation back to the owning empire view; intended to
 * later host worker assignment controls.
 */
public class MenuFarms extends MenuBase {

    private final MenuManager manager;
    private final Empire empire;

    public MenuFarms(BancoDeDados db, MenuManager manager, Empire empire) {
        super(db);
        this.manager = manager;
        this.empire = empire;
    }   


    @Override
    public void render() {
        topPanel.removeAll();
        centerPanel.removeAll();

        // Botão de voltar sempre visível no topo
        JButton backButton = new JButton("Return");
        backButton.setBackground(btnColor);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> manager.switchMenu(new MenuEmpire(db, manager, empire)));
        topPanel.add(backButton);

        // Construção de novas fazendas
        JButton buildFarmButton = new JButton("Build Farm");
        buildFarmButton.setBackground(btnColor);
        buildFarmButton.setForeground(Color.WHITE);
        buildFarmButton.addActionListener(e -> {
            int id = db.createFarm(empire.getId());
            if (id == 0) {
                JOptionPane.showMessageDialog(null, "Insufficient resources (Wood: 5, Gold: 2).");
            }
            manager.switchMenu(new MenuFarms(db, manager, empire));
        });
        topPanel.add(buildFarmButton);

        // Painel lateral com status e resumo das fazendas
        renderEmpireSidebar();

        // Cartões de cada fazenda com ações de enviar/retirar trabalhadores
        renderFarmCards();
    }

    /**
     * Monta a coluna da esquerda com status do império e lista de fazendas.
     */
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

        if (empire.getFarms().isEmpty()) return;

        leftPanel.add(Box.createVerticalStrut(10));
        JLabel farmsHeader = new JLabel("Farms:");
        farmsHeader.setForeground(Color.WHITE);
        farmsHeader.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        leftPanel.add(farmsHeader);

        for (Farm farm : empire.getFarms().values()) {
            JLabel farmLabel = new JLabel(farm.toString());
            farmLabel.setForeground(Color.LIGHT_GRAY);
            farmLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            leftPanel.add(farmLabel);
        }
    }

    /**
     * Cria um cartão por fazenda com botões de enviar/retirar trabalhadores.
     */
    private void renderFarmCards() {
        centerPanel.setLayout(new GridLayout(0, 2, 10, 10));

        if (empire.getFarms().isEmpty()) {
            JLabel empty = new JLabel("No farms yet. Build one to start producing food.");
            empty.setForeground(Color.WHITE);
            centerPanel.add(empty);
            return;
        }

        for (Map.Entry<Integer, Farm> entry : empire.getFarms().entrySet()) {
            Farm farm = entry.getValue();
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(BACKGROUND_COLOR);

            JLabel title = new JLabel(String.format("Farm #%d", farm.getId()));
            title.setForeground(Color.WHITE);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(title);

            JLabel workers = new JLabel(String.format("Workers: %d/10", farm.getWorkers()));
            workers.setForeground(Color.LIGHT_GRAY);
            workers.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(workers);

            card.add(Box.createVerticalStrut(8));

            JButton sendBtn = new JButton("Send Workers");
            sendBtn.setBackground(btnColor);
            sendBtn.setForeground(Color.WHITE);
            sendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sendBtn.addActionListener(e -> promptWorkers(
                    "Workers to send to farm " + farm.getId(),
                    amount -> {
                        int sent = empire.sendWorkersToFarm(amount, farm.getId());
                        JOptionPane.showMessageDialog(null, sent + " workers sent.");
                        manager.switchMenu(new MenuFarms(db, manager, empire));
                    }));
            card.add(sendBtn);

            JButton takeBtn = new JButton("Take Workers");
            takeBtn.setBackground(btnColor);
            takeBtn.setForeground(Color.WHITE);
            takeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            takeBtn.addActionListener(e -> promptWorkers(
                    "Workers to take from farm " + farm.getId(),
                    amount -> {
                        int taken = empire.takeWorkersFromFarm(amount, farm.getId());
                        JOptionPane.showMessageDialog(null, taken + " workers taken.");
                        manager.switchMenu(new MenuFarms(db, manager, empire));
                    }));
            card.add(takeBtn);

            centerPanel.add(card);
        }
    }

    /**
     * Mostra um dialogo simples para obter um numero de trabalhadores.
     */
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
