import unittest
import sys
import os

# Add src/python to path to import modules
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../../src/python')))

from text_to_braille import translate_to_ueb_grade1
from braille_to_text import translate_ueb_grade1_to_text

class TestBrailleTranslation(unittest.TestCase):

    def test_basic_lowercase(self):
        self.assertEqual(translate_to_ueb_grade1("abc"), "⠁⠃⠉")
        self.assertEqual(translate_ueb_grade1_to_text("⠁⠃⠉"), "abc")

    def test_capitalization(self):
        # Note: Code only supports single letter capitalization (Dot 6)
        self.assertEqual(translate_to_ueb_grade1("Hello"), "⠠⠓⠑⠇⠇⠕")
        self.assertEqual(translate_ueb_grade1_to_text("⠠⠓⠑⠇⠇⠕"), "Hello")

    def test_numbers_and_decimals(self):
        self.assertEqual(translate_to_ueb_grade1("123.45"), "⠼⠁⠃⠉⠲⠙⠑")
        self.assertEqual(translate_ueb_grade1_to_text("⠼⠁⠃⠉⠲⠙⠑"), "123.45")

    def test_punctuation_handling(self):
        self.assertEqual(translate_to_ueb_grade1("Go home!"), "⠠⠛⠕⠀⠓⠕⠍⠑⠖")
        self.assertEqual(translate_ueb_grade1_to_text("⠠⠛⠕⠀⠓⠕⠍⠑⠖"), "Go home!")

    def test_parentheses(self):
        self.assertEqual(translate_to_ueb_grade1("(Code)"), "⠐⠣⠠⠉⠕⠙⠑⠐⠜")
        self.assertEqual(translate_ueb_grade1_to_text("⠐⠣⠠⠉⠕⠙⠑⠐⠜"), "(Code)")

    def test_smart_quote_normalization(self):
        # Verify “ ” become "
        self.assertEqual(translate_to_ueb_grade1("“Hi”"), "⠶⠠⠓⠊⠶")

    def test_round_trip(self):
        original = "The year is 2025."
        braille = translate_to_ueb_grade1(original)
        back_to_text = translate_ueb_grade1_to_text(braille)
        self.assertEqual(original, back_to_text)

if __name__ == '__main__':
    unittest.main()