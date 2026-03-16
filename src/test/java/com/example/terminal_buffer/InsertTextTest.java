package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsertTextTest {

    @Test
    void insertTextOnEmptyLine() {
        ITerminalBuffer tb = new TerminalBuffer(5, 1, 10);
        tb.insertText("text");
        assertEquals("text", tb.getLineFromScreen(0).trim());
    }

    @Test
    void insertTextDisplacesExistingContent() {
        // screen="33333", insert "text" at col 0 -> "text3" overflows to scrollback,
        // the remaining "3333" stays on screen (one '3' was pushed off with the overflow)
        ITerminalBuffer tb = new TerminalBuffer(5, 1, 10);
        tb.fillLine('3');
        tb.setCursorPosition(0, 0);
        tb.insertText("text");
        assertEquals("3333", tb.getLineFromScreen(0).trim());
        assertEquals("text3", tb.getLineFromScrollback(0));
    }

    @Test
    void insertTextDisplacesMultipleLines() {
        // both rows filled with '3', insert "text" at (0,0)
        // overflow cascades through both rows and triggers a scroll
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.fillLine('3');
        tb.setCursorPosition(1, 0);
        tb.fillLine('3');
        tb.setCursorPosition(0, 0);
        tb.insertText("text");

        assertEquals("text3", tb.getLineFromScrollback(0));
        assertEquals("33333", tb.getLineFromScreen(0));
        assertEquals("3333", tb.getLineFromScreen(1).trim());
    }

    @Test
    void insertTextInMiddleOfLine() {
        // "ABCD_" with "xy" inserted at col 2 -> "ABxyC" fills the row,
        // "D" overflows to the next row (which triggers a scroll on height=1)
        ITerminalBuffer tb = new TerminalBuffer(5, 1, 10);
        tb.writeText("ABCD");
        tb.setCursorPosition(0, 2);
        tb.insertText("xy");

        assertEquals("ABxyC", tb.getLineFromScrollback(0).trim());
        assertEquals("D", tb.getLineFromScreen(0).trim());
    }

    @Test
    void insertTextWithDifferentCharsPreservesOrder() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 0);
        tb.insertText("12");

        assertEquals("12ABC", tb.getLineFromScreen(0));
        assertEquals("DE", tb.getLineFromScreen(1).trim());
    }

    @Test
    void insertTextThrowsWithEmptyString() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            tb.insertText("");
        });
    }

    @Test
    void insertTextAtEndOfLinePushesOverflowForward() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 4);

        tb.insertText("X");

        assertEquals("ABCDX", tb.getLineFromScreen(0));
        assertEquals("E", tb.getLineFromScreen(1).trim());
    }
    @Test
    void insertTextInMiddleShiftsMultipleCharactersForward() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 2);

        tb.insertText("xyz");

        assertEquals("ABxyz", tb.getLineFromScreen(0));
        assertEquals("CDE", tb.getLineFromScreen(1).trim());
    }

    @Test
    void insertTextWrapsAcrossMultipleLinesOnEmptyScreen() {
        // on an empty screen, insertText behaves like writeText since
        // there is no existing content to shift
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);

        tb.insertText("123456789");

        assertEquals("1234", tb.getLineFromScreen(0));
        assertEquals("5678", tb.getLineFromScreen(1));
        assertEquals("9", tb.getLineFromScreen(2).trim());
    }

    @Test
    void insertTextScrollsWhenScreenIsFull() {
        // row 0="ABCD", row 1="EEEE" (cursor on row 1 after writeText, then fillLine)
        // insert "X" at (0,0) -> row 0="XABC", overflow "D" cascades into row 1,
        // which is full, causing a scroll
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("ABCD");
        tb.fillLine('E');
        tb.setCursorPosition(0, 0);

        tb.insertText("X");

        assertEquals("DEEE", tb.getLineFromScreen(0));
        assertEquals("E", tb.getLineFromScreen(1).trim());
        assertEquals("XABC", tb.getLineFromScrollback(0));
    }

    @Test
    void insertTextOnLowerLineDoesNotChangePreviousLineUnlessNeeded() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.writeText("FG");
        tb.setCursorPosition(1, 0);

        tb.insertText("12");

        assertEquals("ABCDE", tb.getLineFromScreen(0));
        assertEquals("12FG", tb.getLineFromScreen(1).trim());
    }

    @Test
    void insertTextWithSpacesInsertsSpacesToo() {
        // spaces are treated as regular characters, not as empty cells
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 2);

        tb.insertText("  ");

        assertEquals("AB  C", tb.getLineFromScreen(0));
        assertEquals("DE", tb.getLineFromScreen(1).trim());
    }

    @Test
    void insertSingleCharacterIntoFullLinePushesLastCharacterOut() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 0);

        tb.insertText("X");

        assertEquals("XABCD", tb.getLineFromScreen(0));
        assertEquals("E", tb.getLineFromScreen(1).trim());
    }
}
