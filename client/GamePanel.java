// start edits - mga46
package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import common.PlayerStatus;

public class GamePanel extends JPanel {
    private JTextArea eventArea;
    private JLabel timerLabel;
    private JPanel userListPanel;
    private JPanel pickPanel;
    private Map<String, PlayerStatus> playerStatuses = new HashMap<>();
    private Map<String, Integer> playerScores = new HashMap<>();
    private Map<String, JButton> pickButtons = new HashMap<>();
    private String lastPick = "";
    private Client client;

    public GamePanel() {
        setLayout(new BorderLayout());

        eventArea = new JTextArea();
        eventArea.setEditable(false);
        add(new JScrollPane(eventArea), BorderLayout.CENTER);

        // Timer label at the top
        timerLabel = new JLabel("Timer: 0");
        add(timerLabel, BorderLayout.NORTH);

        // Right-side player list panel
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(userListPanel);
        scroll.setPreferredSize(new Dimension(200, 0));
        add(scroll, BorderLayout.EAST);

        // Bottom pick button panel
        pickPanel = new JPanel();
        String[] picks = {"rock", "paper", "scissors", "lizard", "spock"};
        for (String pick : picks) {
            JButton btn = new JButton(cap(pick));
            btn.addActionListener(e -> handlePick(pick));
            pickButtons.put(pick, btn);
            pickPanel.add(btn);
        }
        add(pickPanel, BorderLayout.SOUTH);
    }

    private void handlePick(String pick) {
        if (pick.equals(lastPick)) {
            logEvent("[CLIENT] You can't pick the same option twice in a row.");
            return;
        }
        lastPick = pick;
        highlightPick(pick);
        if (client != null) {
            client.sendPick(pick);
            logEvent("[CLIENT] You picked: " + pick);
        }
    }

    private void highlightPick(String pick) {
        for (Map.Entry<String, JButton> entry : pickButtons.entrySet()) {
            entry.getValue().setBackground(entry.getKey().equals(pick) ? Color.LIGHT_GRAY : null);
        }
    }

    public void logEvent(String msg) {
        eventArea.append(msg + "\n");
    }

    public void updateTimer(String value) {
        timerLabel.setText("Timer: " + value);
    }

    public void updatePlayer(String name, int score, String status) {
        PlayerStatus st = PlayerStatus.fromString(status);
        playerStatuses.put(name, st);
        playerScores.put(name, score);
        refreshUserList();
    }

    private void refreshUserList() {
        userListPanel.removeAll();

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(playerScores.entrySet());
        sorted.sort((a, b) -> {
            int cmp = Integer.compare(b.getValue(), a.getValue());
            return cmp != 0 ? cmp : a.getKey().compareTo(b.getKey());
        });

        for (Map.Entry<String, Integer> entry : sorted) {
            String name = entry.getKey();
            int score = entry.getValue();
            PlayerStatus status = playerStatuses.getOrDefault(name, PlayerStatus.ACTIVE);

            JLabel lbl = new JLabel(name + " - " + score + " pts (" + status.name().toLowerCase() + ")");
            lbl.setForeground(getColorForStatus(status));
            userListPanel.add(lbl);
        }

        userListPanel.revalidate();
        userListPanel.repaint();
    }

    private Color getColorForStatus(PlayerStatus status) {
        return switch (status) {
            case ACTIVE -> Color.GREEN;
            case PENDING -> Color.YELLOW;
            case ELIMINATED -> Color.RED;
            case AWAY -> Color.GRAY;
            case SPECTATOR -> new Color(128, 0, 128); // Purple
        };
    }

    public void setClient(Client c) {
        this.client = c;
    }

    private String cap(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
// stop edits - mga46
