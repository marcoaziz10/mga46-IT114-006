package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName = "Unknown";
    private Room currentRoom;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Get client name
            clientName = in.readLine();

            // Place client in Lobby by default
            currentRoom = Server.getRoom("Lobby");
            if (currentRoom != null) {
                currentRoom.join(this);
            }

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.startsWith("/join ")) {
                    String roomName = msg.substring(6).trim();
                    Room newRoom = Server.getRoom(roomName);
                    if (newRoom != null) {
                        if (currentRoom != null) currentRoom.leave(this);
                        currentRoom = newRoom;
                        currentRoom.join(this);
                    } else {
                        send("[SERVER] Room does not exist.");
                    }

                } else if (msg.startsWith("/create ")) {
                    String roomName = msg.substring(8).trim();
                    Room newRoom = Server.createRoom(roomName);
                    if (currentRoom != null) currentRoom.leave(this);
                    currentRoom = newRoom;
                    currentRoom.join(this);

                } else {
                    if (currentRoom != null) {
                        currentRoom.broadcast(clientName + ": " + msg);
                    } else {
                        send("[SERVER] You must join a room first.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("[SERVER] Error with client " + clientName + ": " + e.getMessage());
        } finally {
            Server.removeClient(this);
            if (currentRoom != null) {
                currentRoom.leave(this);
            }
            System.out.println("[SERVER] " + clientName + " has left the room.");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[SERVER] Error closing socket for " + clientName);
            }
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    public String getClientName() {
        return clientName;
    }

    private void broadcast(String msg) {
        if (currentRoom != null) {
            currentRoom.broadcast(msg);
        }
    }
}
