package com.example.terminal_buffer;

import org.junit.jupiter.api.Test;

import com.example.exceptions.OutOfBoundsException;

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

    @Test
    void multipleScrollsPreserveLineOrder() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        assertEquals("1111", tb.getLineFromScrollback(0));
        assertEquals("2222", tb.getLineFromScrollback(1));
        assertEquals("3333", tb.getLineFromScrollback(2));
    }

    @Test
    void scrollbackKeepsOnlyLastMaxLines() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 2);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        assertEquals("2222", tb.getLineFromScrollback(0));
        assertEquals("3333", tb.getLineFromScrollback(1));
        assertEquals("", tb.getScreenContent().trim());
    }

    @Test
    void scrollbackContentContainsLinesInLogicalOrder() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        assertEquals("1111\n2222", tb.getScrollbackContent());
    }

    @Test
    void clearScreenAndScrollbackRemovesAllStoredLines() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        tb.clearScreenAndScrollback();

        assertEquals("", tb.getScreenContent().trim());
        assertEquals("", tb.getScrollbackContent().trim());
    }

    @Test
    void clearScreenAndScrollbackAllowsReuseAfterwards() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.clearScreenAndScrollback();

        tb.writeText("abcd");

        assertEquals("", tb.getScrollbackContent().trim());
        assertEquals("abcd", tb.getScreenContent().trim());
    }

    @Test
    void scrollbackWorksWithThreeVisibleRows() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        assertEquals("1111", tb.getLineFromScrollback(0));
        assertEquals("2222", tb.getLineFromScreen(0));
        assertEquals("3333", tb.getLineFromScreen(1));
    }

    @Test
    void scrollbackStoresSeveralLinesAfterLargeWrite() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);

        tb.writeText("1111222233334444");

        assertEquals("1111", tb.getLineFromScrollback(0));
        assertEquals("2222", tb.getLineFromScrollback(1));
        assertEquals("3333", tb.getLineFromScreen(0));
        assertEquals("4444", tb.getLineFromScreen(1));
    }

    @Test
    void scrollbackOverwritesOldestLineWhenFull() {
        ITerminalBuffer tb = new TerminalBuffer(4, 1, 2);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");
        tb.writeText("4444");

        assertEquals("3333", tb.getLineFromScrollback(0));
        assertEquals("4444", tb.getLineFromScrollback(1));
    }

    @Test
    void clearOnEmptyBufferDoesNothing() {
        ITerminalBuffer tb = new TerminalBuffer(4, 2, 10);

        tb.clearScreenAndScrollback();

        assertEquals("", tb.getScreenContent().trim());
        assertEquals("", tb.getScrollbackContent().trim());
    }

    @Test
    void getLineFromScrollbackThrowsWhenRowIsOutOfBounds() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);

        tb.writeText("1111");
        tb.writeText("2222");
        tb.writeText("3333");

        assertThrows(OutOfBoundsException.class, () -> tb.getLineFromScrollback(-1));
    }

    @Test
    void getLineFromScrollbackReturnsEmptyLineStringIfAccessedLineIsEmpty() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        assertEquals("    ", tb.getLineFromScrollback(5));
    }


    @Test
    void getAttributesFromScrollbackPositionReturnsNull() {
        ITerminalBuffer tb = new TerminalBuffer(4, 3, 10);
        assertNull(tb.getAttributesFromScrollbackAt(5, 2));
    }
}
