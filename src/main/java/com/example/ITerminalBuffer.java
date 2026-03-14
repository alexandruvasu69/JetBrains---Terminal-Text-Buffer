package com.example;

public interface ITerminalBuffer {
    int getWidth();
    int getHeight();
    int getScrollBackMaxSize();

    void setForegroundColor(TerminalColor color); 
    void setBackgroundColor(TerminalColor color);
    void setTextStyle(TextStyle style);

    void writeText(String text);
    void insertText(String text);
    void fillLine(char character);

    String getScreenContent();
}