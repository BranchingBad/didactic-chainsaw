import re

# --- UEB Grade 1 Mappings ---

# 1. Lowercase Alphabet (a-z)
BRAILLE_LETTERS = {
    'a': '⠁', 'b': '⠃', 'c': '⠉', 'd': '⠙', 'e': '⠑', 'f': '⠋',
    'g': '⠛', 'h': '⠓', 'i': '⠊', 'j': '⠚', 'k': '⠅', 'l': '⠇',
    'm': '⠍', 'n': '⠝', 'o': '⠕', 'p': '⠏', 'q': '⠟', 'r': '⠗',
    's': '⠎', 't': '⠞', 'u': '⠥', 'v': '⠧', 'w': '⠺', 'x': '⠭',
    'y': '⠽', 'z': '⠵',
}

# 2. Punctuation Signs
PUNCTUATION_SIGNS = {
    # Basic Punctuation
    ' ': ' ',      # Space
    '.': '⠲',      # Period/Dot/Decimal Point
    ',': '⠂',      # Comma
    '!': '⠖',      # Exclamation point
    '?': '⠦',      # Question mark
    ':': '⠒',      # Colon
    ';': '⠆',      # Semicolon
    '-': '⠤',      # Hyphen/Dash
    "'": '⠄',      # Apostrophe/Single Closing Quote
    '(': '⠐⠣',     # Opening Parenthesis
    ')': '⠐⠜',     # Closing Parenthesis
    # Standard UEB double quote sign (non-directional)
    '"': '⠶',      # Generic Double Quote (Dots 2-3-5-6)
    '”': '⠶',      # Closing double quote mapped to generic
    '“': '⠶',      # Opening double quote mapped to generic
}

# 3. Indicators
CAPITAL_LETTER_INDICATOR = '⠠' # Dot 6
CAPITAL_WORD_INDICATOR = '⠠⠠' # Dot 6, Dot 6
CAPITAL_TERMINATOR = 'bh'     # Dot 6, Dot 3
NUMERIC_INDICATOR = '⠼'     # Dots 3, 4, 5, 6

# 4. Number Characters (1-0 mapped to a-j)
NUMBER_CHARS = {
    '1': '⠁', '2': '⠃', '3': '⠉', '4': '⠙', '5': '⠑',
    '6': '⠋', '7': '⠛', '8': '⠓', '9': '⠊', '0': '⠚',
}


def translate_to_ueb_grade1(text):
    """
    Translates standard English text to UEB Grade 1 (uncontracted) braille.
    This function processes character-by-character, applying indicators for
    capitalization and numbers.
    """
    braille_output = []
    in_numeric_mode = False
    
    # Pre-process: Replace known punctuation with standard forms if necessary
    text = text.replace('“', '"').replace('”', '"') # Standardize quotes for lookup

    i = 0
    while i < len(text):
        char = text[i]
        
        # --- Handle Numeric Mode ---
        if char.isdigit():
            if not in_numeric_mode:
                braille_output.append(NUMERIC_INDICATOR)
                in_numeric_mode = True
            braille_output.append(NUMBER_CHARS[char])
            i += 1
            continue
            
        # --- Handle Decimal in Numeric Mode ---
        # If we see a period while in numeric mode, check if the NEXT char is a digit.
        # If so, it's a decimal point and numeric mode persists.
        if char == '.' and in_numeric_mode:
            if i + 1 < len(text) and text[i+1].isdigit():
                braille_output.append(PUNCTUATION_SIGNS['.'])
                # Do NOT turn off numeric mode
                i += 1
                continue
            else:
                # Otherwise, it's just a period ending the number
                in_numeric_mode = False
                braille_output.append(PUNCTUATION_SIGNS['.'])
                i += 1
                continue

        # For any other character, numeric mode ends (unless it was a comma inside a number, handled similarly, but keeping simple)
        if in_numeric_mode:
            in_numeric_mode = False

        # --- Handle Punctuation and Space ---
        if char in PUNCTUATION_SIGNS:
            braille_output.append(PUNCTUATION_SIGNS[char])
            i += 1
            continue

        # --- Handle Capitalization and Letters ---
        if 'A' <= char <= 'Z':
            braille_output.append(CAPITAL_LETTER_INDICATOR)
            lower_char = char.lower()
            braille_output.append(BRAILLE_LETTERS[lower_char])
            i += 1
            continue

        # --- Handle Lowercase Letters ---
        if 'a' <= char <= 'z':
            braille_output.append(BRAILLE_LETTERS[char])
            i += 1
            continue

        # --- Handle Other (e.g., symbols not mapped, just pass through or ignore) ---
        i += 1
        pass

    return "".join(braille_output)

# --- Example Usage ---

# 1. Define the text to be translated (Change this line to submit new text)
text_to_translate = "Hello World! This is a test with 123.45 and \"quotes\"."

# 2. Perform the translation
braille_result = translate_to_ueb_grade1(text_to_translate)

# 3. Print the result
print(braille_result)