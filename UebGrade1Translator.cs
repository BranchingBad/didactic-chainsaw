using System;
using System.Collections.Generic;
using System.Text;

public static class UebGrade1Translator
{
    private static readonly IReadOnlyDictionary<char, string> BrailleLetters;
    private static readonly IReadOnlyDictionary<char, string> PunctuationSigns;
    private static readonly IReadOnlyDictionary<char, string> NumberChars;

    private const string CapitalLetterIndicator = "⠠"; 
    private const string NumericIndicator = "⠼";     

    static UebGrade1Translator()
    {
        BrailleLetters = new Dictionary<char, string>
        {
            { 'a', "⠁" }, { 'b', "⠃" }, { 'c', "⠉" }, { 'd', "⠙" }, { 'e', "⠑" }, { 'f', "⠋" },
            { 'g', "⠛" }, { 'h', "⠓" }, { 'i', "⠊" }, { 'j', "⠚" }, { 'k', "⠅" }, { 'l', "⠇" },
            { 'm', "⠍" }, { 'n', "⠝" }, { 'o', "⠕" }, { 'p', "⠏" }, { 'q', "⠟" }, { 'r', "⠗" },
            { 's', "⠎" }, { 't', "⠞" }, { 'u', "⠥" }, { 'v', "⠧" }, { 'w', "⠺" }, { 'x', "⠭" },
            { 'y', "⠽" }, { 'z', "⠵" }
        };

        PunctuationSigns = new Dictionary<char, string>
        {
            { ' ', " " },      
            { '.', "⠲" },      
            { ',', "⠂" },      
            { '!', "⠖" },      
            { '?', "⠦" },      
            { ':', "⠒" },      
            { ';', "⠆" },      
            { '-', "⠤" },      
            { '\'', "⠄" },     
            { '(', "⠐⠣" },     
            { ')', "⠐⠜" },     
            { '"', "⠶" }       // Corrected from 'w' to Generic Double Quote
        };
        
        NumberChars = new Dictionary<char, string>
        {
            { '1', "⠁" }, { '2', "⠃" }, { '3', "⠉" }, { '4', "⠙" }, { '5', "⠑" },
            { '6', "⠋" }, { '7', "⠛" }, { '8', "⠓" }, { '9', "⠊" }, { '0', "⠚" }
        };
    }

    public static string TranslateToUebGrade1(string text)
    {
        var brailleOutput = new StringBuilder();
        bool inNumericMode = false;
        
        string standardizedText = text.Replace('“', '"').Replace('”', '"');

        for (int i = 0; i < standardizedText.Length; i++)
        {
            char charCode = standardizedText[i];
            char charLower = char.ToLower(charCode);
            
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
                continue;
            }
            
            // Check for decimal point inside number
            if (charCode == '.' && inNumericMode)
            {
                 if (i + 1 < standardizedText.Length && char.IsDigit(standardizedText[i+1]))
                 {
                     brailleOutput.Append(PunctuationSigns['.']);
                     continue; // Keep numeric mode
                 }
            }

            if (inNumericMode)
            {
                inNumericMode = false;
            }

            if (PunctuationSigns.ContainsKey(charCode))
            {
                brailleOutput.Append(PunctuationSigns[charCode]);
                continue;
            }

            if (char.IsUpper(charCode))
            {
                brailleOutput.Append(CapitalLetterIndicator);
                if (BrailleLetters.ContainsKey(charLower))
                {
                    brailleOutput.Append(BrailleLetters[charLower]);
                }
                continue;
            }

            if (char.IsLower(charCode) && BrailleLetters.ContainsKey(charCode))
            {
                brailleOutput.Append(BrailleLetters[charCode]);
                continue;
            }
        }

        return brailleOutput.ToString();
    }

    public static void Main()
    {
        Console.WriteLine(TranslateToUebGrade1("123.45 and \"quotes\""));
    }
}