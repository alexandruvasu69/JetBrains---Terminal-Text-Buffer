package com.example.terminal_buffer;

import com.example.cell.ICellAttributes;
import com.example.style.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StyleTest {

    @Test
    void defaultAttributesAreWhiteOnBlackNoStyle() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("A");
        ICellAttributes attrs = tb.getAttributesFromScreenAt(0, 0);
        assertEquals(Color.WHITE, attrs.getForegroundColor());
        assertEquals(Color.BLACK, attrs.getBackgroundColor());
        assertFalse(attrs.isBold());
        assertFalse(attrs.isItalic());
        assertFalse(attrs.isUnderline());
    }

    @Test
    void setForegroundColorAppliesToWrittenCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("AB");
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
    }

    @Test
    void setBackgroundColorAppliesToWrittenCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setBackgroundColor(Color.BLUE);
        tb.writeText("A");
        assertEquals(Color.BLUE, tb.getAttributesFromScreenAt(0, 0).getBackgroundColor());
    }

    @Test
    void setBoldAppliesToWrittenCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setBold(true);
        tb.writeText("A");
        assertTrue(tb.getAttributesFromScreenAt(0, 0).isBold());
    }

    @Test
    void setItalicAppliesToWrittenCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setItalic(true);
        tb.writeText("A");
        assertTrue(tb.getAttributesFromScreenAt(0, 0).isItalic());
    }

    @Test
    void setUnderlineAppliesToWrittenCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setUnderline(true);
        tb.writeText("A");
        assertTrue(tb.getAttributesFromScreenAt(0, 0).isUnderline());
    }

    @Test
    void styleChangeDoesNotAffectPreviousCells() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("A");
        tb.setForegroundColor(Color.GREEN);
        tb.writeText("B");
        assertEquals(Color.WHITE, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.GREEN, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
    }

    @Test
    void multipleStylesCombine() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.CYAN);
        tb.setBackgroundColor(Color.MAGENTA);
        tb.setBold(true);
        tb.setItalic(true);
        tb.setUnderline(true);
        tb.writeText("X");

        ICellAttributes attrs = tb.getAttributesFromScreenAt(0, 0);
        assertEquals(Color.CYAN, attrs.getForegroundColor());
        assertEquals(Color.MAGENTA, attrs.getBackgroundColor());
        assertTrue(attrs.isBold());
        assertTrue(attrs.isItalic());
        assertTrue(attrs.isUnderline());
    }

    @Test
    void styleAppliesToFillLine() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setForegroundColor(Color.YELLOW);
        tb.setBold(true);
        tb.fillLine('Z');

        for (int col = 0; col < 4; col++) {
            ICellAttributes attrs = tb.getAttributesFromScreenAt(0, col);
            assertEquals(Color.YELLOW, attrs.getForegroundColor());
            assertTrue(attrs.isBold());
        }
    }

    @Test
    void styleAppliesToInsertText() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABC");
        tb.setCursorPosition(0, 1);
        tb.setForegroundColor(Color.RED);
        tb.insertText("x");

        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
        assertEquals(Color.WHITE, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.WHITE, tb.getAttributesFromScreenAt(0, 2).getForegroundColor());
    }

    @Test
    void disablingStyleAfterEnabling() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setBold(true);
        tb.writeText("A");
        tb.setBold(false);
        tb.writeText("B");

        assertTrue(tb.getAttributesFromScreenAt(0, 0).isBold());
        assertFalse(tb.getAttributesFromScreenAt(0, 1).isBold());
    }

    @Test
    void stylePreservedAfterScrollToScrollback() {
        ITerminalBuffer tb = new TerminalBuffer(3, 1, 10);
        tb.setForegroundColor(Color.RED);
        tb.setBold(true);
        tb.writeText("ABC");

        ICellAttributes attrs = tb.getAttributesFromScrollbackAt(0, 0);
        assertEquals(Color.RED, attrs.getForegroundColor());
        assertTrue(attrs.isBold());
    }

    @Test
    void overwriteCellReplacesStyleCompletely() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.setBold(true);
        tb.writeText("A");

        tb.setCursorPosition(0, 0);
        tb.setForegroundColor(Color.GREEN);
        tb.setBold(false);
        tb.setItalic(true);
        tb.writeText("B");

        ICellAttributes attrs = tb.getAttributesFromScreenAt(0, 0);
        assertEquals('B', tb.getCharacterFromScreenAt(0, 0));
        assertEquals(Color.GREEN, attrs.getForegroundColor());
        assertFalse(attrs.isBold());
        assertTrue(attrs.isItalic());
    }

    @Test
    void writeTextWithMixedStylesPerCharacter() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("A");
        tb.setForegroundColor(Color.GREEN);
        tb.writeText("B");
        tb.setForegroundColor(Color.BLUE);
        tb.writeText("C");

        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.GREEN, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
        assertEquals(Color.BLUE, tb.getAttributesFromScreenAt(0, 2).getForegroundColor());
    }

    @Test
    void stylePreservedAcrossLineWrap() {
        ITerminalBuffer tb = new TerminalBuffer(3, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.setBold(true);
        tb.writeText("ABCD");

        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 2).getForegroundColor());
        assertTrue(tb.getAttributesFromScreenAt(0, 2).isBold());
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(1, 0).getForegroundColor());
        assertTrue(tb.getAttributesFromScreenAt(1, 0).isBold());
    }

    @Test
    void styleIsolatedBetweenCellsAfterClone() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("AB");
        tb.setForegroundColor(Color.BLUE);

        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
    }

    @Test
    void fillLineOverwritesExistingStyles() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("ABCD");

        tb.setCursorPosition(0, 0);
        tb.setForegroundColor(Color.BLUE);
        tb.setBold(true);
        tb.fillLine('X');

        for (int col = 0; col < 4; col++) {
            ICellAttributes attrs = tb.getAttributesFromScreenAt(0, col);
            assertEquals(Color.BLUE, attrs.getForegroundColor());
            assertTrue(attrs.isBold());
        }
    }

    @Test
    void clearScreenResetsStylesToDefault() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.setBold(true);
        tb.writeText("ABCD");
        tb.clearScreen();

        ICellAttributes attrs = tb.getAttributesFromScreenAt(0, 0);
        assertFalse(attrs.isBold());
    }

    @Test
    void insertTextDisplacedCellsKeepOriginalStyle() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("ABC");

        tb.setCursorPosition(0, 1);
        tb.setForegroundColor(Color.GREEN);
        tb.insertText("x");

        // A stays RED, x is GREEN, B shifted to col 2 stays RED, C shifted to col 3 stays RED
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.GREEN, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 2).getForegroundColor());
        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 3).getForegroundColor());
    }

    @Test
    void rapidStyleToggleBoldOnOffOn() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setBold(true);
        tb.writeText("A");
        tb.setBold(false);
        tb.writeText("B");
        tb.setBold(true);
        tb.writeText("C");

        assertTrue(tb.getAttributesFromScreenAt(0, 0).isBold());
        assertFalse(tb.getAttributesFromScreenAt(0, 1).isBold());
        assertTrue(tb.getAttributesFromScreenAt(0, 2).isBold());
    }

    @Test
    void allThreeTextStylesIndependent() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setBold(true);
        tb.writeText("A");
        tb.setBold(false);
        tb.setItalic(true);
        tb.writeText("B");
        tb.setItalic(false);
        tb.setUnderline(true);
        tb.writeText("C");

        assertTrue(tb.getAttributesFromScreenAt(0, 0).isBold());
        assertFalse(tb.getAttributesFromScreenAt(0, 0).isItalic());
        assertFalse(tb.getAttributesFromScreenAt(0, 0).isUnderline());

        assertFalse(tb.getAttributesFromScreenAt(0, 1).isBold());
        assertTrue(tb.getAttributesFromScreenAt(0, 1).isItalic());
        assertFalse(tb.getAttributesFromScreenAt(0, 1).isUnderline());

        assertFalse(tb.getAttributesFromScreenAt(0, 2).isBold());
        assertFalse(tb.getAttributesFromScreenAt(0, 2).isItalic());
        assertTrue(tb.getAttributesFromScreenAt(0, 2).isUnderline());
    }

    @Test
    void styledCellsPreservedInScrollbackAfterMultipleScrolls() {
        ITerminalBuffer tb = new TerminalBuffer(3, 1, 5);
        tb.setForegroundColor(Color.RED);
        tb.writeText("AAA");
        tb.setForegroundColor(Color.GREEN);
        tb.writeText("BBB");
        tb.setForegroundColor(Color.BLUE);
        tb.writeText("CC");

        // scrollback: "AAA" (RED), "BBB" (GREEN)
        // screen: "CC " (BLUE at col 0 and 1)
        assertEquals(Color.RED, tb.getAttributesFromScrollbackAt(0, 0).getForegroundColor());
        assertEquals(Color.GREEN, tb.getAttributesFromScrollbackAt(1, 0).getForegroundColor());
        assertEquals(Color.BLUE, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
        assertEquals(Color.BLUE, tb.getAttributesFromScreenAt(0, 1).getForegroundColor());
    }

    @Test
    void writeEmptyStringDoesNotAffectStyles() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.setForegroundColor(Color.RED);
        tb.writeText("A");
        tb.setForegroundColor(Color.BLUE);
        tb.writeText("");

        assertEquals(Color.RED, tb.getAttributesFromScreenAt(0, 0).getForegroundColor());
    }

    @Test
    void singleCellWidthScreenStylePreserved() {
        // width=1, height=1: writing "X" triggers scroll after the char,
        // so the styled cell ends up in scrollback
        ITerminalBuffer tb = new TerminalBuffer(1, 1, 5);
        tb.setForegroundColor(Color.CYAN);
        tb.setBold(true);
        tb.setUnderline(true);
        tb.writeText("X");

        ICellAttributes attrs = tb.getAttributesFromScrollbackAt(0, 0);
        assertEquals(Color.CYAN, attrs.getForegroundColor());
        assertTrue(attrs.isBold());
        assertTrue(attrs.isUnderline());
    }
}
