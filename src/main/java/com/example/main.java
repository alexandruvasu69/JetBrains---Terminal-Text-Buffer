package com.example;

import com.example.style.Color;
import com.example.terminal_buffer.ITerminalBuffer;
import com.example.terminal_buffer.TerminalBuffer;

public class main {
    public static void main(String[] args) {
        ITerminalBuffer terminalBuffer = new TerminalBuffer(10, 1, 10);
        terminalBuffer.setBackgroundColor(Color.BLACK);
        terminalBuffer.setForegroundColor(Color.BLUE);
        terminalBuffer.fillLine('3');
        terminalBuffer.insertText("Text");
        // terminalBuffer.insertEmptyLine();
        // terminalBuffer.insertEmptyLine();
        System.out.println(terminalBuffer.getScrollbackContent());
    }
}
