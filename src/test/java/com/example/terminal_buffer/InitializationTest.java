package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InitializationTest {

    // --- valid construction ---

    @Test
    void validConstructionSetsCorrectDimensions() {
        ITerminalBuffer tb = new TerminalBuffer(10, 5, 100);

        assertEquals(10, tb.getScreenWidth());
        assertEquals(5, tb.getScreenHeight());
        assertEquals(100, tb.getScrollBackMaxSize());
    }

    @Test
    void minimumValidDimensions() {
        // width=1, height=1, scrollback=0 is the smallest valid terminal
        ITerminalBuffer tb = new TerminalBuffer(1, 1, 0);

        assertEquals(1, tb.getScreenWidth());
        assertEquals(1, tb.getScreenHeight());
        assertEquals(0, tb.getScrollBackMaxSize());
    }

    @Test
    void cursorStartsAtOrigin() {
        ITerminalBuffer tb = new TerminalBuffer(5, 3, 10);

        assertEquals(0, tb.getCursorRow());
        assertEquals(0, tb.getCursorColumn());
    }

    @Test
    void screenStartsEmpty() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        assertEquals("", tb.getScreenContent().trim());
    }

    @Test
    void scrollbackStartsEmpty() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        assertEquals("", tb.getScrollbackContent().trim());
    }

    @Test
    void zeroScrollbackSizeIsValid() {
        // scrollback=0 means no history is kept at all
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 0);

        assertEquals(0, tb.getScrollBackMaxSize());
    }

    // --- invalid width ---

    @Test
    void zeroWidthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(0, 5, 10));
    }

    @Test
    void negativeWidthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(-1, 5, 10));
    }

    @Test
    void largeNegativeWidthThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(-100, 5, 10));
    }

    // --- invalid height ---

    @Test
    void zeroHeightThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(5, 0, 10));
    }

    @Test
    void negativeHeightThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(5, -1, 10));
    }

    @Test
    void largeNegativeHeightThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(5, -100, 10));
    }

    // --- invalid scrollback ---

    @Test
    void negativeScrollbackThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(5, 3, -1));
    }

    @Test
    void largeNegativeScrollbackThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(5, 3, -100));
    }

    // --- multiple invalid parameters ---

    @Test
    void allParametersInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(-1, -1, -1));
    }

    @Test
    void widthAndHeightBothZeroThrows() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalBuffer(0, 0, 10));
    }
}
