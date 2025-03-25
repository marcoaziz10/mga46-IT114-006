package M4.Part3HW;

// UCID: mga46
// Date: April 5, 2025

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import M4.Part3HW.TextFX.Color;

public class ServerThread extends Thread {
    private Socket client;
    private boolean isRunning = false;
    private ObjectOutputStream out;
    private Server server;
    private long clientId;
    private Consumer<ServerThread> onInitializationComplete;

    private void info(String message) {
        System.out.println(String.format("Thread[%s]: %s", this.getClientId(), message));
    }

    public boolean isRunning() {
        return isRunning;
    }

    protected ServerThread(Socket myClient, Server server, Consumer<ServerThread> onInitializationComplete) {
        Objects.requireNonNull(myClient, "Client socket cannot be null");
        Objects.requireNonNull(server, "Server cannot be null");
        Objects.requireNonNull(onInitializationComplete, "callback cannot be null");
        info("ServerThread created");
        this.client = myClient;
        this.server = server;
        this.clientId = this.threadId();
        this.onInitializationComplete = onInitializationComplete;
    }

    public long getClientId() {
        return this.clientId;
    }

    protected void disconnect() {
        if (!isRunning) return;
        info("Thread being disconnected by server");
        isRunning = false;
        this.interrupt();
        cleanup();
    }

    protected boolean sendToClient(String message) {
        if (!isRunning) return false;
        try {
            out.writeObject(message);
            out.flush();
            return true;
        } catch (IOException e) {
            info("Error sending message to client (most likely disconnected)");
            cleanup();
            return false;
        }
    }

    @Override
    public void run() {
        info("Thread starting");
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
            this.out = out;
            isRunning = true;
            onInitializationComplete.accept(this);
            String fromClient;
            while (isRunning) {
                try {
                    fromClient = (String) in.readObject();
                    if (fromClient == null) throw new IOException("Connection interrupted");
                    info(TextFX.colorize("Received from my client: " + fromClient, Color.CYAN));
                    processPayload(fromClient);
                } catch (ClassCastException | ClassNotFoundException cce) {
                    System.err.println("Error reading object: " + cce.getMessage());
                    cce.printStackTrace();
                } catch (IOException e) {
                    if (Thread.currentThread().isInterrupted()) {
                        info("Thread interrupted during read");
                        break;
                    }
                    info("IO exception while reading from client");
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            info("General Exception");
            e.printStackTrace();
            info("My Client disconnected");
        } finally {
            isRunning = false;
            info("Exited thread loop. Cleaning up connection");
            cleanup();
        }
    }

    private void processPayload(String incoming) {
        if (!processCommand(incoming)) {
            server.handleMessage(this, incoming);
        }
    }

    private boolean processCommand(String message) {
        boolean wasCommand = false;

        if (message.startsWith(Constants.COMMAND_TRIGGER)) {
            String[] commandData = message.split(",");
            if (commandData.length >= 2) {
                final String command = commandData[1].trim();
                System.out.println(TextFX.colorize("Checking command: " + command, Color.YELLOW));

                switch (command) {
                    case "quit":
                    case "disconnect":
                    case "logout":
                    case "logoff":
                        server.handleDisconnect(this);
                        wasCommand = true;
                        break;
                    case "reverse":
                        String relevantText = String.join(" ", Arrays.copyOfRange(commandData, 2, commandData.length));
                        server.handleReverseText(this, relevantText);
                        wasCommand = true;
                        break;
                    case "flip":
                        server.handleFlipCoin(this);
                        wasCommand = true;
                        break;
                    case "shuffle":
                        String toShuffle = String.join(" ", Arrays.copyOfRange(commandData, 2, commandData.length));
                        server.handleShuffleText(this, toShuffle);
                        wasCommand = true;
                        break;
                    case "pm":
                        if (commandData.length >= 4) {
                            try {
                                long targetId = Long.parseLong(commandData[2].trim());
                                String pmMessage = commandData[3];
                                server.handlePrivateMessage(this, targetId, pmMessage);
                            } catch (NumberFormatException nfe) {
                                System.out.println(TextFX.colorize("Invalid target ID", Color.RED));
                            }
                        } else {
                            System.out.println(TextFX.colorize("PM command missing target ID or message", Color.RED));
                        }
                        wasCommand = true;
                        break;
                    default:
                        break;
                }
            }
        }

        return wasCommand;
    }

    private void cleanup() {
        info("ServerThread cleanup() start");
        try {
            client.close();
            info("Closed Server-side Socket");
        } catch (IOException e) {
            info("Client already closed");
        }
        info("ServerThread cleanup() end");
    }
}
