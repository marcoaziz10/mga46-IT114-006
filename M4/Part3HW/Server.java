package M4.Part3HW;

// UCID: mga46
// Date: April 5, 2025

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private int port = 3000;
    private final ConcurrentHashMap<Long, ServerThread> connectedClients = new ConcurrentHashMap<>();
    private boolean isRunning = true;

    private void start(int port) {
        this.port = port;
        System.out.println("Listening on port " + this.port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (isRunning) {
                System.out.println("Waiting for next client");
                Socket incomingClient = serverSocket.accept(); // blocking action
                System.out.println("Client connected");
                ServerThread serverThread = new ServerThread(incomingClient, this, this::onServerThreadInitialized);
                serverThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("Closing server socket");
        }
    }

    private void onServerThreadInitialized(ServerThread serverThread) {
        connectedClients.put(serverThread.getClientId(), serverThread);
        relay(null, String.format("*User[%s] connected*", serverThread.getClientId()));
    }

    private synchronized void disconnect(ServerThread serverThread) {
        serverThread.disconnect();
        ServerThread removed = connectedClients.remove(serverThread.getClientId());
        if (removed != null) {
            relay(null, "User[" + removed.getClientId() + "] disconnected");
        }
    }

    private synchronized void relay(ServerThread sender, String message) {
        final String senderString = sender == null ? "Server" : String.format("User[%s]", sender.getClientId());
        final String formattedMessage = String.format("%s: %s", senderString, message);

        connectedClients.values().removeIf(client -> {
            boolean failed = !client.sendToClient(formattedMessage);
            if (failed) {
                System.out.println("Removing disconnected client[" + client.getClientId() + "]");
                disconnect(client);
            }
            return failed;
        });
    }

    protected synchronized void handleDisconnect(ServerThread sender) {
        disconnect(sender);
    }

    protected synchronized void handleReverseText(ServerThread sender, String text) {
        StringBuilder sb = new StringBuilder(text).reverse();
        relay(sender, sb.toString());
    }

    protected synchronized void handleMessage(ServerThread sender, String text) {
        relay(sender, text);
    }

    protected synchronized void handleFlipCoin(ServerThread sender) {
        String result = Math.random() < 0.5 ? "Heads" : "Tails";
        relay(sender, sender.getClientId() + " flipped a coin and got " + result);
    }

    protected synchronized void handleShuffleText(ServerThread sender, String text) {
        List<String> words = Arrays.asList(text.split(" "));
        Collections.shuffle(words);
        String shuffled = String.join(" ", words);
        relay(sender, String.format("Shuffled from %s: %s", sender.getClientId(), shuffled));
    }

    protected synchronized void handlePrivateMessage(ServerThread sender, long targetId, String message) {
        ServerThread receiver = connectedClients.get(targetId);
        if (receiver != null) {
            receiver.sendToClient("PM from " + sender.getClientId() + ": " + message);
            sender.sendToClient("PM to " + targetId + ": " + message);
        } else {
            sender.sendToClient("User " + targetId + " not found.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // use default
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}
