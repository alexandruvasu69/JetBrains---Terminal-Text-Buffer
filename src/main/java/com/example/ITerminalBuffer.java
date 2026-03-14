package com.example;

public interface ITerminalBuffer {
    // screen operations
    int getScreenWidth();
    int getScreenHeight();
    void writeText(String text);
    void insertText(String text);
    void fillLine(char character);
    String getScreenContent();
    
    // scrollback operations
    int getScrollBackMaxSize();

    // style operations
    void setForegroundColor(TerminalColor color); 
    void setBackgroundColor(TerminalColor color);
    void setTextStyle(TextStyle style);

    // cursor operations
    void moveCursorUp(int offset) throws RuntimeException;
    void moveCursorDown(int offset) throws RuntimeException;
    void moveCursorLeft(int offset) throws RuntimeException;
    void moveCursorRight(int offset) throws RuntimeException;
    void setCursorRow(int row) throws RuntimeException;
    void setCursorColumn(int col) throws RuntimeException;
    void setCursorPosition(int row, int col) throws RuntimeException;
}