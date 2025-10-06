# didactic-chainsaw

This repository contains two Python scripts, two Java classes, two C# classes and two Javascript files for translation between standard English text and **Unified English Braille (UEB) Grade 1 (uncontracted)**. 

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

### 2. `text-braille-ueb-grade-1.py`

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

-----

## ☕ Java Classes (Needs further testing. Vibe coded with Gemini.)
These two Java classes replicate the functionality of the Python scripts, translating between standard English text and UEB Grade 1 braille using static methods.

### 3. `BrailleToTextTranslator.java`
This class provides the static method translateUebGrade1ToText(String uebInput) for translating UEB Grade 1 braille back into standard English text.

#### Example Usage:
To run the translation, compile and execute the class, which contains a main method with example usage:

```Java
public static void main(String[] args) {
    String uebInput = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
    String translatedText = BrailleToTextTranslator.translateUebGrade1ToText(uebInput);
    System.out.println("Text Output: " + translatedText);
    // Expected Output: On Sept. 22, 1975.
}
```

### 4. `UebGrade1Translator.java`
This class provides the static method translateToUebGrade1(String text) for translating standard English text into its UEB Grade 1 braille representation.

#### Example Usage:
To run the translation, compile and execute the class, which contains a main method with example usage:

```Java
public static void main(String[] args) {
    String textToTranslate = "Hello World! This is a test with 123.";
    String brailleResult = UebGrade1Translator.translateToUebGrade1(textToTranslate);
    System.out.println("Braille Output: " + brailleResult);
    // Expected Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
}
```
## 🖥️ C# Classes (Needs further testing. Vibe coded with Gemini.)
These two C# classes replicate the Python and Java functionality, providing static methods for UEB Grade 1 translation within the .NET environment.

### 5. `BrailleToTextTranslator.cs`
This static class provides the method TranslateUebGrade1ToText(string uebInput) for translating UEB Grade 1 braille back into standard English text.

#### Example Usage:
To run the translation, you can use the built-in Main method after compiling the class:

```C#
public static void Main()
{
    // Example from the original Python script.
    string uebInput = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
    string translatedText = BrailleToTextTranslator.TranslateUebGrade1ToText(uebInput);
    Console.WriteLine($"Text Output: {translatedText}");
    // Expected Output: On Sept. 22, 1975.
}
```

### 6. `UebGrade1Translator.cs`
This static class provides the method TranslateToUebGrade1(string text) for translating standard English text into its UEB Grade 1 braille representation.

#### Example Usage:
To run the translation, you can use the built-in Main method after compiling the class:

```C#
public static void Main()
{
    string textToTranslate = "Hello World! This is a test with 123.";
    string brailleResult = UebGrade1Translator.TranslateToUebGrade1(textToTranslate);
    Console.WriteLine($"Braille Output: {brailleResult}");
    // Expected Output: ⠠⠓⠑⠇⠇⠕ w⠠⠺⠕⠗⠇⠙⠖ ⠠⠞⠓⠊⠎ ⠊⠎ ⠁ ⠞⠑⠎⠞ ⠺⠊⠞⠓ ⠼⠁⠃⠉⠲
}
```

## 🌐 JavaScript Files (Needs further testing. Vibe coded with Gemini)
These two JavaScript files provide client-side translation functions, replicating the core UEB Grade 1 translation logic of the other implementations.

### 7. `BrailleToTextTranslator.js`
This script provides the function translateUebGrade1ToText(uebInput) which translates UEB Grade 1 braille (Unicode patterns) back into standard English text, handling capitalization and the numeric indicator.

#### Example Usage (from script):
```JavaScript
const UEB_INPUT = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
let translatedText = translateUebGrade1ToText(UEB_INPUT);
console.log(`Text Output: ${translatedText}`);
// Expected Output: On Sept. 22, 1975.
```

### 8. `UebGrade1Translator.js`
This script provides the function translateToUebGrade1(text) which translates standard English text into its corresponding UEB Grade 1 (uncontracted) braille representation, including indicators for capitals and digits.

#### Example Usage (from script):
``` JavaScript
const textToTranslate = "Hello World! This is a test with 123.";
const brailleResult = translateToUebGrade1(textToTranslate);
console.log(`Braille Output: ${brailleResult}`);
// Expected Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
```