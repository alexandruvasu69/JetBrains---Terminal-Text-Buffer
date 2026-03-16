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
    void fillLineDoesNotMoveCursor() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        tb.setCursorPosition(1, 2);

        tb.fillLine('Q');

        assertEquals(1, tb.getCursorRow());
        assertEquals(2, tb.getCursorColumn());
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

    @Test
    void fillLineDoesNotAffectOtherRows() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        tb.setCursorPosition(1, 0);
        tb.fillLine('X');

        assertEquals("", tb.getLineFromScreen(0).trim());
        assertEquals("XXXX", tb.getLineFromScreen(1));
        assertEquals("", tb.getLineFromScreen(2).trim());
    }

    @Test
    void fillLineOverwritesExistingContentOnCurrentRow() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("ABCD");
        tb.setCursorPosition(0, 0);

        tb.fillLine('Z');

        assertEquals("ZZZZ", tb.getLineFromScreen(0));
    }

    @Test
    void fillLineDoesNotPushAnythingToScrollback() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.fillLine('X');

        assertEquals("", tb.getScrollbackContent().trim());
    }

    @Test
    void fillLineWithSpaceFillsRowWithSpaces() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.fillLine(' ');

        assertEquals("    ", tb.getLineFromScreen(0));
    }

    @Test
    void fillLineWithSpecialCharacter() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.fillLine('#');

        assertEquals("####", tb.getLineFromScreen(0));
    }

    @Test
    void fillLineWorksAfterScreenWasPreviouslyUsed() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);
        tb.writeText("12345678");
        tb.setCursorPosition(0, 0);

        tb.fillLine('A');

        assertEquals("AAAA", tb.getLineFromScreen(0));
    }

}
