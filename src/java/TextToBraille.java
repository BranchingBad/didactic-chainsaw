import java.util.HashMap;
import java.util.Map;

/**
 * Translates standard English text to UEB Grade 1 (uncontracted) braille.
 */
public class TextToBraille { // Renamed from UebGrade1Translator

    // --- UEB Grade 1 Mappings ---
    private static final Map<Character, String> BRAILLE_LETTERS = new HashMap<>();
    private static final Map<Character, String> PUNCTUATION_SIGNS = new HashMap<>();
    private static final Map<Character, String> NUMBER_CHARS = new HashMap<>();

    // Indicators
    private static final String CAPITAL_LETTER_INDICATOR = "⠠"; 
    private static final String NUMERIC_INDICATOR = "⠼";     

    static {
        // 1. Lowercase Alphabet
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
        PUNCTUATION_SIGNS.put(' ', " ");      
        PUNCTUATION_SIGNS.put('.', "⠲");      
        PUNCTUATION_SIGNS.put(',', "⠂");      
        PUNCTUATION_SIGNS.put('!', "⠖");      
        PUNCTUATION_SIGNS.put('?', "⠦");      
        PUNCTUATION_SIGNS.put(':', "⠒");      
        PUNCTUATION_SIGNS.put(';', "⠆");      
        PUNCTUATION_SIGNS.put('-', "⠤");      
        PUNCTUATION_SIGNS.put('\'', "⠄");     
        PUNCTUATION_SIGNS.put('(', "⠐⠣");     
        PUNCTUATION_SIGNS.put(')', "⠐⠜");     
        PUNCTUATION_SIGNS.put('"', "⠶");     

        // 4. Number Characters 
        NUMBER_CHARS.put('1', "⠁"); NUMBER_CHARS.put('2', "⠃"); NUMBER_CHARS.put('3', "⠉");
        NUMBER_CHARS.put('4', "⠙"); NUMBER_CHARS.put('5', "⠑"); NUMBER_CHARS.put('6', "⠋");
        NUMBER_CHARS.put('7', "⠛"); NUMBER_CHARS.put('8', "⠓"); NUMBER_CHARS.put('9', "⠊");
        NUMBER_CHARS.put('0', "⠚");
    }

    public static String translateToUebGrade1(String text) {
        StringBuilder brailleOutput = new StringBuilder();
        boolean inNumericMode = false;
        
        String standardizedText = text.replace('“', '"').replace('”', '"');
        char[] chars = standardizedText.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char charCode = chars[i];
            char charLower = Character.toLowerCase(charCode);
            
            // --- Handle Numeric Mode ---
            if (Character.isDigit(charCode)) {
                if (!inNumericMode) {
                    brailleOutput.append(NUMERIC_INDICATOR);
                    inNumericMode = true;
                }
                brailleOutput.append(NUMBER_CHARS.get(charCode));
                continue;
            }

            // --- Handle Decimal Point in Numeric Mode ---
            if (charCode == '.' && inNumericMode) {
                // Lookahead to see if next char is digit
                if (i + 1 < chars.length && Character.isDigit(chars[i + 1])) {
                    brailleOutput.append(PUNCTUATION_SIGNS.get('.'));
                    // Maintain numeric mode
                    continue;
                }
            }

            if (inNumericMode) {
                inNumericMode = false;
            }

            // --- Handle Punctuation and Space ---
            if (PUNCTUATION_SIGNS.containsKey(charCode)) {
                brailleOutput.append(PUNCTUATION_SIGNS.get(charCode));
                continue;
            }

            // --- Handle Capitalization and Letters ---
            if (Character.isUpperCase(charCode)) {
                brailleOutput.append(CAPITAL_LETTER_INDICATOR);
                brailleOutput.append(BRAILLE_LETTERS.get(charLower));
                continue;
            }

            if (Character.isLetter(charCode)) {
                brailleOutput.append(BRAILLE_LETTERS.get(charCode));
                continue;
            }
        }

        return brailleOutput.toString();
    }

    public static void main(String[] args) {
        String textToTranslate = "Hello World! 123.45";
        String brailleResult = TextToBraille.translateToUebGrade1(textToTranslate); // Updated class reference
        System.out.println("Text: " + textToTranslate);
        System.out.println("Braille: " + brailleResult); 
    }
}