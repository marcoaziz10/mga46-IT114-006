
package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);
    private static String name;
    private static PrintWriter out;

    public static void main(String[] args) {
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
                    Socket socket = new Socket(host, port);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    new Thread(new ServerListener(socket)).start();
                    out.println(name);
                } catch (Exception e) {
                    System.out.println("[CLIENT] Connection error: " + e.getMessage());
                }
            } else if (out != null) {
                out.println(name + ": " + input);
            } else {
                System.out.println("[CLIENT] Please connect first using /connect <host> <port>");
            }
        }
    }
}

