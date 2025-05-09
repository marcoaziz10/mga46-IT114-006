package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = "";
        PrintWriter out = null;
        Socket socket = null;

        System.out.println("[CLIENT] Welcome. Use /name and /connect to begin.");

        while (true) {
            String input = scanner.nextLine();

            if (input.startsWith("/name ")) {
                name = input.substring(6);
                System.out.println("[CLIENT] Name set to: " + name);

            } else if (input.startsWith("/connect ")) {
                try {
                    String[] parts = input.split(" ");
                    String host = parts[1];
                    int port = Integer.parseInt(parts[2]);
                    socket = new Socket(host, port);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    new Thread(new ServerListener(socket)).start();
                    out.println(name);
                } catch (Exception e) {
                    System.out.println("[CLIENT] Connection error: " + e.getMessage());
                }

            } else if (input.equals("/exit")) {
                try {
                    if (socket != null) {
                        socket.close();
                        System.out.println("[CLIENT] Disconnected.");
                    }
                } catch (IOException e) {
                    System.out.println("[CLIENT] Error disconnecting: " + e.getMessage());
                }
                break; // exits the loop and ends program

            } else if (out != null) {
                // Send commands as-is, messages get tagged with the name
                if (input.startsWith("/")) {
                    out.println(input);
                } else {
                    out.println(name + ": " + input);
                }

            } else {
                System.out.println("[CLIENT] Please connect first using /connect <host> <port>");
            }
        }

        scanner.close();
    }
}
