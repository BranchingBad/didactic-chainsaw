function translateToUebGrade1(text) {
    const BRAILLE_LETTERS = {
        'a': '⠁', 'b': '⠃', 'c': '⠉', 'd': '⠙', 'e': '⠑', 'f': '⠋',
        'g': '⠛', 'h': '⠓', 'i': '⠊', 'j': '⠚', 'k': '⠅', 'l': '⠇',
        'm': '⠍', 'n': '⠝', 'o': '⠕', 'p': '⠏', 'q': '⠟', 'r': '⠗',
        's': '⠎', 't': '⠞', 'u': '⠥', 'v': '⠧', 'w': '⠺', 'x': '⠭',
        'y': '⠽', 'z': '⠵',
    };

    const PUNCTUATION_SIGNS = {
        ' ': ' ', '.': '⠲', ',': '⠂', '!': '⠖', '?': '⠦',
        ':': '⠒', ';': '⠆', '-': '⠤', "'": '⠄',
        '(': '⠐⠣', ')': '⠐⠜', '"': '⠶'
    };

    const CAPITAL_LETTER_INDICATOR = '⠠'; 
    const NUMERIC_INDICATOR = '⠼';     

    const NUMBER_CHARS = {
        '1': '⠁', '2': '⠃', '3': '⠉', '4': '⠙', '5': '⠑',
        '6': '⠋', '7': '⠛', '8': '⠓', '9': '⠊', '0': '⠚',
    };

    let brailleOutput = [];
    let inNumericMode = false;

    text = text.replace(/“/g, '"').replace(/”/g, '"');

    for (let i = 0; i < text.length; i++) {
        const char = text[i];

        if (/[0-9]/.test(char)) {
            if (!inNumericMode) {
                brailleOutput.push(NUMERIC_INDICATOR);
                inNumericMode = true;
            }
            brailleOutput.push(NUMBER_CHARS[char]);
            continue;
        }
        
        // Handle Decimal
        if (char === '.' && inNumericMode) {
            if (i + 1 < text.length && /[0-9]/.test(text[i + 1])) {
                brailleOutput.push(PUNCTUATION_SIGNS['.']);
                continue; // Maintain numeric mode
            }
        }

        if (inNumericMode) {
            inNumericMode = false;
        }

        if (PUNCTUATION_SIGNS.hasOwnProperty(char)) {
            brailleOutput.push(PUNCTUATION_SIGNS[char]);
            continue;
        }

        if (/[A-Z]/.test(char)) {
            brailleOutput.push(CAPITAL_LETTER_INDICATOR);
            const lowerChar = char.toLowerCase();
            brailleOutput.push(BRAILLE_LETTERS[lowerChar]);
            continue;
        }

        if (/[a-z]/.test(char)) {
            brailleOutput.push(BRAILLE_LETTERS[char]);
            continue;
        }
    }

    return brailleOutput.join("");
}