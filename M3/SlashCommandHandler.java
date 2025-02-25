package M3;
// UCID: mga46
// Date: February 24, 2025
// Summary: Solution for Task #2 - Implementing a Simple Slash Command Handler

/*
Challenge 2: Simple Slash Command Handler
-----------------------------------------
- Accept user input as slash commands
  - "/greet <name>" → Prints "Hello, <name>!"
  - "/roll <num>d<sides>" → Roll <num> dice with <sides> and returns a single outcome as "Rolled <num>d<sides> and got <result>!"
  - "/echo <message>" → Prints the message back
  - "/quit" → Exits the program
- Commands are case-insensitive
- Print an error for unrecognized commands
- Print errors for invalid command formats (when applicable)
- Capture 3 variations of each command except "/quit"
*/

import java.util.Scanner;

public class SlashCommandHandler extends BaseClass {
    private static String ucid = "mga46"; // <-- change to your UCID

    public static void main(String[] args) {
        printHeader(ucid, 2, "Objective: Implement a simple slash command parser.");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        // Can define any variables needed here

        while (running) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase(); 
          
            // Ensure case-insensitivity
            // get entered text
            
            // check if greet
            //// process greet
            if (command.equals("/greet")) {
                if (parts.length < 2) {
                    System.out.println("Error: Missing name. Usage: /greet <name>");
                } else {
                    System.out.println("Hello, " + parts[1] + "!");
                }
            }
            // check if roll
            //// process roll
            //// handle invalid formats
            else if (command.equals("/roll")) {
                if (parts.length < 2 || !parts[1].matches("\\d+d\\d+")) {
                    System.out.println("Error: Invalid format. Usage: /roll <num>d<sides>");
                } else {
                    String[] diceParts = parts[1].split("d");
                    int num = Integer.parseInt(diceParts[0]);
                    int sides = Integer.parseInt(diceParts[1]);
                    Random rand = new Random();
                    int result = 0;

                    for (int i = 0; i < num; i++) {
                        result += rand.nextInt(sides) + 1;
                    }

                    System.out.println("Rolled " + parts[1] + " and got " + result + "!");
                }
            }
            // check if echo
            //// process echo
            else if (command.equals("/echo")) {
                if (parts.length < 2) {
                    System.out.println("Error: Missing message. Usage: /echo <message>");
                } else {
                    System.out.println(parts[1]);
                }
            }
            // check if quit
            //// process quit
            else if (command.equals("/quit")) {
                running = false;
                System.out.println("Exiting program...");
            }
            // handle invalid commnads
            else {
                System.out.println("Error: Unrecognized command.");
            }
        }        

        printFooter(ucid, 2);
        scanner.close();
    }
}
