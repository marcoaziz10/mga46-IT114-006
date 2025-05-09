// start edits - mga46
package server;

import java.util.HashSet;
import java.util.Set;
import common.Payload;
import common.PayloadType;

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

    // Sends a text message to all clients
    public synchronized void broadcast(String message) {
        for (ServerThread client : clients) {
            client.sendPayload(new Payload(PayloadType.MESSAGE, "SERVER", message));
        }
    }

    // Sends a Payload object to all clients
    public synchronized void broadcastPayload(Payload payload) {
        for (ServerThread client : clients) {
            client.sendPayload(payload);
        }
    }

    public String getName() {
        return name;
    }

    public void onClientAdded(ServerThread client) {
        // Can be overridden in GameRoom
    }

    public void onClientRemoved(ServerThread client) {
        // Can be overridden in GameRoom
    }
}
// stop edits - mga46
