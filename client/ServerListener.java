// start edits - mga46
package client;

import common.*;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener extends Thread {
    private Socket socket;
    private ObjectInputStream input;
    private GameUI gameUI;

    public ServerListener(Socket socket, GameUI gameUI) {
        this.socket = socket;
        this.gameUI = gameUI;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            Payload payload;
            while ((payload = (Payload) input.readObject()) != null) {
                switch (payload.getPayloadType()) {
                    case CONNECT:
                        gameUI.appendToGameLog("[SERVER] " + payload.getMessage());
                        break;
                    case MESSAGE:
                        gameUI.appendToGameLog(payload.getMessage());
                        break;
                    case POINTS:
                        if (payload instanceof PointsPayload pp) {
                            String name = pp.getClientId();
                            int score = pp.getPoints();
                            String status = pp.getStatus().name().toLowerCase();
                            gameUI.updatePlayer(name, score, status);
                        }
                        break;
                    case TIMER:
                        gameUI.updateTimer(payload.getMessage());
                        break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Disconnected from server.");
        }
    }
}
// stop edits - mga46
