# didactic-chainsaw

A multilingual implementation of bidirectional translation between standard English text and **Unified English Braille (UEB) Grade 1 (uncontracted)**. This repository provides implementations in Python, Java, C#, and JavaScript, complete with comprehensive test suites.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Implementations](#implementations)
  - [Python](#python)
  - [Java](#java)
  - [C#](#c)
  - [JavaScript](#javascript)
- [Installation & Usage](#installation--usage)
- [Testing](#testing)
- [UEB Grade 1 Reference](#ueb-grade-1-reference)
- [Contributing](#contributing)
- [License](#license)

---

## 🔍 Overview

This project implements UEB Grade 1 braille translation in four programming languages. Each implementation provides two core functions:

1. **Text to Braille**: Converts standard English text into UEB Grade 1 braille (Unicode patterns)
2. **Braille to Text**: Converts UEB Grade 1 braille back into readable English text

All implementations handle:
- Lowercase and uppercase letters
- Capital letter indicators
- Numeric mode with proper indicators
- Decimal points in numbers
- Standard punctuation marks
- Parentheses with proper prefixes

---

## ✨ Features

- **Multi-language Support**: Identical functionality across Python, Java, C#, and JavaScript
- **Full Unicode Support**: Uses standard Unicode braille patterns (U+2800 to U+28FF)
- **Comprehensive Testing**: Unit tests for all implementations
- **CI/CD Ready**: Includes Drone CI configuration
- **Round-trip Translation**: Ensures accuracy with bidirectional conversion tests
- **Smart Quote Handling**: Automatically normalizes smart quotes
- **Numeric Mode Logic**: Proper handling of decimals and number sequences

---

## 📁 Project Structure

```
didactic-chainsaw/
├── .drone.yml                      # CI/CD pipeline configuration
├── .gitignore                      # Git ignore rules
├── LICENSE                         # Apache 2.0 license
├── README.md                       # This file
├── data/
│   ├── dictionaries/               # UEB Grade 2 reference dictionaries
│   │   ├── ueb-grade-2-ascii.txt
│   │   ├── ueb-grade-2-perky.txt
│   │   └── ueb-grade-2-unicode.txt
│   └── samples/                    # Sample text and braille files
│       ├── ontario-human-rights.txt
│       └── sample-perky.txt
├── src/
│   ├── python/                     # Python implementation
│   │   ├── text_to_braille.py
│   │   └── braille_to_text.py
│   ├── java/                       # Java implementation
│   │   ├── TextToBraille.java
│   │   └── BrailleToText.java
│   ├── csharp/                     # C# implementation
│   │   ├── TextToBraille.cs
│   │   └── BrailleToText.cs
│   └── javascript/                 # JavaScript implementation
│       ├── text-to-braille.js
│       └── braille-to-text.js
└── tests/
    ├── python/
    │   └── test_braille.py
    ├── java/
    │   └── BrailleTest.java
    ├── csharp/
    │   └── BrailleTests.cs
    └── javascript/
        └── braille.test.js
```

---

## 🚀 Implementations

### Python

#### **text_to_braille.py**
Translates standard English text into UEB Grade 1 braille.

**Usage:**
```python
from text_to_braille import translate_to_ueb_grade1

text = "Hello World! This is a test with 123.45 and \"quotes\"."
braille = translate_to_ueb_grade1(text)
print(braille)
# Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲⠙⠑⠀⠁⠝⠙⠀⠶⠟⠥⠕⠞⠑⠎⠶⠲
```

#### **braille_to_text.py**
Translates UEB Grade 1 braille back into standard English text.

**Usage:**
```python
from braille_to_text import translate_ueb_grade1_to_text

braille = "⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠼⠁⠃⠉⠲⠙⠑"
text = translate_ueb_grade1_to_text(braille)
print(text)
# Output: Hello World! 123.45
```

---

### Java

#### **TextToBraille.java**
Provides static method `translateToUebGrade1(String text)` for text-to-braille translation.

**Usage:**
```java
public static void main(String[] args) {
    String text = "Hello World! This is a test with 123.";
    String braille = TextToBraille.translateToUebGrade1(text);
    System.out.println("Braille: " + braille);
    // Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
}
```

#### **BrailleToText.java**
Provides static method `translateUebGrade1ToText(String uebInput)` for braille-to-text translation.

**Usage:**
```java
public static void main(String[] args) {
    String braille = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
    String text = BrailleToText.translateUebGrade1ToText(braille);
    System.out.println("Text: " + text);
    // Output: On Sept. 22, 1975.
}
```

---

### C#

#### **TextToBraille.cs**
Static class with method `TranslateToUebGrade1(string text)` for text-to-braille translation.

**Usage:**
```csharp
public static void Main()
{
    string text = "Hello World! This is a test with 123.";
    string braille = TextToBraille.TranslateToUebGrade1(text);
    Console.WriteLine($"Braille: {braille}");
    // Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
}
```

#### **BrailleToText.cs**
Static class with method `TranslateUebGrade1ToText(string uebInput)` for braille-to-text translation.

**Usage:**
```csharp
public static void Main()
{
    string braille = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
    string text = BrailleToText.TranslateUebGrade1ToText(braille);
    Console.WriteLine($"Text: {text}");
    // Output: On Sept. 22, 1975.
}
```

---

### JavaScript

#### **text-to-braille.js**
Function `translateToUebGrade1(text)` for text-to-braille translation.

**Usage:**
```javascript
const textToTranslate = "Hello World! This is a test with 123.";
const braille = translateToUebGrade1(textToTranslate);
console.log(`Braille: ${braille}`);
// Output: ⠠⠓⠑⠇⠇⠕⠀⠠⠺⠕⠗⠇⠙⠖⠀⠠⠞⠓⠊⠎⠀⠊⠎⠀⠁⠀⠞⠑⠎⠞⠀⠺⠊⠞⠓⠀⠼⠁⠃⠉⠲
```

#### **braille-to-text.js**
Function `translateUebGrade1ToText(uebInput)` for braille-to-text translation.

**Usage:**
```javascript
const braille = "⠠⠕⠝⠀⠠⠎⠑⠏⠞⠲⠀⠼⠃⠃⠂⠀⠼⠁⠊⠛⠑⠲";
const text = translateUebGrade1ToText(braille);
console.log(`Text: ${text}`);
// Output: On Sept. 22, 1975.
```

---

## 🔧 Installation & Usage

### Python
```bash
# No installation required - pure Python 3
python src/python/text_to_braille.py
python src/python/braille_to_text.py
```

### Java
```bash
# Compile
javac src/java/TextToBraille.java src/java/BrailleToText.java

# Run
java -cp src/java TextToBraille
java -cp src/java BrailleToText
```

### C#
```bash
# Compile
csc src/csharp/TextToBraille.cs
csc src/csharp/BrailleToText.cs

# Run
./TextToBraille.exe
./BrailleToText.exe
```

### JavaScript
```bash
# Node.js
node src/javascript/text-to-braille.js
node src/javascript/braille-to-text.js
```

---

## 🧪 Testing

All implementations include comprehensive unit tests that verify:
- Basic alphabet translation
- Capitalization handling
- Numeric mode and decimal points
- Punctuation marks
- Parentheses with proper prefixes
- Round-trip translation integrity
- Smart quote normalization

### Running Tests

#### Python
```bash
python -m unittest discover -s tests/python -p "test_*.py"
```

#### Java
```bash
# Requires JUnit 5
javac -cp junit-platform-console-standalone.jar src/java/*.java tests/java/*.java
java -jar junit-platform-console-standalone.jar -cp src/java:tests/java --scan-classpath
```

#### C#
```bash
# Requires NUnit
nunit-console tests/csharp/BrailleTests.cs
```

#### JavaScript
```bash
# Requires Jest
npm install jest
npx jest tests/javascript/braille.test.js
```

---

## 📖 UEB Grade 1 Reference

### Key Indicators

| Indicator | Braille | Unicode | Description |
|-----------|---------|---------|-------------|
| Capital | ⠠ | U+2820 | Dot 6 - Precedes capital letter |
| Numeric | ⠼ | U+283C | Dots 3-4-5-6 - Starts numeric mode |
| Grade 1 Symbol | ⠰ | U+2830 | Dot 5-6 - Used for parentheses prefix |

### Number Mapping

Numbers use the first ten letters (a-j) preceded by the numeric indicator:

| Digit | Letter | Braille | Unicode |
|-------|--------|---------|---------|
| 1 | a | ⠁ | U+2801 |
| 2 | b | ⠃ | U+2803 |
| 3 | c | ⠉ | U+2809 |
| 4 | d | ⠙ | U+2819 |
| 5 | e | ⠑ | U+2811 |
| 6 | f | ⠋ | U+280B |
| 7 | g | ⠛ | U+281B |
| 8 | h | ⠓ | U+2813 |
| 9 | i | ⠊ | U+280A |
| 0 | j | ⠚ | U+281A |

### Numeric Mode Rules

1. Numeric indicator (⠼) starts numeric mode
2. Numeric mode continues through digits, commas, and decimal points
3. Decimal points only maintain numeric mode if followed by a digit
4. Hyphens, spaces, or letters terminate numeric mode
5. Periods not followed by digits terminate numeric mode

### Common Punctuation

| Character | Braille | Unicode | Dots |
|-----------|---------|---------|------|
| Space | (space) | U+2800 | None |
| Period | ⠲ | U+2832 | 2-5-6 |
| Comma | ⠂ | U+2802 | 2 |
| Exclamation | ⠖ | U+2816 | 2-3-5 |
| Question | ⠦ | U+2826 | 2-3-6 |
| Hyphen | ⠤ | U+2824 | 3-6 |
| Apostrophe | ⠄ | U+2804 | 3 |
| Opening Paren | ⠐⠣ | U+2810+2823 | 5, 1-2-6 |
| Closing Paren | ⠐⠜ | U+2810+281C | 5, 3-4-5 |

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for:

- Bug fixes
- Additional language implementations
- Enhanced test coverage
- Documentation improvements
- UEB Grade 2 (contracted) support

---

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

---

## 🔗 Resources

- [Unified English Braille (UEB) Guidelines](http://www.iceb.org/ueb.html)
- [Unicode Braille Patterns](https://en.wikipedia.org/wiki/Braille_Patterns)
- [Perkins School for the Blind - UEB Resources](https://www.perkins.org/resource/unified-english-braille-ueb/)

---

## ⚠️ Important Notes

- **Java and C# implementations**: While functional, these have been generated with AI assistance and require further testing in production environments
- **Grade 2 Support**: This repository includes Grade 2 dictionary references in the `data/dictionaries/` folder for future implementation
- **Limited Punctuation**: Current implementations support basic punctuation; additional symbols may require updates to the mapping dictionaries

---

**Last Updated**: December 2024