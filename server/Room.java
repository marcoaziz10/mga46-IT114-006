package server;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private String name;
    private Set<ServerThread> clients = new HashSet<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public synchronized void join(ServerThread client) {
        clients.add(client);
        broadcast(client.getClientName() + " joined the room.");
    }

    public synchronized void leave(ServerThread client) {
        clients.remove(client);
        broadcast(client.getClientName() + " left the room.");
    }

    public synchronized void broadcast(String message) {
        for (ServerThread client : clients) {
            client.send(message);
        }
    }
}

