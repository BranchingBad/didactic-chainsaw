using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

public static class BrailleToTextTranslator
{
    private static readonly IReadOnlyDictionary<string, string> BrailleMap;
    private static readonly IReadOnlyDictionary<string, string> DigitMap;

    private const string CapitalIndicator = "⠠";   
    private const string NumberSign = "⠼";         
    private const string Hyphen = "⠤";             

    static BrailleToTextTranslator()
    {
        BrailleMap = new Dictionary<string, string>
        {
            { "⠁", "a" }, { "⠃", "b" }, { "⠉", "c" }, { "⠙", "d" }, { "⠑", "e" }, { "⠋", "f" },
            { "⠛", "g" }, { "⠓", "h" }, { "⠊", "i" }, { "⠚", "j" }, { "⠅", "k" }, { "⠇", "l" },
            { "⠍", "m" }, { "⠝", "n" }, { "⠕", "o" }, { "⠏", "p" }, { "⠟", "q" }, { "⠗", "r" },
            { "⠎", "s" }, { "⠞", "t" }, { "⠥", "u" }, { "⠧", "v" }, { "⠺", "w" }, { "⠭", "x" },
            { "⠽", "y" }, { "⠵", "z" },
            { " ", " " }, { "⠲", "." }, { "⠂", "," }, { "⠖", "!" }, { "⠦", "?" },
            { "⠒", ":" }, { "⠆", ";" }, { "⠤", "-" }, { "⠄", "'" }, { "⠶", "\"" },
            { "⠣", "(" }, { "⠜", ")" }
        };

        DigitMap = new Dictionary<string, string>
        {
            { "⠁", "1" }, { "⠃", "2" }, { "⠉", "3" }, { "⠙", "4" }, { "⠑", "5" },
            { "⠋", "6" }, { "⠛", "7" }, { "⠓", "8" }, { "⠊", "9" }, { "⠚", "0" }
        };
    }

    public static string TranslateUebGrade1ToText(string uebInput)
    {
        var result = new StringBuilder();
        int i = 0;
        bool inNumericMode = false;

        while (i < uebInput.Length)
        {
            string currentChar = uebInput[i].ToString();

            if (currentChar == CapitalIndicator)
            {
                if (i + 1 < uebInput.Length)
                {
                    string nextBraille = uebInput[i + 1].ToString();
                    if (BrailleMap.TryGetValue(nextBraille, out string mappedText) && mappedText.Length == 1 && char.IsLetter(mappedText[0]))
                    {
                        result.Append(char.ToUpper(mappedText[0]));
                        i += 2; 
                        continue;
                    }
                }
                i += 1;
                continue;
            }

            if (currentChar == NumberSign)
            {
                inNumericMode = true;
                i += 1;
                continue;
            }

            if (BrailleMap.TryGetValue(currentChar, out string textChar))
            {
                if (inNumericMode)
                {
                    if (DigitMap.TryGetValue(currentChar, out string digit))
                    {
                        result.Append(digit);
                    } 
                    else if (currentChar == Hyphen)
                    {
                        result.Append('-');
                    } 
                    // Continue numeric mode for decimal (period) and comma
                    else if (currentChar == "⠲" || currentChar == "⠂")
                    {
                        result.Append(textChar);
                    }
                    else
                    {
                        inNumericMode = false;
                        result.Append(textChar);
                    }
                } 
                else 
                {
                    result.Append(textChar);
                }
            } 
            
            i += 1;
        }

        return result.ToString().Trim();
    }
}