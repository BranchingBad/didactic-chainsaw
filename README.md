# didactic-chainsaw

This repository contains two Python scripts for translation between standard English text and **Unified English Braille (UEB) Grade 1 (uncontracted)**.

---

## 🚀 Scripts

### 1. `braille-text.py`

This script provides the function `translate_ueb_grade1_to_text(ueb_input)` which translates **UEB Grade 1 braille (Unicode patterns)** back into standard English text. It handles capitalization indicators, the numeric indicator, and basic punctuation.

#### Example Usage (from script):

The script currently processes the following UEB input, which is a news-style passage:

```python
UEB_INPUT = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠂⠀⠼⠙⠑⠤⠽⠑⠁⠗⠤⠕⠇⠙⠀⠠⠎⠁⠗⠁⠀⠠⠚⠁⠝⠑⠀⠠⠍⠕⠕⠗⠑⠀⠙⠗⠕⠏⠏⠑⠙⠀⠓⠑⠗⠀⠎⠕⠝⠀⠕⠋⠋⠀⠁⠞⠀⠓⠊⠎⠀⠠⠎⠁⠝⠀⠠⠋⠗⠁⠝⠉⠊⠎⠉⠕⠀⠎⠉⠓⠕⠕⠇⠂⠀⠧⠊⠎⠊⠞⠑⠙⠀⠁⠀⠏⠗⠊⠧⠁⠞⠑⠀⠛⠥⠝⠀⠙⠑⠁⠇⠑⠗⠀⠁⠝⠙⠂⠀⠊⠝⠀⠺⠓⠁⠞⠀⠎⠓⠑⠀⠇⠁⠞⠑⠗⠀⠞⠕⠇⠙⠀⠞⠓⠑⠀⠠⠇⠕⠎⠀⠠⠁⠝⠛⠑⠇⠑⠎⠀⠠⠞⠊⠍⠑⠎⠀⠺⠁⠎⠀⠁⠀⠶⠁⠀⠅⠊⠝⠙⠀⠕⠋⠀⠥⠇⠞⠊⠍⠁⠞⠑⠀⠏⠗⠕⠞⠑⠎⠞⠀⠁⠛⠁⠊⠝⠎⠞⠀⠞⠓⠑⠀⠎⠽⠎⠞⠑⠍⠂⠴⠀⠙⠗⠑⠺⠀⠁⠀⠨⠼⠉⠓⠤⠉⠁⠇⠊⠃⠗⠑⠀⠏⠊⠎⠞⠕⠇⠀⠕⠥⠞⠎⠊⠙⠑⠀⠁⠀⠓⠕⠞⠑⠇⠀⠇⠁⠞⠑⠗⠀⠊⠝⠀⠞⠓⠑⠀⠙⠁⠽⠂⠀⠋⠊⠗⠊⠝⠛⠀⠁⠞⠀⠞⠓⠑⠝⠤⠏⠗⠑⠎⠊⠙⠑⠝⠞⠀⠠⠛⠑⠗⠁⠇⠙⠀⠠⠋⠕⠗⠙⠲"
````

**Output:**

```
On Sept. 22, 1989, 45-year-old Sara Jane Moore dropped her son off at his San Francisco school, visited a private gun dealer and, in what she later told the Los Angeles Times was a "a kind of ultimate protest against the system," drew a .38-caliber pistol outside a hotel later in the day, firing at then-president Gerald Ford.
```

-----

### 2\. `text-braille-ueb-grade-1.py`

This script provides the function `translate_to_ueb_grade1(text)` which translates **standard English text** into its corresponding **UEB Grade 1 (uncontracted) braille representation**. It implements rules for:

  * Lowercase letters
  * The **Capital Letter Indicator** (`⠠`) for capitalized letters.
  * The **Numeric Indicator** (`⠼`) for digit sequences.
  * Basic punctuation (e.g., space, period, comma, question mark).

#### Example Usage (from script):

```python
text_to_translate = "Hello World! This is a test with 123."
```

**Output:**

```
⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
```

(Which translates back to "Hello World\! This is a test with 123.")

```
```
