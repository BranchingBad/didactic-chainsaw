import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrailleTest {

    @Test
    void testBasicTranslation() {
        assertEquals("⠁⠃⠉", TextToBraille.translateToUebGrade1("abc"));
        assertEquals("abc", BrailleToText.translateUebGrade1ToText("⠁⠃⠉"));
    }

    @Test
    void testNumericMode() {
        assertEquals("⠼⠁⠃⠉", TextToBraille.translateToUebGrade1("123"));
        // Test interrupt by space
        assertEquals("⠼⠁⠃⠉⠀⠼⠙", TextToBraille.translateToUebGrade1("123 4"));
    }

    @Test
    void testDecimals() {
        // Decimal should maintain numeric mode
        assertEquals("⠼⠁⠲⠑", TextToBraille.translateToUebGrade1("1.5"));
        assertEquals("1.5", BrailleToText.translateUebGrade1ToText("⠼⠁⠲⠑"));
    }

    @Test
    void testSpecialSymbols() {
        // Smart quotes normalization
        assertEquals("⠶⠠⠓⠊⠶", TextToBraille.translateToUebGrade1("“Hi”"));
    }
}