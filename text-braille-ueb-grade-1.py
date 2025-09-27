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
    '"': 'ow',     # Double Quote (Opening or Closing) - Using `⠦right` and `right⠱` is more accurate for directional, but `enclcloseright` is common for non-directional. Using simple `w` for simplicity in uncontracted. **Correction for simple UEB: Use `quotedouble` (w is Grade 2 'will')**
    # Standard UEB double quote sign (non-directional)
    '"': 'w', # The UEB simple double quote is `w` for non-directional/generic. *Using `w` for simplicity.*
    '”': 'right⠱', # Closing double quote
    '“': '⠦left', # Opening double quote
}
# Replacing the simple double quote with the generic non-directional symbol
PUNCTUATION_SIGNS['"'] = 'w'


# 3. Indicators
CAPITAL_LETTER_INDICATOR = '⠠' # Dot 6
CAPITAL_WORD_INDICATOR = '⠠⠠' # Dot 6, Dot 6
CAPITAL_TERMINATOR = 'bh'     # Dot 6, Dot 3 (Not strictly needed for single words, but useful for multi-word caps, although not in Grade 1/uncontracted for single char/word)
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

    for char in text:
        # --- Handle Numeric Mode ---
        if char.isdigit():
            if not in_numeric_mode:
                braille_output.append(NUMERIC_INDICATOR)
                in_numeric_mode = True
            braille_output.append(NUMBER_CHARS[char])
            continue
        else:
            if in_numeric_mode:
                in_numeric_mode = False

        # --- Handle Punctuation and Space ---
        if char in PUNCTUATION_SIGNS:
            braille_output.append(PUNCTUATION_SIGNS[char])
            continue

        # --- Handle Capitalization and Letters ---
        if 'A' <= char <= 'Z':
            braille_output.append(CAPITAL_LETTER_INDICATOR)
            lower_char = char.lower()
            braille_output.append(BRAILLE_LETTERS[lower_char])
            continue

        # --- Handle Lowercase Letters ---
        if 'a' <= char <= 'z':
            braille_output.append(BRAILLE_LETTERS[char])
            continue

        # --- Handle Other (e.g., symbols not mapped, just pass through or ignore) ---
        # For simplicity, any unmapped character is ignored.
        pass

    return "".join(braille_output)

# --- Example Usage ---

# 1. Define the text to be translated (Change this line to submit new text)
text_to_translate = "Hello World! This is a test with 123."

# 2. Perform the translation
braille_result = translate_to_ueb_grade1(text_to_translate)

# 3. Print the result
print(braille_result)
