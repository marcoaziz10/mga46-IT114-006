package M3;

// UCID: mga46
// Date: February 24, 2025
// Summary: Solution for Task #2 - Implement a Mad Libs generator that replaces placeholders dynamically

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
Challenge 3: Mad Libs Generator (Randomized Stories)
-----------------------------------------------------
- Load a **random** story from the "stories" folder
- Extract **each line** into a collection (i.e., ArrayList)
- Prompts user for each placeholder (i.e., <adjective>) 
    - Any word the user types is acceptable, no need to verify if it matches the placeholder type
    - Any placeholder with underscores should display with spaces instead
- Replace placeholders with user input (assign back to original slot in collection)
*/

public class MadLibsGenerator extends BaseClass {
    private static final String STORIES_FOLDER = "M3/stories";
    private static String ucid = "mga46"; // Updated UCID

    public static void main(String[] args) {
        printHeader(ucid, 3, "Objective: Implement a Mad Libs generator that replaces placeholders dynamically.");

        Scanner scanner = new Scanner(System.in);
        File folder = new File(STORIES_FOLDER);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        // Ensure stories exist before picking a random one
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        List<String> lines = new ArrayList<>();

        // START EDITS

        // Load a random story file
        File[] files = folder.listFiles();
        Random rand = new Random();
        File storyFile = files[rand.nextInt(files.length)];

        // Parse the story lines
        try (BufferedReader reader = new BufferedReader(new FileReader(storyFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading the story file.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        // Iterate through the lines
        for (int i = 0; i < lines.size(); i++) {
            while (lines.get(i).contains("<")) {
                // Prompt the user for each placeholder
                int start = lines.get(i).indexOf("<");
                int end = lines.get(i).indexOf(">", start);
                if (start == -1 || end == -1) break;

                String placeholder = lines.get(i).substring(start, end + 1);
                String formattedPlaceholder = placeholder.replace("_", " "); // Replace underscores with spaces
                System.out.print("Enter a word for " + formattedPlaceholder + ": ");
                String userInput = scanner.nextLine();

                // Apply the update to the same collection slot
                lines.set(i, lines.get(i).replaceFirst("<[^>]+>", userInput));
            }
        }


        // STOP EDITS

        System.out.println("\nYour Completed Mad Libs Story:\n");
        StringBuilder finalStory = new StringBuilder();
        for (String line : lines) {
            finalStory.append(line).append("\n");
        }
        System.out.println(finalStory.toString());

        printFooter(ucid, 3);
        scanner.close();
    }
}
