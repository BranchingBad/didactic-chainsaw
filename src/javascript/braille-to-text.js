function translateUebGrade1ToText(uebInput) {
    const BRAILLE_MAP = {
        '⠁': 'a', '⠃': 'b', '⠉': 'c', '⠙': 'd', '⠑': 'e', '⠋': 'f',
        '⠛': 'g', '⠓': 'h', '⠊': 'i', '⠚': 'j', '⠅': 'k', '⠇': 'l',
        '⠍': 'm', '⠝': 'n', '⠕': 'o', '⠏': 'p', '⠟': 'q', '⠗': 'r',
        '⠎': 's', '⠞': 't', '⠥': 'u', '⠧': 'v', '⠺': 'w', '⠭': 'x',
        '⠽': 'y', '⠵': 'z',
        ' ': ' ', '⠲': '.', '⠂': ',', '⠖': '!', '⠦': '?',
        '⠒': ':', '⠆': ';', '⠤': '-', '⠄': "'", '⠶': '"',
        '⠣': '(', '⠜': ')'
    };

    const CAPITAL_INDICATOR = '⠠';   
    const NUMBER_SIGN = '⠼';  
    const DOT_5 = '⠐';       

    const DIGIT_MAP = {
        '⠁': '1', '⠃': '2', '⠉': '3', '⠙': '4', '⠑': '5',
        '⠋': '6', '⠛': '7', '⠓': '8', '⠊': '9', '⠚': '0',
    };

    let result = [];
    let i = 0;
    let inNumericMode = false;

    while (i < uebInput.length) {
        const char = uebInput[i];

        // Capital Indicator
        if (char === CAPITAL_INDICATOR) {
            if (i + 1 < uebInput.length) {
                const nextBraille = uebInput[i + 1];
                if (BRAILLE_MAP.hasOwnProperty(nextBraille) && /[a-z]/.test(BRAILLE_MAP[nextBraille])) {
                    result.push(BRAILLE_MAP[nextBraille].toUpperCase());
                    i += 2;
                    continue;
                }
            }
            i += 1;
            continue;
        }

        // Dot 5 (Parentheses)
        if (char === DOT_5) {
             if (i + 1 < uebInput.length) {
                 const nextBraille = uebInput[i + 1];
                 if (nextBraille === '⠣') {
                     result.push('(');
                     i += 2; 
                     continue;
                 }
                 if (nextBraille === '⠜') {
                     result.push(')');
                     i += 2;
                     continue;
                 }
             }
             i += 1;
             continue;
        }

        // Numeric Indicator
        if (char === NUMBER_SIGN) {
            inNumericMode = true;
            i += 1;
            continue;
        }

        if (BRAILLE_MAP.hasOwnProperty(char)) {
            const textChar = BRAILLE_MAP[char];

            if (inNumericMode) {
                if (DIGIT_MAP.hasOwnProperty(char)) {
                    result.push(DIGIT_MAP[char]);
                } else if (char === '⠤') {
                    // Hyphen terminates numeric mode
                    inNumericMode = false;
                    result.push('-');
                } else if (char === '⠲') {
                    // Decimal Lookahead
                    let isDecimal = false;
                    if (i + 1 < uebInput.length) {
                        if (DIGIT_MAP.hasOwnProperty(uebInput[i + 1])) {
                            isDecimal = true;
                        }
                    }
                    result.push('.');
                    if (!isDecimal) {
                        inNumericMode = false;
                    }
                } else if (char === '⠂') {
                    // Comma continues numeric mode
                    result.push(textChar);
                } else {
                    inNumericMode = false;
                    result.push(textChar);
                }
            } else {
                result.push(textChar);
            }
        }
        i += 1;
    }

    return result.join('').trim();
}