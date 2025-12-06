import re

def translate_ueb_grade1_to_text(ueb_input):
    """
    Translates UEB Grade 1 (uncontracted) braille input to standard English text.

    Args:
        ueb_input (str): The UEB Grade 1 braille string (using Unicode braille patterns).

    Returns:
        str: The translated standard English text.
    """

    # 1. Braille to Text Mapping
    BRAILLE_MAP = {
        # Lowercase Letters (a-z)
        '⠁': 'a', '⠃': 'b', '⠉': 'c', '⠙': 'd', '⠑': 'e', '⠋': 'f',
        '⠛': 'g', '⠓': 'h', '⠊': 'i', '⠚': 'j', '⠅': 'k', '⠇': 'l',
        '⠍': 'm', '⠝': 'n', '⠕': 'o', '⠏': 'p', '⠟': 'q', '⠗': 'r',
        '⠎': 's', '⠞': 't', '⠥': 'u', '⠧': 'v', '⠺': 'w', '⠭': 'x',
        '⠽': 'y', '⠵': 'z',

        # Punctuation and Symbols
        ' ': ' ',   # Space
        '⠲': '.',   # Period
        '⠂': ',',   # Comma
        '⠖': '!',   # Exclamation point
        '⠦': '?',   # Question mark
        '⠒': ':',   # Colon
        '⠆': ';',   # Semicolon
        '⠤': '-',   # Hyphen / Dash
        '⠄': "'",   # Apostrophe
        '⠶': '"',   # Generic Double Quote
        '⠣': '(',   # Opening Parenthesis
        '⠜': ')',   # Closing Parenthesis
    }

    # 2. Indicators
    CAPITAL_INDICATOR = '⠠'   # Dot 6
    NUMBER_SIGN = '⠼'         # Dots 3456
    GRADE1_INDICATOR = '⠐'    # Dot 5 (Used for parentheses prefix here)

    # Braille digits (a-j) to Text digits (1-0)
    DIGIT_MAP = {
        '⠁': '1', '⠃': '2', '⠉': '3', '⠙': '4', '⠑': '5',
        '⠋': '6', '⠛': '7', '⠓': '8', '⠊': '9', '⠚': '0',
    }

    # 3. Translation Logic
    result = []
    i = 0
    in_capital_mode = False
    in_numeric_mode = False

    while i < len(ueb_input):
        char = ueb_input[i]

        # Check for Capital Letter Indicator (⠠)
        if char == CAPITAL_INDICATOR:
            if i + 1 < len(ueb_input):
                next_braille = ueb_input[i + 1]
                if next_braille in BRAILLE_MAP and BRAILLE_MAP[next_braille].isalpha():
                    result.append(BRAILLE_MAP[next_braille].upper())
                    i += 2
                    continue
            i += 1
            continue
            
        # Check for Grade 1 Indicator / Dot 5 Prefix (⠐) - Handling Parentheses
        elif char == GRADE1_INDICATOR:
            if i + 1 < len(ueb_input):
                next_braille = ueb_input[i + 1]
                if next_braille == '⠣': # Opening Parenthesis
                    result.append('(')
                    i += 2
                    continue
                elif next_braille == '⠜': # Closing Parenthesis
                    result.append(')')
                    i += 2
                    continue
            # If Dot 5 is standalone or followed by something else, treat as empty or generic
            i += 1
            continue

        # Check for Numeric Indicator (⠼)
        elif char == NUMBER_SIGN:
            in_numeric_mode = True
            i += 1
            continue

        # Handle characters based on current mode
        elif char in BRAILLE_MAP:
            text_char = BRAILLE_MAP[char]

            if in_numeric_mode:
                if char in DIGIT_MAP:
                    result.append(DIGIT_MAP[char])
                    
                elif char == '⠤': # Hyphen
                    # Hyphen terminates numeric mode in UEB (e.g. 1-a)
                    in_numeric_mode = False
                    result.append('-')
                    
                elif char == '⠲': # Period / Decimal
                    # Lookahead: Only treat as decimal if next char is a digit
                    is_decimal = False
                    if i + 1 < len(ueb_input):
                         next_c = ueb_input[i+1]
                         if next_c in DIGIT_MAP:
                             is_decimal = True
                    
                    result.append(text_char)
                    if not is_decimal:
                        in_numeric_mode = False
                        
                elif char == '⠂': # Comma
                    # Numeric mode continues for comma (digit separator)
                    result.append(text_char)
                    
                else:
                    # Anything else ends numeric mode
                    in_numeric_mode = False
                    result.append(text_char)
            else:
                # Regular letter or punctuation
                result.append(text_char.upper() if in_capital_mode and text_char.isalpha() else text_char)
                if text_char in ['.', '!', '?', ':', ';', ',', ')', '"', ' ']:
                    in_capital_mode = False 

        else:
            # Unknown character
            result.append('')

        i += 1

    return "".join(result).strip()

# --- Example Usage ---
# UEB Input (Standardized decimal): Hello World! 123.45
UEB_INPUT = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠼⠁⠃⠉⠲⠙⠑"

print("--- Translation ---")
translated_text = translate_ueb_grade1_to_text(UEB_INPUT)
print(f"UEB Grade 1: {UEB_INPUT}")
print(f"Text Output: {translated_text}")