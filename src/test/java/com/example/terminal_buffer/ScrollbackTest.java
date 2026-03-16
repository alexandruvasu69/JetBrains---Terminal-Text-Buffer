package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScrollbackTest {

    @Test
    void scrollPushesTopLineToScrollback() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.fillLine('3');
        tb.setCursorPosition(1, 0);
        tb.writeText("123456789");

        assertEquals("3333", tb.getLineFromScrollback(0));
        assertEquals("1234", tb.getLineFromScrollback(1));
        assertEquals("5678", tb.getLineFromScreen(0));
        assertEquals("9", tb.getLineFromScreen(1).trim());
    }

    @Test
    void clearScreenAndScrollback() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 10);
        tb.writeText("test");
        tb.clearScreenAndScrollback();
        assertEquals("", tb.getScreenContent().trim());
        assertEquals("", tb.getScrollbackContent().trim());
    }

    @Test
    void scrollbackDeletesOldestLine() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 1);
        tb.writeText("test");
        assertEquals("test", tb.getScrollbackContent());
        tb.writeText("1234");
        assertEquals("1234", tb.getScrollbackContent());
    }
}
