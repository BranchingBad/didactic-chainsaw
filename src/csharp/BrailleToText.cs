using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

public static class BrailleToText 
{
    private static readonly IReadOnlyDictionary<string, string> BrailleMap;
    private static readonly IReadOnlyDictionary<string, string> DigitMap;

    private const string CapitalIndicator = "⠠";   
    private const string NumberSign = "⠼";         
    private const string Hyphen = "⠤";     
    private const string Dot5 = "⠐";

    static BrailleToText() 
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

            // Check for Capital Letter Indicator (Dot 6)
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

            // Check for Dot 5 (Parentheses prefix)
            if (currentChar == Dot5)
            {
                if (i + 1 < uebInput.Length)
                {
                    string nextBraille = uebInput[i + 1].ToString();
                    if (nextBraille == "⠣") // Opening paren
                    {
                        result.Append("(");
                        i += 2;
                        continue;
                    }
                    if (nextBraille == "⠜") // Closing paren
                    {
                        result.Append(")");
                        i += 2;
                        continue;
                    }
                }
                i += 1; // Skip Dot 5 if not valid prefix here
                continue;
            }

            // Check for Numeric Indicator
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
                        // Hyphen terminates numeric mode
                        inNumericMode = false;
                        result.Append('-');
                    } 
                    else if (currentChar == "⠲") // Period / Decimal
                    {
                        // Lookahead for digit to see if this is a decimal point
                        bool isDecimal = false;
                        if (i + 1 < uebInput.Length)
                        {
                            string nextC = uebInput[i + 1].ToString();
                            if (DigitMap.ContainsKey(nextC))
                            {
                                isDecimal = true;
                            }
                        }

                        result.Append(textChar); // Append '.'

                        if (!isDecimal)
                        {
                            inNumericMode = false;
                        }
                    }
                    else if (currentChar == "⠂") // Comma (stays in numeric mode)
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