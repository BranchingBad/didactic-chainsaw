import java.util.HashMap;
import java.util.Map;

/**
 * Translates UEB Grade 1 (uncontracted) braille input to standard English text.
 * This class mirrors the functionality of the provided Python script braille-text.py.
 */
public class BrailleToTextTranslator {

    // --- Mappings and Indicators ---
    private static final Map<String, String> BRAILLE_MAP = new HashMap<>();
    private static final Map<String, String> DIGIT_MAP = new HashMap<>();

    private static final String CAPITAL_INDICATOR = "⠠";   // Dot 6
    private static final String NUMBER_SIGN = "⠼";         // Dots 3456
    private static final String HYPHEN = "⠤";             // Hyphen/Dash braille symbol
    private static final String DECIMAL_POINT = "⠨";       // Dot 6, 3 (Used for Decimal in the input example)

    // Static initializer block to populate the maps
    static {
        // 1. Braille to Text Mapping (Letters and Punctuation)
        BRAILLE_MAP.put("⠁", "a"); BRAILLE_MAP.put("⠃", "b"); BRAILLE_MAP.put("⠉", "c");
        BRAILLE_MAP.put("⠙", "d"); BRAILLE_MAP.put("⠑", "e"); BRAILLE_MAP.put("⠋", "f");
        BRAILLE_MAP.put("⠛", "g"); BRAILLE_MAP.put("⠓", "h"); BRAILLE_MAP.put("⠊", "i");
        BRAILLE_MAP.put("⠚", "j"); BRAILLE_MAP.put("⠅", "k"); BRAILLE_MAP.put("⠇", "l");
        BRAILLE_MAP.put("⠍", "m"); BRAILLE_MAP.put("⠝", "n"); BRAILLE_MAP.put("⠕", "o");
        BRAILLE_MAP.put("⠏", "p"); BRAILLE_MAP.put("⠟", "q"); BRAILLE_MAP.put("⠗", "r");
        BRAILLE_MAP.put("⠎", "s"); BRAILLE_MAP.put("⠞", "t"); BRAILLE_MAP.put("⠥", "u");
        BRAILLE_MAP.put("⠧", "v"); BRAILLE_MAP.put("⠺", "w"); BRAILLE_MAP.put("⠭", "x");
        BRAILLE_MAP.put("⠽", "y"); BRAILLE_MAP.put("⠵", "z");

        // Punctuation and Symbols
        BRAILLE_MAP.put(" ", " ");   // Space
        BRAILLE_MAP.put("⠲", ".");   // Period
        BRAILLE_MAP.put("⠂", ",");   // Comma
        BRAILLE_MAP.put("⠖", "!");   // Exclamation point
        BRAILLE_MAP.put("⠦", "?");   // Question mark
        BRAILLE_MAP.put("⠒", ":");   // Colon
        BRAILLE_MAP.put("⠆", ";");   // Semicolon
        BRAILLE_MAP.put("⠤", "-");   // Hyphen / Dash
        BRAILLE_MAP.put("⠄", "'");   // Apostrophe
        BRAILLE_MAP.put("⠶", "\"");  // Generic Double Quote
        BRAILLE_MAP.put("⠣", "(");   // Opening Parenthesis (Note: the text-to-braille uses ⠐⠣)
        BRAILLE_MAP.put("⠜", ")");   // Closing Parenthesis (Note: the text-to-braille uses ⠐⠜)

        // Braille digits (a-j) to Text digits (1-0)
        DIGIT_MAP.put("⠁", "1"); DIGIT_MAP.put("⠃", "2"); DIGIT_MAP.put("⠉", "3");
        DIGIT_MAP.put("⠙", "4"); DIGIT_MAP.put("⠑", "5"); DIGIT_MAP.put("⠋", "6");
        DIGIT_MAP.put("⠛", "7"); DIGIT_MAP.put("⠓", "8"); DIGIT_MAP.put("⠊", "9");
        DIGIT_MAP.put("⠚", "0");
    }

    /**
     * Translates a UEB Grade 1 braille string to standard English text.
     * @param uebInput The UEB Grade 1 braille string.
     * @return The translated standard English text.
     */
    public static String translateUebGrade1ToText(String uebInput) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        boolean inNumericMode = false;

        // Note: Java's char type is 16-bit and handles Unicode well. We iterate
        // by index and use `substring` for multi-character braille symbols (e.g., indicators).
        
        while (i < uebInput.length()) {
            String currentChar = String.valueOf(uebInput.charAt(i));

            // Check for Indicators (Single characters)
            if (currentChar.equals(CAPITAL_INDICATOR)) {
                // Check if the next character exists
                if (i + 1 < uebInput.length()) {
                    String nextBraille = String.valueOf(uebInput.charAt(i + 1));
                    String mappedText = BRAILLE_MAP.get(nextBraille);
                    
                    // If the next braille symbol is a letter
                    if (mappedText != null && Character.isLetter(mappedText.charAt(0))) {
                        result.append(mappedText.toUpperCase());
                        i += 2; // Consume indicator and letter
                        continue;
                    }
                }
                // If indicator is at the end or followed by non-letter, just consume the indicator (or ignore)
                i += 1;
                continue;
            }

            if (currentChar.equals(NUMBER_SIGN)) {
                inNumericMode = true;
                i += 1;
                continue;
            }

            // Check for the specific Decimal Point from the example
            if (currentChar.equals(DECIMAL_POINT)) {
                 // In the context of a number, it's a decimal point, and it turns off numeric mode
                result.append('.');
                inNumericMode = false;
                i += 1;
                continue;
            }

            // --- Handle the actual braille cell ---
            String textChar = BRAILLE_MAP.get(currentChar);
            
            if (textChar != null) {
                if (inNumericMode) {
                    // Check for Braille Digits (a-j cells)
                    if (DIGIT_MAP.containsKey(currentChar)) {
                        result.append(DIGIT_MAP.get(currentChar));
                        // Numeric mode stays on for next character
                    } 
                    // Hyphen maintains numeric mode for number ranges, etc.
                    else if (currentChar.equals(HYPHEN)) {
                        result.append('-');
                    } 
                    // Punctuation (or a space) ends numeric mode
                    else {
                        inNumericMode = false;
                        result.append(textChar);
                    }
                } else {
                    // Regular letter or punctuation (no capital *word* mode logic is needed as the input doesn't use it)
                    result.append(textChar);
                }
            } else {
                // Handle unmapped braille (e.g., part of a multi-cell symbol like ⠐⠣)
                // The provided braille input does not contain multi-cell symbols, so this is a simple pass-through/ignore.
            }
            
            i += 1;
        }

        return result.toString().trim();
    }

    public static void main(String[] args) {
        // The original UEB Grade 1 input from the user's request:
        String uebInput = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠂⠀⠼⠙⠑⠤⠽⠑⠁⠗⠤⠕⠇⠙⠀⠠⠎⠁⠗⠁⠀⠠⠚⠁⠝⠑⠀⠠⠍⠕⠕⠗⠑⠀⠙⠗⠕⠏⠏⠑⠙⠀⠓⠑⠗⠀⠎⠕⠝⠀⠕⠋⠋⠀⠁⠞⠀⠓⠊⠎⠀⠠⠎⠁⠝⠀⠠⠋⠗⠁⠝⠉⠊⠎⠉⠕⠀⠎⠉⠓⠕⠕⠇⠂⠀⠧⠊⠎⠊⠞⠑⠙⠀⠁⠀⠏⠗⠊⠧⠁⠞⠑⠀⠛⠥⠝⠀⠙⠑⠁⠇⠑⠗⠀⠁⠝⠙⠂⠀⠊⠝⠀⠺⠓⠁⠞⠀⠎⠓⠑⠀⠇⠁⠞⠑⠗⠀⠞⠕⠇⠙⠀⠞⠓⠑⠀⠠⠇⠕⠎⠀⠠⠁⠝⠛⠑⠇⠑⠎⠀⠠⠞⠊⠍⠑⠎⠀⠺⠁⠎⠀⠁⠀⠶⠁⠀⠅⠊⠝⠙⠀⠕⠋⠀⠥⠇⠞⠊⠍⠁⠞⠑⠀⠏⠗⠕⠞⠑⠎⠞⠀⠁⠛⠁⠊⠝⠞⠀⠞⠓⠑⠀⠎⠽⠎⠞⠑⠍⠂⠴⠀⠙⠗⠑⠺⠀⠁⠀⠨⠼⠉⠓⠤⠉⠁⠇⠊⠃⠗⠑⠀⠏⠊⠎⠞⠕⠇⠀⠕⠥⠞⠎⠊⠙⠑⠀⠁⠀⠓⠕⠞⠑⠇⠀⠇⠁⠞⠑⠗⠀⠊⠝⠀⠞⠓⠑⠀⠙⠁⠽⠂⠀⠋⠊⠗⠊⠝⠛⠀⠁⠞⠀⠞⠓⠑⠝⠤⠏⠗⠑⠎⠊⠙⠑⠝⠞⠀⠠⠛⠑⠗⠁⠇⠙⠀⠠⠋⠕⠗⠙⠲";

        // --- Output the translation of the original input ---
        System.out.println("--- Translation of the Original UEB Input (Braille to Text) ---");
        String translatedText = translateUebGrade1ToText(uebInput);
        System.out.println("UEB Grade 1: " + uebInput);
        System.out.println("Text Output: " + translatedText);
        // Expected Output: On Sept. 22, 1975, 45-year-old Sara Jane Moore dropped her son off at his San Francisco school, visited a private gun dealer and, in what she later told the Los Angeles Times was a "a kind of ultimate protest against the system," drew a .38-caliber pistol outside a hotel later in the day, firing at then-president Gerald Ford.
        System.out.println("---------------------------------------------------------------");

        // --- Example of an Alternative Input ---
        String alternativeUebInput = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠊⠀⠓⠁⠧⠑⠀⠼⠁⠚⠀⠁⠏⠏⠇⠑⠎⠲";

        // --- Output the translation of the alternative input ---
        System.out.println("--- Translation of an Alternative UEB Input ---");
        String translatedAlternative = translateUebGrade1ToText(alternativeUebInput);
        System.out.println("UEB Grade 1: " + alternativeUebInput);
        System.out.println("Text Output: " + translatedAlternative);
        // Expected Output: Hello World! I have 10 apples.
        System.out.println("---------------------------------------------");
    }
}