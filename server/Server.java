package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    private static ArrayList<ServerThread> clients = new ArrayList<>();

    // Room management
    private static ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    static {
        rooms.put("Lobby", new Room("Lobby")); // default room
    }

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

    // Room management methods
    public static Room getRoom(String name) {
        return rooms.get(name);
    }

    public static Room createRoom(String name) {
        Room room = new Room(name);
        rooms.put(name, room);
        return room;
    }

    // NEW: Used to add a custom Room like GameRoom
    public static void addRoom(String name, Room room) {
        rooms.put(name, room);
    }
}
