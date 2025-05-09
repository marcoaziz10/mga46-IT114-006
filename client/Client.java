// start edits - mga46
package client;

import common.Payload;
import common.PayloadType;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private GameUI ui;
    private String name;

    public Client(GameUI ui) {
        this.ui = ui;
    }

    public void connect(String host, int port, String name) {
        this.name = name;
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            new Thread(new ServerListener(socket, ui)).start();

            Payload connectPayload = new Payload(PayloadType.CONNECT, name, null);
            sendPayload(connectPayload);

            ui.appendToGameLog("[CLIENT] Connected to server.");
        } catch (Exception e) {
            ui.appendToGameLog("[CLIENT] Failed to connect: " + e.getMessage());
        }
    }

    public void sendJoin(String roomName) {
        sendPayload(new Payload(PayloadType.JOIN, name, roomName));
    }

    public void sendReady() {
        sendPayload(new Payload(PayloadType.READY, name, null));
    }

    public void sendPick(String choice) {
        sendPayload(new Payload(PayloadType.PICK, name, choice));
    }

    public void sendMessage(String message) {
        sendPayload(new Payload(PayloadType.MESSAGE, name, message));
    }

    private void sendPayload(Payload payload) {
        try {
            if (out != null) {
                out.writeObject(payload);
                out.flush();
            } else {
                ui.appendToGameLog("[CLIENT] Output stream not initialized.");
            }
        } catch (Exception e) {
            ui.appendToGameLog("[CLIENT] Error sending message: " + e.getMessage());
        }
    }
}
// stop edits - mga46
