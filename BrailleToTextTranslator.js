/**
 * Translates UEB Grade 1 (uncontracted) braille input to standard English text.
 * This script processes the braille string character by character, managing
 * indicator modes for capitalization and numbers.
 *
 * @param {string} uebInput The UEB Grade 1 braille string (using Unicode braille patterns).
 * @returns {string} The translated standard English text.
 */
function translateUebGrade1ToText(uebInput) {
    // 1. Braille to Text Mapping (Letters, Punctuation, Spacing)
    const BRAILLE_MAP = {
        // Lowercase Letters (a-z)
        '⠁': 'a', '⠃': 'b', '⠉': 'c', '⠙': 'd', '⠑': 'e', '⠋': 'f',
        '⠛': 'g', '⠓': 'h', '⠊': 'i', '⠚': 'j', '⠅': 'k', '⠇': 'l',
        '⠍': 'm', '⠝': 'n', '⠕': 'o', '⠏': 'p', '⠟': 'q', '⠗': 'r',
        '⠎': 's', '⠞': 't', '⠥': 'u', '⠧': 'v', '⠺': 'w', '⠭': 'x',
        '⠽': 'y', '⠵': 'z',

        // Punctuation and Symbols
        ' ': ' ',   // Space
        '⠲': '.',   // Period
        '⠂': ',',   // Comma
        '⠖': '!',   // Exclamation point
        '⠦': '?',   // Question mark
        '⠒': ':',   // Colon
        '⠆': ';',   // Semicolon
        '⠤': '-',   // Hyphen / Dash
        '⠄': "'",   // Apostrophe
        '⠶': '"',   // Opening/Closing Quotation Mark
        '⠣': '(',   // Opening Parenthesis
        '⠜': ')',   // Closing Parenthesis
    };

    // 2. Indicators and Special Modes
    const CAPITAL_INDICATOR = '⠠';   // Dot 6 (Single Capital)
    const NUMBER_SIGN = '⠼';         // Dots 3456
    const DECIMAL_POINT_BRAILLE = '⠨'; // Dot 6, 3 (Used for decimal/dot after a number in this context)

    // Braille digits (a-j) to Text digits (1-0)
    const DIGIT_MAP = {
        '⠁': '1', '⠃': '2', '⠉': '3', '⠙': '4', '⠑': '5',
        '⠋': '6', '⠛': '7', '⠓': '8', '⠊': '9', '⠚': '0',
    };

    // 3. Translation Logic
    let result = [];
    let i = 0;
    let inNumericMode = false;
    let inCapitalWordMode = false; // Added for completeness, though not strictly needed for this input

    while (i < uebInput.length) {
        const char = uebInput[i];

        // --- Check for Indicators ---

        // Check for Capital Word Indicator (⠠⠠) - Not implemented as it wasn't in the Python logic,
        // but the Python logic *had* the variable in_capital_mode, which we will use here.
        // For this translation, we'll only check for the single capital indicator as per the loop logic.

        // Check for Capital Letter Indicator (⠠)
        if (char === CAPITAL_INDICATOR) {
            if (i + 1 < uebInput.length) {
                const nextBraille = uebInput[i + 1];
                if (BRAILLE_MAP.hasOwnProperty(nextBraille) && /[a-z]/.test(BRAILLE_MAP[nextBraille])) {
                    // Apply capitalization to the next letter
                    result.push(BRAILLE_MAP[nextBraille].toUpperCase());
                    i += 2;
                    continue;
                }
            }
            // If followed by non-letter or end of string, skip the indicator and move to the next char
            i += 1;
            continue;
        }

        // Check for Numeric Indicator (⠼)
        if (char === NUMBER_SIGN) {
            inNumericMode = true;
            i += 1;
            continue;
        }

        // Check for the Decimal Point (⠨)
        if (char === DECIMAL_POINT_BRAILLE) {
            // As per the Python logic for the .38 example, treat ⠨ as '.'
            result.push('.');
            // Punctuation, including the decimal, terminates numeric mode
            inNumericMode = false;
            i += 1;
            continue;
        }

        // --- Handle Mapped Characters ---
        if (BRAILLE_MAP.hasOwnProperty(char)) {
            const textChar = BRAILLE_MAP[char];

            if (inNumericMode) {
                if (DIGIT_MAP.hasOwnProperty(char)) {
                    // It's a digit
                    result.push(DIGIT_MAP[char]);
                } else if (char === '⠤') {
                    // It's a hyphen, which maintains numeric mode
                    result.push('-');
                } else {
                    // It's a non-digit, non-hyphen character (e.g., letter, space, other punctuation)
                    // End numeric mode
                    inNumericMode = false;

                    // If it's a letter, apply capital word mode if active (though not set in the Python code)
                    if (/[a-z]/.test(textChar)) {
                        result.push(inCapitalWordMode ? textChar.toUpperCase() : textChar);
                    } else {
                        // It's punctuation or a space
                        result.push(textChar);
                    }
                }
            } else {
                // Not in numeric mode: Regular letter or punctuation
                result.push(inCapitalWordMode && /[a-z]/.test(textChar) ? textChar.toUpperCase() : textChar);

                // Punctuation or space turns off capital word mode
                if (/[.!,?:;)"'\s]/.test(textChar)) {
                    inCapitalWordMode = false;
                }
            }
        }

        i += 1;
    }

    // Return the joined result, trimming any excess space as per original Python script
    return result.join('').trim();
}

// --- Example Usage (from original script) ---
const UEB_INPUT = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠂⠀⠼⠙⠑⠤⠽⠑⠁⠗⠤⠕⠇⠙⠀⠠⠎⠁⠗⠁⠀⠠⠚⠁⠝⠑⠀⠠⠍⠕⠕⠗⠑⠀⠙⠗⠕⠏⠏⠑⠙⠀⠓⠑⠗⠀⠎⠕⠝⠀⠕⠋⠋⠀⠁⠞⠀⠓⠊⠎⠀⠠⠎⠁⠝⠀⠠⠋⠗⠁⠝⠉⠊⠎⠉⠕⠀⠎⠉⠓⠕⠕⠇⠂⠀⠧⠊⠎⠊⠞⠑⠙⠀⠁⠀⠏⠗⠊⠧⠁⠞⠑⠀⠛⠥⠝⠀⠙⠑⠁⠇⠑⠗⠀⠁⠝⠙⠂⠀⠊⠝⠀⠺⠓⠁⠞⠀⠎⠓⠑⠀⠇⠁⠞⠑⠗⠀⠞⠕⠇⠙⠀⠞⠓⠑⠀⠠⠇⠕⠎⠀⠠⠁⠝⠛⠑⠇⠑⠎⠀⠠⠞⠊⠍⠑⠎⠀⠺⠁⠎⠀⠁⠀⠶⠁⠀⠅⠊⠝⠙⠀⠕⠋⠀⠥⠇⠞⠊⠍⠁⠞⠑⠀⠏⠗⠕⠞⠑⠎⠞⠀⠁⠛⠁⠊⠝⠎⠞⠀⠞⠓⠑⠀⠎⠽⠎⠞⠑⠍⠂⠴⠀⠙⠗⠑⠺⠀⠁⠀⠨⠼⠉⠓⠤⠉⠁⠇⠊⠃⠗⠑⠀⠏⠊⠎⠞⠕⠇⠀⠕⠥⠞⠎⠊⠙⠑⠀⠁⠀⠓⠕⠞⠑⠇⠀⠇⠁⠞⠑⠗⠀⠊⠝⠀⠞⠓⠑⠀⠙⠁⠽⠂⠀⠋⠊⠗⠊⠝⠛⠀⠁⠞⠀⠞⠓⠑⠝⠤⠏⠗⠑⠎⠊⠙⠑⠝⠞⠀⠠⠛⠑⠗⠁⠇⠙⠀⠠⠋⠕⠗⠙⠲";
const ALTERNATIVE_UEB_INPUT = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠊⠀⠓⠁⠧⠑⠀⠼⠁⠚⠀⠁⠏⠏⠇⠑⠎⠲";

console.log("--- Translation of the Original UEB Input ---");
let translatedText = translateUebGrade1ToText(UEB_INPUT);
console.log(`UEB Grade 1: ${UEB_INPUT}`);
console.log(`Text Output: ${translatedText}`);
console.log("-".repeat(40));

console.log("--- Translation of an Alternative UEB Input ---");
let translatedAlternative = translateUebGrade1ToText(ALTERNATIVE_UEB_INPUT);
console.log(`UEB Grade 1: ${ALTERNATIVE_UEB_INPUT}`);
console.log(`Text Output: ${translatedAlternative}`);
console.log("-".repeat(40));