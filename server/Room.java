package server;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private String name;
    private Set<ServerThread> clients = new HashSet<>();

    public Room(String name) {
        this.name = name;
    }

    public synchronized void join(ServerThread client) {
        clients.add(client);
        onClientAdded(client);
    }

    public synchronized void leave(ServerThread client) {
        clients.remove(client);
        onClientRemoved(client);
    }

    public synchronized void broadcast(String message) {
        for (ServerThread client : clients) {
            client.send(message);
        }
    }

    public String getName() {
        return name;
    }

    public void onClientAdded(ServerThread client) {
    }

    public void onClientRemoved(ServerThread client) {
    }
}
