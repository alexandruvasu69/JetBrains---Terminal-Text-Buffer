package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;

import com.example.exceptions.OutOfBoundsException;

import static org.junit.jupiter.api.Assertions.*;

class CursorTest {

    @Test
    void correctInitialPosition() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        assertEquals(0, tb.getCursorRow());
        assertEquals(0, tb.getCursorColumn());
        tb.writeText("X");
        assertEquals("X", tb.getLineFromScreen(0).trim());
        assertEquals('X', tb.getCharacterFromScreenAt(0, 0));
    }

    @Test
    void setCursorPosition() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        tb.setCursorPosition(2, 3);
        assertEquals(2, tb.getCursorRow());
        assertEquals(3, tb.getCursorColumn());
        tb.writeText("X");
        assertEquals("X", tb.getLineFromScreen(2).trim());
        assertEquals('X', tb.getCharacterFromScreenAt(2, 3));
    }

    @Test
    void setCursorPositionDoesNotChangeScreenContent() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        tb.writeText("ABC");
        tb.setCursorPosition(2, 4);

        assertEquals("ABC", tb.getLineFromScreen(0).trim());
        assertEquals("", tb.getLineFromScreen(1).trim());
        assertEquals("", tb.getLineFromScreen(2).trim());
    }

    @Test
    void setCursorLastPosition() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        tb.setCursorPosition(2, 4);
        assertEquals(2, tb.getCursorRow());
        assertEquals(4, tb.getCursorColumn());
    }

    @Test
    void cursorOutOfBoundsThrows() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        assertThrows(OutOfBoundsException.class, () -> tb.setCursorPosition(3, 0));
        assertThrows(OutOfBoundsException.class, () -> tb.setCursorPosition(0, 5));
    }

    @Test
    void negativeCursorPositionThrows() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        assertThrows(OutOfBoundsException.class, () -> tb.setCursorPosition(-1, 0));
        assertThrows(OutOfBoundsException.class, () -> tb.setCursorPosition(0, -1));
    }

    @Test
    void bothCursorCoordinatesOutOfBoundsThrow() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        assertThrows(OutOfBoundsException.class, () -> tb.setCursorPosition(10, 10));
    }

    @Test
    void setCursorPositionToCurrentPositionWorks() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);
        tb.setCursorPosition(0, 0);

        assertEquals(0, tb.getCursorRow());
        assertEquals(0, tb.getCursorColumn());
    }
}
