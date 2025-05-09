// start edits - mga46
package client;

import javax.swing.*;
import java.awt.*;

public class GameUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    // Panels
    private JPanel connectPanel;
    private JPanel roomPanel;
    private JPanel readyPanel;
    private GamePanel gamePanel;

    // Connect fields
    private JTextField usernameField;
    private JTextField hostField;
    private JTextField portField;
    private JButton connectButton;

    // Room fields
    private JTextField roomNameField;
    private JButton joinRoomButton;

    // Ready
    private JButton readyButton;

    private Client client;

    public GameUI() {
        setTitle("RPS Multiplayer Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);

        initConnectPanel();
        initRoomPanel();
        initReadyPanel();
        initGamePanel();

        add(mainPanel);
        setVisible(true);
    }

    private void initConnectPanel() {
        connectPanel = new JPanel(new GridLayout(4, 2));
        usernameField = new JTextField();
        hostField = new JTextField("localhost");
        portField = new JTextField("12345");
        connectButton = new JButton("Connect");

        connectPanel.add(new JLabel("Username:"));
        connectPanel.add(usernameField);
        connectPanel.add(new JLabel("Host:"));
        connectPanel.add(hostField);
        connectPanel.add(new JLabel("Port:"));
        connectPanel.add(portField);
        connectPanel.add(new JLabel());
        connectPanel.add(connectButton);

        connectButton.addActionListener(e -> {
            String name = usernameField.getText().trim();
            String host = hostField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            client = new Client(this);
            gamePanel.setClient(client); // Link game panel to client
            client.connect(host, port, name);
            showRoomPanel(); // Move to room panel after connect
        });

        mainPanel.add(connectPanel, "connect");
    }

    private void initRoomPanel() {
        roomPanel = new JPanel(new GridLayout(2, 1));
        roomNameField = new JTextField();
        joinRoomButton = new JButton("Join Room");

        roomPanel.add(new JLabel("Enter Room Name:"));
        roomPanel.add(roomNameField);
        roomPanel.add(joinRoomButton);

        joinRoomButton.addActionListener(e -> {
            String room = roomNameField.getText().trim();
            if (!room.isEmpty() && client != null) {
                client.sendJoin(room);
                showReadyPanel();
            }
        });

        mainPanel.add(roomPanel, "room");
    }

    private void initReadyPanel() {
        readyPanel = new JPanel();
        readyButton = new JButton("I'm Ready");

        readyPanel.add(new JLabel("Waiting to start..."));
        readyPanel.add(readyButton);

        readyButton.addActionListener(e -> {
            if (client != null) {
                client.sendReady();
                showGamePanel();
            }
        });

        mainPanel.add(readyPanel, "ready");
    }

    private void initGamePanel() {
        gamePanel = new GamePanel();
        mainPanel.add(gamePanel, "game");
    }

    public void showConnectPanel() {
        cardLayout.show(mainPanel, "connect");
    }

    public void showRoomPanel() {
        cardLayout.show(mainPanel, "room");
    }

    public void showReadyPanel() {
        cardLayout.show(mainPanel, "ready");
    }

    public void showGamePanel() {
        cardLayout.show(mainPanel, "game");
    }

    public void appendToGameLog(String msg) {
        if (gamePanel != null) {
            gamePanel.logEvent(msg);
        }
    }

    public void updateTimer(String value) {
        if (gamePanel != null) {
            gamePanel.updateTimer(value);
        }
    }

    public void updatePlayer(String name, int score, String status) {
        if (gamePanel != null) {
            gamePanel.updatePlayer(name, score, status);
        }
    }

    public void setClient(Client c) {
        if (gamePanel != null) {
            gamePanel.setClient(c);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameUI::new);
    }
}
// stop edits - mga46
