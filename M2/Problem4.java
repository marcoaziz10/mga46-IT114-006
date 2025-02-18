package M2;

// UCID: mga46
// Date: February 17, 2025
// Summary: Solution for #4 - Transforming text with cleaning & formatting
public class Problem4 extends BaseClass {
    private static String[] array1 = { "hello world!", "java programming", "special@#$%^&characters", "numbers 123 456",
            "mIxEd CaSe InPut!" };
    private static String[] array2 = { "hello world", "java programming", "this is a title case test",
            "capitalize every word", "mixEd CASE input" };
    private static String[] array3 = { "  hello   world  ", "java    programming  ",
            "  extra    spaces  between   words   ",
            "      leading and trailing spaces      ", "multiple      spaces" };
    private static String[] array4 = { "hello world", "java programming", "short", "a", "even" };

    private static void transformText(String[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfoBasic(arr, arrayNumber);

        // Challenge 1: Remove non-alphanumeric characters except spaces
        // Challenge 2: Convert text to Title Case
        // Challenge 3: Trim leading/trailing spaces and remove duplicate spaces
        // Result 1-3: Assign final phrase to `placeholderForModifiedPhrase`
        // Challenge 4 (extra credit): Extract middle 3 characters (beginning starts at middle of phrase),
        // assign to 'placeholderForMiddleCharacters'
        // if not enough characters assign "Not enough characters"
 
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        String placeholderForModifiedPhrase = "";
        String placeholderForMiddleCharacters = "";
        
        for(int i = 0; i <arr.length; i++){
            // Start Solution Edits
            //Remove non-alphanumeric characters except spaces
            String cleanedText = arr[i].replaceAll("[^a-zA-Z0-9 ]", "");

            //Convert to Title Case
            String[] words = cleanedText.toLowerCase().split("\\s+");
            StringBuilder titleCased = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    titleCased.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }
            String titleCaseText = titleCased.toString().trim();

            //Trim spaces and replace multiple spaces with a single space
            String modifiedPhrase = titleCaseText.replaceAll("\\s+", " ").trim();

            //Extract middle 3 characters
            String middleCharacters;
            int length = modifiedPhrase.length();
            if (length < 3) {
                middleCharacters = "Not enough characters";
            } else {
                int middleIndex = length / 2 - 1;
                middleCharacters = modifiedPhrase.substring(middleIndex, middleIndex + 3);
            }

            //Assign final values
            // Assign final values
            placeholderForModifiedPhrase = modifiedPhrase;
            placeholderForMiddleCharacters = middleCharacters;

             // End Solution Edits
            System.out.println(String.format("Index[%d] \"%s\" | Middle: \"%s\"",i, placeholderForModifiedPhrase, placeholderForMiddleCharacters));
        }

       

        
        System.out.println("\n______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "mga46"; // <-- change to your UCID
        // No edits below this line
        printHeader(ucid, 4);

        transformText(array1, 1);
        transformText(array2, 2);
        transformText(array3, 3);
        transformText(array4, 4);
        printFooter(ucid, 4);
    }

}
