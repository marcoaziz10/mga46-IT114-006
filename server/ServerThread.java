// start edits - mga46
package server;

import java.io.*;
import java.net.Socket;
import common.Payload;
import common.PayloadType;

public class ServerThread extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String clientName = "Unknown";
    private Room currentRoom;
    private boolean isSpectator = false;
    private boolean isAway = false;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Object obj;
            while ((obj = in.readObject()) != null) {
                if (obj instanceof Payload payload) {
                    handlePayload(payload);
                }
            }
        } catch (Exception e) {
            System.out.println("[SERVER] Disconnected: " + clientName);
        } finally {
            cleanup();
        }
    }

    private void handlePayload(Payload payload) {
        switch (payload.getPayloadType()) {
            case CONNECT -> {
                this.clientName = payload.getClientId();
                send("[SERVER] Welcome, " + clientName + "!");
            }

            case JOIN -> {
                String roomName = payload.getMessage().trim();
                Room targetRoom = Server.getRoom(roomName);
                if (targetRoom == null) {
                    targetRoom = new GameRoom(roomName);
                    Server.addRoom(roomName, targetRoom);
                    send("[SERVER] Room created: " + roomName);
                } else {
                    send("[SERVER] Joining room: " + roomName);
                }

                if (currentRoom != null) {
                    currentRoom.leave(this);
                }
                currentRoom = targetRoom;
                currentRoom.join(this);
            }

            case READY -> {
                if (currentRoom instanceof GameRoom gr) {
                    gr.setPlayerReady(this);
                }
            }

            case PICK -> {
                if (currentRoom instanceof GameRoom gr && !isSpectator && !isAway) {
                    gr.playerPick(this, payload.getMessage());
                } else {
                    send("[SERVER] You are away or a spectator and cannot pick.");
                }
            }

            case COMMAND -> {
                String cmd = payload.getMessage().toLowerCase();
                switch (cmd) {
                    case "/away" -> {
                        isAway = true;
                        send("[SERVER] You are now marked as AWAY.");
                    }
                    case "/back" -> {
                        isAway = false;
                        send("[SERVER] You are no longer away.");
                    }
                    case "/spectator" -> {
                        isSpectator = true;
                        send("[SERVER] You are now a SPECTATOR.");
                    }
                    default -> send("[SERVER] Unknown command.");
                }
            }
        }
    }

    public void send(String msg) {
        sendPayload(new Payload(PayloadType.MESSAGE, "SERVER", msg));
    }

    public void sendPayload(Payload payload) {
        try {
            out.writeObject(payload);
            out.flush();
        } catch (IOException e) {
            System.out.println("[SERVER] Error sending to " + clientName);
        }
    }

    public String getClientName() {
        return clientName;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public boolean isAway() {
        return isAway;
    }

    private void cleanup() {
        if (currentRoom != null) {
            currentRoom.leave(this);
        }
        Server.removeClient(this);
        System.out.println("[SERVER] " + clientName + " disconnected.");
    }
}
// stop edits - mga46
