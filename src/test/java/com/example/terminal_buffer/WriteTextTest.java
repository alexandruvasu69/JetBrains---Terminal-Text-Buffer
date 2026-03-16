package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WriteTextTest {
    @Test
    void writeTextOverwritesAtCursor() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 0);
        tb.writeText("xy");
        // "AB" is overwritten by "xy" and disappear forever
        assertEquals("xyCDE", tb.getLineFromScreen(0));
    }

    @Test
    void writeTextWrapsToNextLine() {
        // if the remaining text overflows the line,
        // the cursor moves on the next line and continues writing
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("12345");
        assertEquals("1234", tb.getLineFromScreen(0));
        assertEquals("5", tb.getLineFromScreen(1).trim());
    }

    @Test
    void writeTextWrapsToMultipleLines() {
        // the above functionality works on multiple rows (eg. if input text is bigger than 
        // 2 * rowWidth)
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        tb.writeText("123456789");
        assertEquals("1234", tb.getLineFromScreen(0));
        assertEquals("5678", tb.getLineFromScreen(1).trim());
        assertEquals("9", tb.getLineFromScreen(2).trim());
    }

    @Test
    void writeTextScrollsIfLineFull() {
        // Writing exactly one full line should move the cursor to the beginning of the next line.
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("1234");
        assertEquals(1, tb.getCursorRow());
        assertEquals(0, tb.getCursorColumn());
    }

    @Test
    void writeTextScrollsWhenScreenFull() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setCursorPosition(1, 0);
        tb.writeText("123456789");
        // after scrolling, oldest lines go to scrollback
        assertEquals("5678", tb.getLineFromScreen(0));
        assertEquals("9", tb.getLineFromScreen(1).trim());
    }

    @Test
    void writeTextMovesToScrollbackIfOnlyOneLine() {
        // Writing exactly one full line should move the cursor to the beginning of the next line.
        // If for example there is only 1 line in the terminal, then the line is moved to the scrollback and
        // a new empty line appears on the screen (this could also be considered a bug, but personally,
        // I think the terminal should scroll to the next line automatically after the input)
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 10);
        tb.writeText("1234");
        assertEquals("", tb.getLineFromScreen(0).trim());
        assertEquals("1234", tb.getLineFromScrollback(0));
    }

    @Test
    void writeTextThrowsWithEmptyString() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        assertThrows(IllegalArgumentException.class, () -> {
            tb.writeText("");
        });
    }

    @Test
    void writeTextOverwritesFromMiddleOfLine() {
        ITerminalBuffer tb = new TerminalBuffer(5, 2, 10);
        tb.writeText("ABCDE");
        tb.setCursorPosition(0, 2);
        tb.writeText("xy");

        assertEquals("ABxyE", tb.getLineFromScreen(0));
    }

    @Test
    void writeTextAtLastColumnWrapsAfterCharacter() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setCursorPosition(0, 3);
        tb.writeText("ab");

        assertEquals("   a", tb.getLineFromScreen(0));
        assertEquals("b", tb.getLineFromScreen(1).trim());
        assertEquals(1, tb.getCursorRow());
        assertEquals(1, tb.getCursorColumn());
    }

    @Test
    void writeTextWrapsToNextLineWhenStartedInMiddle() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.setCursorPosition(0, 2);
        tb.writeText("xyz");

        assertEquals("  xy", tb.getLineFromScreen(0));
        assertEquals("z", tb.getLineFromScreen(1).trim());
    }
}
