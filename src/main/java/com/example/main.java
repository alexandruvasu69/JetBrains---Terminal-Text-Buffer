package com.example;

public class main {
    public static void main(String[] args) {
        ITerminalBuffer terminalBuffer = new TerminalBuffer(10, 3, 10);
        terminalBuffer.setBackgroundColor(Color.BLACK);
        terminalBuffer.setForegroundColor(Color.BLUE);
        terminalBuffer.fillLine('3');
        terminalBuffer.insertText("Text");
        terminalBuffer.insertEmptyLine();
        terminalBuffer.insertEmptyLine();
        System.out.println(terminalBuffer.getScreenContent());
        System.out.println(terminalBuffer.getScrollbackContent());
    }
}
