// Assuming you have modified source files to export functions, 
// or you can load them via fs/eval if you want to keep source clean.
const textToBraille = require('../../src/javascript/text-to-braille');
const brailleToText = require('../../src/javascript/braille-to-text');

describe('Didactic Chainsaw JS Implementation', () => {
    
    test('Basic Alphabet', () => {
        expect(textToBraille.translateToUebGrade1("abc")).toBe("⠁⠃⠉");
        expect(brailleToText.translateUebGrade1ToText("⠁⠃⠉")).toBe("abc");
    });

    test('Numbers and Decimals', () => {
        expect(textToBraille.translateToUebGrade1("12.5")).toBe("⠼⠁⠃⠲⠑");
        expect(brailleToText.translateUebGrade1ToText("⠼⠁⠃⠲⠑")).toBe("12.5");
    });

    test('Parentheses handling', () => {
        // Dot 5 + 1-2-6 for opening, Dot 5 + 3-4-5 for closing
        expect(textToBraille.translateToUebGrade1("(A)")).toBe("⠐⠣⠠⠁⠐⠜");
        expect(brailleToText.translateUebGrade1ToText("⠐⠣⠠⠁⠐⠜")).toBe("(A)");
    });

    test('Round Trip Integrity', () => {
        const input = "Hello 123";
        const braille = textToBraille.translateToUebGrade1(input);
        const output = brailleToText.translateUebGrade1ToText(braille);
        expect(output).toBe(input);
    });
});