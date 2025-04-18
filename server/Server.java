
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 12345;
    private static ArrayList<ServerThread> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("[SERVER] Starting server on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] New client connected: " + clientSocket);
                ServerThread clientThread = new ServerThread(clientSocket);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("[SERVER] Error: " + e.getMessage());
        }
    }

    public static ArrayList<ServerThread> getClients() {
        return clients;
    }

    public static void removeClient(ServerThread client) {
        clients.remove(client);
    }
}

