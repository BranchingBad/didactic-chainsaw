import java.util.HashMap;
import java.util.Map;

/**
 * Translates standard English text to UEB Grade 1 (uncontracted) braille.
 * This class mirrors the functionality of the provided Python script text-braille-ueb-grade-1.py.
 */
public class UebGrade1Translator {

    // --- UEB Grade 1 Mappings ---
    private static final Map<Character, String> BRAILLE_LETTERS = new HashMap<>();
    private static final Map<Character, String> PUNCTUATION_SIGNS = new HashMap<>();
    private static final Map<Character, String> NUMBER_CHARS = new HashMap<>();

    // 3. Indicators
    private static final String CAPITAL_LETTER_INDICATOR = "⠠"; // Dot 6
    private static final String NUMERIC_INDICATOR = "⠼";     // Dots 3, 4, 5, 6

    // Static initializer block to populate the maps
    static {
        // 1. Lowercase Alphabet (a-z)
        BRAILLE_LETTERS.put('a', "⠁"); BRAILLE_LETTERS.put('b', "⠃"); BRAILLE_LETTERS.put('c', "⠉");
        BRAILLE_LETTERS.put('d', "⠙"); BRAILLE_LETTERS.put('e', "⠑"); BRAILLE_LETTERS.put('f', "⠋");
        BRAILLE_LETTERS.put('g', "⠛"); BRAILLE_LETTERS.put('h', "⠓"); BRAILLE_LETTERS.put('i', "⠊");
        BRAILLE_LETTERS.put('j', "⠚"); BRAILLE_LETTERS.put('k', "⠅"); BRAILLE_LETTERS.put('l', "⠇");
        BRAILLE_LETTERS.put('m', "⠍"); BRAILLE_LETTERS.put('n', "⠝"); BRAILLE_LETTERS.put('o', "⠕");
        BRAILLE_LETTERS.put('p', "⠏"); BRAILLE_LETTERS.put('q', "⠟"); BRAILLE_LETTERS.put('r', "⠗");
        BRAILLE_LETTERS.put('s', "⠎"); BRAILLE_LETTERS.put('t', "⠞"); BRAILLE_LETTERS.put('u', "⠥");
        BRAILLE_LETTERS.put('v', "⠧"); BRAILLE_LETTERS.put('w', "⠺"); BRAILLE_LETTERS.put('x', "⠭");
        BRAILLE_LETTERS.put('y', "⠽"); BRAILLE_LETTERS.put('z', "⠵");

        // 2. Punctuation Signs
        PUNCTUATION_SIGNS.put(' ', " ");      // Space
        PUNCTUATION_SIGNS.put('.', "⠲");      // Period/Dot/Decimal Point
        PUNCTUATION_SIGNS.put(',', "⠂");      // Comma
        PUNCTUATION_SIGNS.put('!', "⠖");      // Exclamation point
        PUNCTUATION_SIGNS.put('?', "⠦");      // Question mark
        PUNCTUATION_SIGNS.put(':', "⠒");      // Colon
        PUNCTUATION_SIGNS.put(';', "⠆");      // Semicolon
        PUNCTUATION_SIGNS.put('-', "⠤");      // Hyphen/Dash
        PUNCTUATION_SIGNS.put('\'', "⠄");     // Apostrophe
        PUNCTUATION_SIGNS.put('(', "⠐⠣");     // Opening Parenthesis
        PUNCTUATION_SIGNS.put(')', "⠐⠜");     // Closing Parenthesis
        PUNCTUATION_SIGNS.put('"', "⠶");     // Double Quote (generic/non-directional)

        // 4. Number Characters (1-0 mapped to a-j braille)
        NUMBER_CHARS.put('1', "⠁"); NUMBER_CHARS.put('2', "⠃"); NUMBER_CHARS.put('3', "⠉");
        NUMBER_CHARS.put('4', "⠙"); NUMBER_CHARS.put('5', "⠑"); NUMBER_CHARS.put('6', "⠋");
        NUMBER_CHARS.put('7', "⠛"); NUMBER_CHARS.put('8', "⠓"); NUMBER_CHARS.put('9', "⠊");
        NUMBER_CHARS.put('0', "⠚");
    }

    /**
     * Translates standard English text to UEB Grade 1 (uncontracted) braille.
     * @param text The standard English text to translate.
     * @return The translated UEB Grade 1 braille string.
     */
    public static String translateToUebGrade1(String text) {
        StringBuilder brailleOutput = new StringBuilder();
        boolean inNumericMode = false;
        
        // Use the standardized text, replacing directional quotes with the generic one
        String standardizedText = text.replace('“', '"').replace('”', '"');

        for (char charCode : standardizedText.toCharArray()) {
            char charLower = Character.toLowerCase(charCode);
            
            // --- Handle Numeric Mode ---
            if (Character.isDigit(charCode)) {
                if (!inNumericMode) {
                    brailleOutput.append(NUMERIC_INDICATOR);
                    inNumericMode = true;
                }
                brailleOutput.append(NUMBER_CHARS.get(charCode));
                continue;
            } else {
                if (inNumericMode) {
                    // Turn off numeric mode if the non-digit character is a space or punctuation
                    // (Numbers are ended by a space or punctuation, but a letter *immediately*
                    // following a number sign will not be a number unless it's a number/letter mix like 3rd)
                    // For Grade 1 simplicity, we turn off number mode unless it's part of a known numeric symbol.
                    inNumericMode = false;
                }
            }

            // --- Handle Punctuation and Space ---
            if (PUNCTUATION_SIGNS.containsKey(charCode)) {
                brailleOutput.append(PUNCTUATION_SIGNS.get(charCode));
                continue;
            }

            // --- Handle Capitalization and Letters ---
            if (Character.isUpperCase(charCode)) {
                brailleOutput.append(CAPITAL_LETTER_INDICATOR);
                // The python script maps only lowercase, so we ensure the lookup is lowercase
                brailleOutput.append(BRAILLE_LETTERS.get(charLower));
                continue;
            }

            // --- Handle Lowercase Letters ---
            if (Character.isLetter(charCode)) {
                brailleOutput.append(BRAILLE_LETTERS.get(charCode));
                continue;
            }
            
            // --- Handle Other (ignored for simplicity) ---
            // Unmapped characters are ignored as in the Python script.
        }

        return brailleOutput.toString();
    }

    public static void main(String[] args) {
        // 1. Define the text to be translated (Example usage from Python script)
        String textToTranslate = "Hello World! This is a test with 123.";

        // 2. Perform the translation
        String brailleResult = translateToUebGrade1(textToTranslate);

        // 3. Print the result
        System.out.println("--- Translation (Text to Braille) ---");
        System.out.println("Text Input: " + textToTranslate);
        System.out.println("Braille Output: " + brailleResult); 
        // Expected Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
        System.out.println("-------------------------------------");
    }
}