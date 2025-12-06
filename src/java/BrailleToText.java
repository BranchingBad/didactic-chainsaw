import java.util.HashMap;
import java.util.Map;

public class BrailleToText { 

    private static final Map<String, String> BRAILLE_MAP = new HashMap<>();
    private static final Map<String, String> DIGIT_MAP = new HashMap<>();

    private static final String CAPITAL_INDICATOR = "⠠";   
    private static final String NUMBER_SIGN = "⠼";         
    private static final String HYPHEN = "⠤";             
    private static final String DOT_5 = "⠐";

    static {
        BRAILLE_MAP.put("⠁", "a"); BRAILLE_MAP.put("⠃", "b"); BRAILLE_MAP.put("⠉", "c");
        BRAILLE_MAP.put("⠙", "d"); BRAILLE_MAP.put("⠑", "e"); BRAILLE_MAP.put("⠋", "f");
        BRAILLE_MAP.put("⠛", "g"); BRAILLE_MAP.put("⠓", "h"); BRAILLE_MAP.put("⠊", "i");
        BRAILLE_MAP.put("⠚", "j"); BRAILLE_MAP.put("⠅", "k"); BRAILLE_MAP.put("⠇", "l");
        BRAILLE_MAP.put("⠍", "m"); BRAILLE_MAP.put("⠝", "n"); BRAILLE_MAP.put("⠕", "o");
        BRAILLE_MAP.put("⠏", "p"); BRAILLE_MAP.put("⠟", "q"); BRAILLE_MAP.put("⠗", "r");
        BRAILLE_MAP.put("⠎", "s"); BRAILLE_MAP.put("⠞", "t"); BRAILLE_MAP.put("⠥", "u");
        BRAILLE_MAP.put("⠧", "v"); BRAILLE_MAP.put("⠺", "w"); BRAILLE_MAP.put("⠭", "x");
        BRAILLE_MAP.put("⠽", "y"); BRAILLE_MAP.put("⠵", "z");

        BRAILLE_MAP.put(" ", " ");   
        BRAILLE_MAP.put("⠲", ".");   
        BRAILLE_MAP.put("⠂", ",");   
        BRAILLE_MAP.put("⠖", "!");   
        BRAILLE_MAP.put("⠦", "?");   
        BRAILLE_MAP.put("⠒", ":");   
        BRAILLE_MAP.put("⠆", ";");   
        BRAILLE_MAP.put("⠤", "-");   
        BRAILLE_MAP.put("⠄", "'");   
        BRAILLE_MAP.put("⠶", "\"");  
        BRAILLE_MAP.put("⠣", "(");   
        BRAILLE_MAP.put("⠜", ")");   

        DIGIT_MAP.put("⠁", "1"); DIGIT_MAP.put("⠃", "2"); DIGIT_MAP.put("⠉", "3");
        DIGIT_MAP.put("⠙", "4"); DIGIT_MAP.put("⠑", "5"); DIGIT_MAP.put("⠋", "6");
        DIGIT_MAP.put("⠛", "7"); DIGIT_MAP.put("⠓", "8"); DIGIT_MAP.put("⠊", "9");
        DIGIT_MAP.put("⠚", "0");
    }

    public static String translateUebGrade1ToText(String uebInput) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        boolean inNumericMode = false;

        while (i < uebInput.length()) {
            String currentChar = String.valueOf(uebInput.charAt(i));

            // Capital Indicator
            if (currentChar.equals(CAPITAL_INDICATOR)) {
                if (i + 1 < uebInput.length()) {
                    String nextBraille = String.valueOf(uebInput.charAt(i + 1));
                    String mappedText = BRAILLE_MAP.get(nextBraille);
                    if (mappedText != null && Character.isLetter(mappedText.charAt(0))) {
                        result.append(mappedText.toUpperCase());
                        i += 2;
                        continue;
                    }
                }
                i += 1;
                continue;
            }

            // Dot 5 (Parentheses Handling)
            if (currentChar.equals(DOT_5)) {
                if (i + 1 < uebInput.length()) {
                    String nextBraille = String.valueOf(uebInput.charAt(i + 1));
                    if (nextBraille.equals("⠣")) {
                        result.append("(");
                        i += 2;
                        continue;
                    }
                    if (nextBraille.equals("⠜")) {
                        result.append(")");
                        i += 2;
                        continue;
                    }
                }
                i += 1;
                continue;
            }

            // Numeric Indicator
            if (currentChar.equals(NUMBER_SIGN)) {
                inNumericMode = true;
                i += 1;
                continue;
            }

            String textChar = BRAILLE_MAP.get(currentChar);
            
            if (textChar != null) {
                if (inNumericMode) {
                    if (DIGIT_MAP.containsKey(currentChar)) {
                        result.append(DIGIT_MAP.get(currentChar));
                    } 
                    else if (currentChar.equals(HYPHEN)) {
                        // Hyphen terminates numeric mode
                        inNumericMode = false;
                        result.append('-');
                    } 
                    else if (currentChar.equals("⠲")) { // Period / Decimal
                        // Lookahead check
                        boolean isDecimal = false;
                        if (i + 1 < uebInput.length()) {
                            String nextC = String.valueOf(uebInput.charAt(i + 1));
                            if (DIGIT_MAP.containsKey(nextC)) {
                                isDecimal = true;
                            }
                        }
                        result.append(".");
                        if (!isDecimal) {
                            inNumericMode = false;
                        }
                    }
                    else if (currentChar.equals("⠂")) {
                        // Comma maintains numeric mode
                        result.append(",");
                    }
                    else {
                        inNumericMode = false;
                        result.append(textChar);
                    }
                } else {
                    result.append(textChar);
                }
            }
            i += 1;
        }

        return result.toString().trim();
    }
    
    public static void main(String[] args) {
         System.out.println(BrailleToText.translateUebGrade1ToText("⠼⠁⠃⠉⠲⠙⠑")); 
    }
}