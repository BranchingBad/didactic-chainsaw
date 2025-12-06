using NUnit.Framework;

[TestFixture]
public class BrailleTests
{
    [Test]
    public void TestBasicAlphabet()
    {
        Assert.AreEqual("⠁⠃⠉", TextToBraille.TranslateToUebGrade1("abc"));
        Assert.AreEqual("abc", BrailleToText.TranslateUebGrade1ToText("⠁⠃⠉"));
    }

    [Test]
    public void TestMixedContent()
    {
        string input = "Room 101";
        string expected = "⠠⠗⠕⠕⠍⠀⠼⠁⠚⠁";
        
        Assert.AreEqual(expected, TextToBraille.TranslateToUebGrade1(input));
        Assert.AreEqual(input, BrailleToText.TranslateUebGrade1ToText(expected));
    }

    [Test]
    public void TestPunctuationLookahead()
    {
        // 123. (End of sentence, not decimal)
        // Note: Your logic might interpret 123. as numeric mode depending on implementation details.
        // The spec implies . is decimal only if followed by digit.
        Assert.AreEqual("⠼⠁⠃⠉⠲", TextToBraille.TranslateToUebGrade1("123."));
    }
}