using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

/**
 * Translates UEB Grade 1 (uncontracted) braille input to standard English text.
 * This class mirrors the functionality of the provided Python script braille-text.py.
 */
public static class BrailleToTextTranslator
{
    // --- Mappings and Indicators ---
    private static readonly IReadOnlyDictionary<string, string> BrailleMap;
    private static readonly IReadOnlyDictionary<string, string> DigitMap;

    private const string CapitalIndicator = "⠠";   // Dot 6
    private const string NumberSign = "⠼";         // Dots 3456
    private const string Hyphen = "⠤";             // Hyphen/Dash braille symbol
    private const string DecimalPointBraille = "⠨"; // Dot 6, 3 (Used for Decimal in the input example: .38)

    // Static constructor to populate the maps
    static BrailleToTextTranslator()
    {
        // 1. Braille to Text Mapping (Letters and Punctuation)
        BrailleMap = new Dictionary<string, string>
        {
            // Lowercase Letters
            { "⠁", "a" }, { "⠃", "b" }, { "⠉", "c" }, { "⠙", "d" }, { "⠑", "e" }, { "⠋", "f" },
            { "⠛", "g" }, { "⠓", "h" }, { "⠊", "i" }, { "⠚", "j" }, { "⠅", "k" }, { "⠇", "l" },
            { "⠍", "m" }, { "⠝", "n" }, { "⠕", "o" }, { "⠏", "p" }, { "⠟", "q" }, { "⠗", "r" },
            { "⠎", "s" }, { "⠞", "t" }, { "⠥", "u" }, { "⠧", "v" }, { "⠺", "w" }, { "⠭", "x" },
            { "⠽", "y" }, { "⠵", "z" },

            // Punctuation and Symbols
            { " ", " " },   // Space
            { "⠲", "." },   // Period
            { "⠂", "," },   // Comma
            { "⠖", "!" },   // Exclamation point
            { "⠦", "?" },   // Question mark
            { "⠒", ":" },   // Colon
            { "⠆", ";" },   // Semicolon
            { "⠤", "-" },   // Hyphen / Dash
            { "⠄", "'" },   // Apostrophe
            { "⠶", "\"" },  // Generic Double Quote
            { "⠣", "(" },   // Opening Parenthesis
            { "⠜", ")" }    // Closing Parenthesis
        };

        // Braille digits (a-j) to Text digits (1-0)
        DigitMap = new Dictionary<string, string>
        {
            { "⠁", "1" }, { "⠃", "2" }, { "⠉", "3" }, { "⠙", "4" }, { "⠑", "5" },
            { "⠋", "6" }, { "⠛", "7" }, { "⠓", "8" }, { "⠊", "9" }, { "⠚", "0" }
        };
    }

    /**
     * Translates a UEB Grade 1 braille string to standard English text.
     * @param uebInput The UEB Grade 1 braille string.
     * @return The translated standard English text.
     */
    public static string TranslateUebGrade1ToText(string uebInput)
    {
        var result = new StringBuilder();
        int i = 0;
        bool inNumericMode = false;
        bool inCapitalWordMode = false; // Used to track capital word mode from Python (though not triggered in example)

        while (i < uebInput.Length)
        {
            // Extract the current character (braille cell) as a string
            string currentChar = uebInput[i].ToString();

            // --- 1. Check for Indicators ---
            if (currentChar == CapitalIndicator)
            {
                // Check for Capital Word Indicator (⠠⠠) - Not used in the provided example
                if (i + 1 < uebInput.Length && uebInput[i + 1].ToString() == CapitalIndicator)
                {
                    inCapitalWordMode = true;
                    i += 2;
                    continue;
                }
                
                // Capital Letter Indicator (⠠)
                if (i + 1 < uebInput.Length)
                {
                    string nextBraille = uebInput[i + 1].ToString();
                    if (BrailleMap.TryGetValue(nextBraille, out string mappedText) && mappedText.Length == 1 && char.IsLetter(mappedText[0]))
                    {
                        // Apply capitalization to the next letter
                        result.Append(char.ToUpper(mappedText[0]));
                        i += 2; // Consume indicator and letter
                        continue;
                    }
                }
                // If indicator is at the end or followed by non-letter, just consume the indicator
                i += 1;
                continue;
            }

            if (currentChar == NumberSign)
            {
                inNumericMode = true;
                i += 1;
                continue;
            }

            // Check for the specific Decimal Point from the example (`⠨` Dot 6, 3)
            if (currentChar == DecimalPointBraille)
            {
                if (inNumericMode)
                {
                    result.Append('.');
                    inNumericMode = false; // Decimal point turns off number mode
                }
                // If not in numeric mode, it's the capital passage indicator, which we ignore
                // (or treat as a period if it appears, based on the Python script's logic).
                // Sticking to Python's simplified logic:
                else
                {
                    result.Append('.'); 
                }
                i += 1;
                continue;
            }

            // --- 2. Handle the actual braille cell ---
            if (BrailleMap.TryGetValue(currentChar, out string textChar))
            {
                if (inNumericMode)
                {
                    // Check for Braille Digits (a-j cells)
                    if (DigitMap.TryGetValue(currentChar, out string digit))
                    {
                        result.Append(digit);
                        // Numeric mode stays on
                    } 
                    // Hyphen maintains numeric mode
                    else if (currentChar == Hyphen)
                    {
                        result.Append('-');
                    } 
                    // Punctuation (or a space) ends numeric mode
                    else
                    {
                        inNumericMode = false;
                        result.Append(textChar);
                        // If it was capital word mode, it ends here on punctuation/space
                        if (textChar.Any(c => !char.IsLetterOrDigit(c) && !char.IsWhiteSpace(c)) || char.IsWhiteSpace(textChar[0])) 
                        {
                            inCapitalWordMode = false;
                        }
                    }
                } 
                else // Not in numeric mode
                {
                    // Apply capital word mode if active and the character is a letter
                    if (inCapitalWordMode && textChar.Length == 1 && char.IsLetter(textChar[0]))
                    {
                        result.Append(char.ToUpper(textChar[0]));
                    }
                    else
                    {
                        result.Append(textChar);
                    }
                    
                    // Punctuation or space turns off capital *word* mode
                    if (textChar.Any(c => !char.IsLetterOrDigit(c) && !char.IsWhiteSpace(c)) || char.IsWhiteSpace(textChar[0]))
                    {
                        inCapitalWordMode = false;
                    }
                }
            } 
            else 
            {
                // Unmapped characters are ignored (e.g., multi-cell parts like the ⠐ from parenthesis)
            }
            
            i += 1;
        }

        return result.ToString().Trim();
    }

    public static void Main()
    {
        // The original UEB Grade 1 input from the user's request:
        string uebInput = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠂⠀⠼⠙⠑⠤⠽⠑⠁⠗⠤⠕⠇⠙⠀⠠⠎⠁⠗⠁⠀⠠⠚⠁⠝⠑⠀⠠⠍⠕⠕⠗⠑⠀⠙⠗⠕⠏⠏⠑⠙⠀⠓⠑⠗⠀⠎⠕⠝⠀⠕⠋⠋⠀⠁⠞⠀⠓⠊⠎⠀⠠⠎⠁⠝⠀⠠⠋⠗⠁⠝⠉⠊⠎⠉⠕⠀⠎⠉⠓⠕⠕⠇⠂⠀⠧⠊⠎⠊⠞⠑⠙⠀⠁⠀⠏⠗⠊⠧⠁⠞⠑⠀⠛⠥⠝⠀⠙⠑⠁⠇⠑⠗⠀⠁⠝⠙⠂⠀⠊⠝⠀⠺⠓⠁⠞⠀⠎⠓⠑⠀⠇⠁⠞⠑⠗⠀⠞⠕⠇⠙⠀⠞⠓⠑⠀⠠⠇⠕⠎⠀⠠⠁⠝⠛⠑⠇⠑⠎⠀⠠⠞⠊⠍⠑⠎⠀⠺⠁⠎⠀⠁⠀⠶⠁⠀⠅⠊⠝⠙⠀⠕⠋⠀⠥⠇⠞⠊⠍⠁⠞⠑⠀⠏⠗⠕⠞⠑⠎⠞⠀⠁⠛⠁⠊⠝⠎⠞⠀⠞⠓⠑⠀⠎⠽⠎⠞⠑⠍⠂⠴⠀⠙⠗⠑⠺⠀⠁⠀⠨⠼⠉⠓⠤⠉⠁⠇⠊⠃⠗⠑⠀⠏⠊⠎⠞⠕⠇⠀⠕⠥⠞⠎⠊⠙⠑⠀⠁⠀⠓⠕⠞⠑⠇⠀⠇⠁⠞⠑⠗⠀⠊⠝⠀⠞⠓⠑⠀⠙⠁⠽⠂⠀⠋⠊⠗⠊⠝⠛⠀⠁⠞⠀⠞⠓⠑⠝⠤⠏⠗⠑⠎⠊⠙⠑⠝⠞⠀⠠⠛⠑⠗⠁⠇⠙⠀⠠⠋⠕⠗⠙⠲";

        // --- Output the translation of the original UEB input ---
        Console.WriteLine("--- Translation of the Original UEB Input (Braille to Text) ---");
        string translatedText = TranslateUebGrade1ToText(uebInput);
        Console.WriteLine($"UEB Grade 1: {uebInput}");
        Console.WriteLine($"Text Output: {translatedText}");
        Console.WriteLine("---------------------------------------------------------------");

        // --- Example of an Alternative Input ---
        string alternativeUebInput = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠊⠀⠓⠁⠧⠑⠀⠼⠁⠚⠀⠁⠏⠏⠇⠑⠎⠲";

        // --- Output the translation of the alternative input ---
        Console.WriteLine("--- Translation of an Alternative UEB Input ---");
        string translatedAlternative = TranslateUebGrade1ToText(alternativeUebInput);
        Console.WriteLine($"UEB Grade 1: {alternativeUebInput}");
        Console.WriteLine($"Text Output: {translatedAlternative}");
        Console.WriteLine("---------------------------------------------");
    }
}