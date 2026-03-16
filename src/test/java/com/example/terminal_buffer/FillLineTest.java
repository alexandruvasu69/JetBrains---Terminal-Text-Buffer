package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FillLineTest {

    @Test
    void fillLineFillsCurrentRow() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.fillLine('X');
        assertEquals("XXXX", tb.getLineFromScreen(0));
    }

    @Test
    void fillLineOnMiddleRow() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        tb.setCursorPosition(1, 0);
        tb.fillLine('Y');
        assertEquals("YYYY", tb.getLineFromScreen(1));
    }

    @Test
    void fillLineOnLastRow() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        tb.setCursorPosition(2, 0);
        tb.fillLine('Y');
        assertEquals("YYYY", tb.getLineFromScreen(2));
    }
}
