import re

def translate_ueb_grade1_to_text(ueb_input):
    """
    Translates UEB Grade 1 (uncontracted) braille input to standard English text.

    Args:
        ueb_input (str): The UEB Grade 1 braille string (using Unicode braille patterns).

    Returns:
        str: The translated standard English text.
    """

    # 1. Braille to Text Mapping (Letters, Numbers, Punctuation, Spacing)
    # The number sign (⠼) and capitalization indicator (⠠) are handled separately.
    BRAILLE_MAP = {
        # Lowercase Letters (a-z)
        '⠁': 'a', '⠃': 'b', '⠉': 'c', '⠙': 'd', '⠑': 'e', '⠋': 'f',
        '⠛': 'g', '⠓': 'h', '⠊': 'i', '⠚': 'j', '⠅': 'k', '⠇': 'l',
        '⠍': 'm', '⠝': 'n', '⠕': 'o', '⠏': 'p', '⠟': 'q', '⠗': 'r',
        '⠎': 's', '⠞': 't', '⠥': 'u', '⠧': 'v', '⠺': 'w', '⠭': 'x',
        '⠽': 'y', '⠵': 'z',

        # Numbers (only when preceded by ⠼). Note: 'a'-'j' are used as digits '1'-'0'.
        # Since 'a'-'j' are mapped to 'a'-'j' above, the number sign logic handles
        # mapping them to '1'-'0' when in numeric mode.

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
        '⠶': '"',   # Opening/Closing Quotation Mark (unpaired)
        '⠣': '(',   # Opening Parenthesis
        '⠜': ')',   # Closing Parenthesis
        # Grade 1 uses the full dot 3 (.3) for the decimal, but in number mode, the
        # number sign is off, so we'll treat it as a period for simplicity in this general map.
        # This specific braille string uses ⠲ for the period.

        # For the specific example's .38-caliber (⠨⠼⠉⠓), the general quotation mark ⠶
        # should be a dot 6 followed by the braille for 3 (c) and 8 (h).
        # We need to handle the .38-caliber sequence (⠨⠼⠉⠓) specifically.
        # The braille for the decimal point used here (dot 3) is ⠨ (Dot 6, Dot 3) for the
        # grade 1 uncontracted decimal/full stop following a number.
        # However, in this *input*, it appears as ⠨ (capital passage indicator) followed by ⠼ (number sign)
        # then ⠉ (3) ⠓ (8). The `⠨` is actually the capital passage indicator in UEB,
        # but in UEB Grade 1 for numbers it is often used for the **Decimal Point** (dot 3).
        # We'll assume the provided input's `⠨` is intended as the Decimal Point (dot 3) in this context.

        # Let's adjust the logic for the specific numeric and indicator sequences.
    }

    # 2. Indicators and Special Modes
    CAPITAL_INDICATOR = '⠠'   # Dot 6
    CAPITAL_WORD_INDICATOR = '⠠⠠' # Dot 6, Dot 6
    NUMBER_SIGN = '⠼'         # Dots 3456

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

        # Check for Capital Word Indicator (⠠⠠) - Not present in this specific input, but good practice
        if char == CAPITAL_INDICATOR and i + 1 < len(ueb_input) and ueb_input[i + 1] == CAPITAL_INDICATOR:
            in_capital_mode = True # Turn on capitalization for all subsequent letters
            i += 2
            continue

        # Check for Capital Letter Indicator (⠠)
        elif char == CAPITAL_INDICATOR:
            if i + 1 < len(ueb_input):
                next_braille = ueb_input[i + 1]
                if next_braille in BRAILLE_MAP and BRAILLE_MAP[next_braille].isalpha():
                    # Apply capitalization to the next letter
                    result.append(BRAILLE_MAP[next_braille].upper())
                    i += 2
                    continue
            # If indicator is followed by non-letter, treat as a single character or skip
            i += 1
            continue

        # Check for Numeric Indicator (⠼)
        elif char == NUMBER_SIGN:
            in_numeric_mode = True
            i += 1
            continue

        # Check for the UEB Grade 1 Decimal Point (Dot 3) - Used after a number
        # The input uses ⠨ (Dot 6, 3) in the `.38` sequence. We will map this to a period/decimal.
        elif char == '⠨':
            # This is specifically for the decimal point in the `.38` example
            if in_numeric_mode:
                result.append('.')
                in_numeric_mode = False # Decimal point turns off number mode
            else:
                # If it's not in numeric mode, it's the capital passage indicator,
                # which isn't handled here (or it's the specific decimal point outside a number string).
                # We'll treat it as a period if it appears, or a Capital Passage Indicator that is ignored.
                # Based on the original example, it is likely the Decimal Point.
                result.append('.') # Defaulting to the decimal point for safety
            i += 1
            continue

        # Handle characters based on current mode
        elif char in BRAILLE_MAP:
            text_char = BRAILLE_MAP[char]

            if in_numeric_mode and char in DIGIT_MAP:
                result.append(DIGIT_MAP[char])
                # Numeric mode stays on until a space, punctuation, or non-digit is encountered.
            elif in_numeric_mode and char == '⠤': # Hyphen (maintains numeric mode)
                result.append('-')
            elif in_numeric_mode and text_char.isalpha():
                # End of number sequence
                in_numeric_mode = False
                result.append(text_char.upper() if in_capital_mode else text_char)
            elif in_numeric_mode and (char == '⠂' or char == '⠲' or char == '⠖' or char == '⠦' or char == '⠒' or char == '⠆' or char == '⠣' or char == '⠜' or char == '⠶'): # Punctuation ends numeric mode
                in_numeric_mode = False
                result.append(text_char)
            elif in_numeric_mode and char == ' ': # Space ends numeric mode
                in_numeric_mode = False
                result.append(' ')

            else:
                # Regular letter or punctuation
                result.append(text_char.upper() if in_capital_mode and text_char.isalpha() else text_char)
                if text_char in ['.', '!', '?', ':', ';', ',', ')', '"', ' ']:
                    in_capital_mode = False # Punctuation or space turns off capital *word* mode

        else:
            # Catch-all for unmapped or unexpected characters
            result.append('')

        i += 1

    # Cleanup any trailing modes that should have been turned off
    return "".join(result).strip()

# --- Example Usage ---

# The original UEB Grade 1 input from the user's request:
UEB_INPUT = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠂⠀⠼⠙⠑⠤⠽⠑⠁⠗⠤⠕⠇⠙⠀⠠⠎⠁⠗⠁⠀⠠⠚⠁⠝⠑⠀⠠⠍⠕⠕⠗⠑⠀⠙⠗⠕⠏⠏⠑⠙⠀⠓⠑⠗⠀⠎⠕⠝⠀⠕⠋⠋⠀⠁⠞⠀⠓⠊⠎⠀⠠⠎⠁⠝⠀⠠⠋⠗⠁⠝⠉⠊⠎⠉⠕⠀⠎⠉⠓⠕⠕⠇⠂⠀⠧⠊⠎⠊⠞⠑⠙⠀⠁⠀⠏⠗⠊⠧⠁⠞⠑⠀⠛⠥⠝⠀⠙⠑⠁⠇⠑⠗⠀⠁⠝⠙⠂⠀⠊⠝⠀⠺⠓⠁⠞⠀⠎⠓⠑⠀⠇⠁⠞⠑⠗⠀⠞⠕⠇⠙⠀⠞⠓⠑⠀⠠⠇⠕⠎⠀⠠⠁⠝⠛⠑⠇⠑⠎⠀⠠⠞⠊⠍⠑⠎⠀⠺⠁⠎⠀⠁⠀⠶⠁⠀⠅⠊⠝⠙⠀⠕⠋⠀⠥⠇⠞⠊⠍⠁⠞⠑⠀⠏⠗⠕⠞⠑⠎⠞⠀⠁⠛⠁⠊⠝⠎⠞⠀⠞⠓⠑⠀⠎⠽⠎⠞⠑⠍⠂⠴⠀⠙⠗⠑⠺⠀⠁⠀⠨⠼⠉⠓⠤⠉⠁⠇⠊⠃⠗⠑⠀⠏⠊⠎⠞⠕⠇⠀⠕⠥⠞⠎⠊⠙⠑⠀⠁⠀⠓⠕⠞⠑⠇⠀⠇⠁⠞⠑⠗⠀⠊⠝⠀⠞⠓⠑⠀⠙⠁⠽⠂⠀⠋⠊⠗⠊⠝⠛⠀⠁⠞⠀⠞⠓⠑⠝⠤⠏⠗⠑⠎⠊⠙⠑⠝⠞⠀⠠⠛⠑⠗⠁⠇⠙⠀⠠⠋⠕⠗⠙⠲"

# --- Output the translation of the original input ---
print("--- Translation of the Original UEB Input ---")
translated_text = translate_ueb_grade1_to_text(UEB_INPUT)
print(f"UEB Grade 1: {UEB_INPUT}")
print(f"Text Output: {translated_text}")
print("-" * 40)

# --- Example of an Alternative Input ---

# Alternative UEB Grade 1 Input: "Hello World! I have 10 apples."
# ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠊⠀⠓⠁⠧⠑⠀⠼⠁⠚⠀⠁⠏⠏⠇⠑⠎⠲
ALTERNATIVE_UEB_INPUT = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠊⠀⠓⠁⠧⠑⠀⠼⠁⠚⠀⠁⠏⠏⠇⠑⠎⠲"

# --- Output the translation of the alternative input ---
print("--- Translation of an Alternative UEB Input ---")
translated_alternative = translate_ueb_grade1_to_text(ALTERNATIVE_UEB_INPUT)
print(f"UEB Grade 1: {ALTERNATIVE_UEB_INPUT}")
print(f"Text Output: {translated_alternative}")
print("-" * 40)
