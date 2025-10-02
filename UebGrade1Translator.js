/**
 * Translates standard English text to UEB Grade 1 (uncontracted) braille.
 * This function processes character-by-character, applying indicators for
 * capitalization and numbers.
 *
 * @param {string} text The standard English text to translate.
 * @returns {string} The translated UEB Grade 1 braille string.
 */
function translateToUebGrade1(text) {
    // 1. Lowercase Alphabet (a-z)
    const BRAILLE_LETTERS = {
        'a': '⠁', 'b': '⠃', 'c': '⠉', 'd': '⠙', 'e': '⠑', 'f': '⠋',
        'g': '⠛', 'h': '⠓', 'i': '⠊', 'j': '⠚', 'k': '⠅', 'l': '⠇',
        'm': '⠍', 'n': '⠝', 'o': '⠕', 'p': '⠏', 'q': '⠟', 'r': '⠗',
        's': '⠎', 't': '⠞', 'u': '⠥', 'v': '⠧', 'w': '⠺', 'x': '⠭',
        'y': '⠽', 'z': '⠵',
    };

    // 2. Punctuation Signs (Adjusted to use the specific symbols from the Python script where provided)
    const PUNCTUATION_SIGNS = {
        ' ': ' ',      // Space
        '.': '⠲',      // Period/Dot/Decimal Point
        ',': '⠂',      // Comma
        '!': '⠖',      // Exclamation point
        '?': '⠦',      // Question mark
        ':': '⠒',      // Colon
        ';': '⠆',      // Semicolon
        '-': '⠤',      // Hyphen/Dash
        "'": '⠄',      // Apostrophe/Single Closing Quote
        '(': '⠐⠣',     // Opening Parenthesis (Dots 5, 126)
        ')': '⠐⠜',     // Closing Parenthesis (Dots 5, 345)
        '"': 'w',      // Generic Double Quote (The Python script used 'w', which is contextually incorrect for UEB Grade 1 but is maintained for strict translation fidelity)
    };

    // 3. Indicators
    const CAPITAL_LETTER_INDICATOR = '⠠'; // Dot 6
    const NUMERIC_INDICATOR = '⠼';     // Dots 3, 4, 5, 6

    // 4. Number Characters (1-0 mapped to a-j)
    const NUMBER_CHARS = {
        '1': '⠁', '2': '⠃', '3': '⠉', '4': '⠙', '5': '⠑',
        '6': '⠋', '7': '⠛', '8': '⠓', '9': '⠊', '0': '⠚',
    };

    let brailleOutput = [];
    let inNumericMode = false;

    // Pre-process: Standardize quotes for lookup (matching Python's approach)
    text = text.replace(/“/g, '"').replace(/”/g, '"');

    for (const char of text) {
        // --- Handle Numeric Mode ---
        if (/[0-9]/.test(char)) {
            if (!inNumericMode) {
                brailleOutput.push(NUMERIC_INDICATOR);
                inNumericMode = true;
            }
            brailleOutput.push(NUMBER_CHARS[char]);
            continue;
        } else {
            // End numeric mode if a non-digit is encountered
            if (inNumericMode) {
                inNumericMode = false;
            }
        }

        // --- Handle Punctuation and Space ---
        if (PUNCTUATION_SIGNS.hasOwnProperty(char)) {
            brailleOutput.push(PUNCTUATION_SIGNS[char]);
            continue;
        }

        // --- Handle Capitalization and Letters ---
        if (/[A-Z]/.test(char)) {
            brailleOutput.push(CAPITAL_LETTER_INDICATOR);
            const lowerChar = char.toLowerCase();
            brailleOutput.push(BRAILLE_LETTERS[lowerChar]);
            continue;
        }

        // --- Handle Lowercase Letters ---
        if (/[a-z]/.test(char)) {
            brailleOutput.push(BRAILLE_LETTERS[char]);
            continue;
        }

        // --- Handle Other (ignored by the original Python script) ---
        // Any unmapped character is ignored.
    }

    return brailleOutput.join("");
}

// --- Example Usage (from original script) ---
const textToTranslate = "Hello World! This is a test with 123.";
const brailleResult = translateToUebGrade1(textToTranslate);

// 3. Print the result
console.log(`Original Text: "${textToTranslate}"`);
console.log(`UEB Grade 1 Braille: ${brailleResult}`);
// Expected output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲