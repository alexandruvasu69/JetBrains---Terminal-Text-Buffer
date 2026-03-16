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
        assertEquals("xyCDE", tb.getLineFromScreen(0));
    }

    @Test
    void writeTextWrapsToNextLine() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("12345");
        assertEquals("1234", tb.getLineFromScreen(0));
        assertEquals("5", tb.getLineFromScreen(1).trim());
    }

    @Test
    void writeTextScrollsIfLineFull() {
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
}
