package M3;
// UCID: mga46
// Date: February 24, 2025
// Summary: Solution for Task #2 - Implement a Mad Libs generator that replaces placeholders dynamically
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    private static String ucid = "mga46"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 3,
                "Objective: Implement a Mad Libs generator that replaces placeholders dynamically.");

        Scanner scanner = new Scanner(System.in);
        File folder = new File(STORIES_FOLDER);

        if (!folder.exists() || !folder.isDirectory() || folder.listFiles().length == 0) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }
        List<String> lines = new ArrayList<>();
        // Start edits

        // load a random story file
        File[] files = folder.listFiles();
        Random rand = new Random();
        File storyFile = files[rand.nextInt(files.length)];
         
        // parse the story lines
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
    
        // iterate through the lines
    for (int i = 0; i < lines.size(); i++) {
            while (lines.get(i).contains("<")) {
        // prompt the user for each placeholder (note: there may be more than one
        // placeholder in a line)
        int start = lines.get(i).indexOf("<");
        int end = lines.get(i).indexOf(">", start);
        if (start == -1 || end == -1) break;

        String placeholder = lines.get(i).substring(start, end + 1);
        System.out.print("Enter a word for " + placeholder + ": ");
        String userInput = scanner.nextLine();

        lines.set(i, lines.get(i).replaceFirst("\\<.*?\\>", userInput));
    }
}
        // apply the update to the same collection slot

        // End edits
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
