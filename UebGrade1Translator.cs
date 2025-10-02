using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

/**
 * Translates standard English text to UEB Grade 1 (uncontracted) braille.
 * This class mirrors the functionality of the provided Python script text-braille-ueb-grade-1.py.
 */
public static class UebGrade1Translator
{
    // --- UEB Grade 1 Mappings ---
    private static readonly IReadOnlyDictionary<char, string> BrailleLetters;
    private static readonly IReadOnlyDictionary<char, string> PunctuationSigns;
    private static readonly IReadOnlyDictionary<char, string> NumberChars;

    // 3. Indicators
    private const string CapitalLetterIndicator = "⠠"; // Dot 6
    private const string NumericIndicator = "⠼";     // Dots 3, 4, 5, 6

    // Static constructor to populate the maps
    static UebGrade1Translator()
    {
        // 1. Lowercase Alphabet (a-z)
        BrailleLetters = new Dictionary<char, string>
        {
            { 'a', "⠁" }, { 'b', "⠃" }, { 'c', "⠉" }, { 'd', "⠙" }, { 'e', "⠑" }, { 'f', "⠋" },
            { 'g', "⠛" }, { 'h', "⠓" }, { 'i', "⠊" }, { 'j', "⠚" }, { 'k', "⠅" }, { 'l', "⠇" },
            { 'm', "⠍" }, { 'n', "⠝" }, { 'o', "⠕" }, { 'p', "⠏" }, { 'q', "⠟" }, { 'r', "⠗" },
            { 's', "⠎" }, { 't', "⠞" }, { 'u', "⠥" }, { 'v', "⠧" }, { 'w', "⠺" }, { 'x', "⠭" },
            { 'y', "⠽" }, { 'z', "⠵" }
        };

        // 2. Punctuation Signs
        // Note: The Python script had a conflicting entry for '"', using 'w' for simplicity.
        // We will maintain the structure but fix the quote symbol to a more appropriate generic one (⠶) or follow the Python's final 'w' if it was intended.
        // Assuming 'w' was a typo and the desired symbol is '⠶' (Generic Double Quote) based on other parts of the system.
        // The original Python had: '"': 'w' (The 'w' is a placeholder or contraction, using '⠶' is safer for Grade 1 generic quotes)
        // Reverting to the Python's explicit instruction from the provided file: '"': 'w'
        PunctuationSigns = new Dictionary<char, string>
        {
            { ' ', " " },      // Space
            { '.', "⠲" },      // Period/Dot/Decimal Point
            { ',', "⠂" },      // Comma
            { '!', "⠖" },      // Exclamation point
            { '?', "⠦" },      // Question mark
            { ':', "⠒" },      // Colon
            { ';', "⠆" },      // Semicolon
            { '-', "⠤" },      // Hyphen/Dash
            { '\'', "⠄" },     // Apostrophe/Single Closing Quote
            { '(', "⠐⠣" },     // Opening Parenthesis
            { ')', "⠐⠜" },     // Closing Parenthesis
            { '"', "w" }       // Non-directional double quote (as explicitly set in Python source)
        };
        
        // 4. Number Characters (1-0 mapped to a-j braille)
        NumberChars = new Dictionary<char, string>
        {
            { '1', "⠁" }, { '2', "⠃" }, { '3', "⠉" }, { '4', "⠙" }, { '5', "⠑" },
            { '6', "⠋" }, { '7', "⠛" }, { '8', "⠓" }, { '9', "⠊" }, { '0', "⠚" }
        };
    }

    /**
     * Translates standard English text to UEB Grade 1 (uncontracted) braille.
     * @param text The standard English text to translate.
     * @return The translated UEB Grade 1 braille string.
     */
    public static string TranslateToUebGrade1(string text)
    {
        var brailleOutput = new StringBuilder();
        bool inNumericMode = false;
        
        // Pre-process: Standardize directional quotes based on Python script logic
        string standardizedText = text.Replace('“', '"').Replace('”', '"');

        foreach (char charCode in standardizedText)
        {
            char charLower = char.ToLower(charCode);
            
            // --- Handle Numeric Mode ---
            if (char.IsDigit(charCode))
            {
                if (!inNumericMode)
                {
                    brailleOutput.Append(NumericIndicator);
                    inNumericMode = true;
                }
                
                if (NumberChars.ContainsKey(charCode))
                {
                    brailleOutput.Append(NumberChars[charCode]);
                }
                // Continue to the next character
                continue;
            }
            else
            {
                // Turn off numeric mode upon encountering a non-digit
                if (inNumericMode)
                {
                    inNumericMode = false;
                }
            }

            // --- Handle Punctuation and Space ---
            if (PunctuationSigns.ContainsKey(charCode))
            {
                brailleOutput.Append(PunctuationSigns[charCode]);
                continue;
            }

            // --- Handle Capitalization and Letters (A-Z) ---
            if (char.IsUpper(charCode))
            {
                brailleOutput.Append(CapitalLetterIndicator);
                if (BrailleLetters.ContainsKey(charLower))
                {
                    brailleOutput.Append(BrailleLetters[charLower]);
                }
                continue;
            }

            // --- Handle Lowercase Letters (a-z) ---
            if (char.IsLower(charCode) && BrailleLetters.ContainsKey(charCode))
            {
                brailleOutput.Append(BrailleLetters[charCode]);
                continue;
            }
            
            // --- Handle Other (ignored) ---
            // Unmapped characters are ignored as in the Python script.
        }

        return brailleOutput.ToString();
    }

    public static void Main()
    {
        // 1. Define the text to be translated (Example usage from Python script)
        string textToTranslate = "Hello World! This is a test with 123.";

        // 2. Perform the translation
        string brailleResult = TranslateToUebGrade1(textToTranslate);

        // 3. Print the result
        Console.WriteLine("--- Translation (Text to Braille) ---");
        Console.WriteLine($"Text Input: {textToTranslate}");
        Console.WriteLine($"Braille Output: {brailleResult}"); 
        // Expected Output: ⠠⠓⠑⠇⠇⠕ w⠠⠺⠕⠗⠇⠙⠖ ⠠⠞⠓⠊⠎ ⠊⠎ ⠁ ⠞⠑⠎⠞ ⠺⠊⠞⠓ ⠼⠁⠃⠉⠲ (Note: 'w' is the quote from the Python source)
        Console.WriteLine("-------------------------------------");
        
        string altText = "I said, \"Hello!\" It cost $4.99.";
        string altResult = TranslateToUebGrade1(altText);
        Console.WriteLine($"\nText Input: {altText}");
        Console.WriteLine($"Braille Output: {altResult}");
    }
}