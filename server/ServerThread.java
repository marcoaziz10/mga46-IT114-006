package server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName = "Unknown";

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clientName = in.readLine(); // First message is the name
            broadcast("[SERVER] " + clientName + " has joined.");

            String msg;
            while ((msg = in.readLine()) != null) {
                broadcast(msg);
            }
        } catch (IOException e) {
            System.out.println("[SERVER] Client disconnected: " + clientName);
        } finally {
            Server.removeClient(this);
            broadcast("[SERVER] " + clientName + " has left.");
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[SERVER] Socket close error");
            }
        }
    }

    public void broadcast(String msg) {
        for (ServerThread client : Server.getClients()) {
            client.send(msg);
        }
    }

    public void send(String msg) {
        out.println(msg);
    }
}

