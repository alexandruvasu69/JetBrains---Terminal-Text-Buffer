package com.example;

public interface ITerminalBuffer {
    // screen operations
    int getScreenWidth();
    int getScreenHeight();
    void writeText(String text);
    void insertText(String text);
    void fillLine(char character);
    void clearScreen();
    
    // scrollback operations
    int getScrollBackMaxSize();
    void insertEmptyLine();

    // style operations
    void setForegroundColor(Color color); 
    void setBackgroundColor(Color color);
    void setBold(boolean isBold);
    void setItalic(boolean isItalic);
    void setUnderline(boolean isUnderline);

    // cursor operations
    void moveCursorUp(int offset) throws RuntimeException;
    void moveCursorDown(int offset) throws RuntimeException;
    void moveCursorLeft(int offset) throws RuntimeException;
    void moveCursorRight(int offset) throws RuntimeException;
    void setCursorRow(int row) throws RuntimeException;
    void setCursorColumn(int col) throws RuntimeException;
    void setCursorPosition(int row, int col) throws RuntimeException;

    // content access
    String getScreenContent();
    String getScrollbackContent(); // TO-DO: make this screen + scrollback content
    String getLineFromScreen(int row) throws RuntimeException;
    String getLineFromScrollback(int row) throws RuntimeException;
}