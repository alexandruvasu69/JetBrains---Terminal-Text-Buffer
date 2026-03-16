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
        ITerminalBuffer tb = new TerminalBuffer(5, 1, 10);
        tb.fillLine('3');
        tb.setCursorPosition(0, 0);
        tb.insertText("text");
        assertEquals("3333", tb.getLineFromScreen(0).trim());
        assertEquals("text3", tb.getLineFromScrollback(0));
    }

    @Test
    void insertTextDisplacesMultipleLines() {
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
}
