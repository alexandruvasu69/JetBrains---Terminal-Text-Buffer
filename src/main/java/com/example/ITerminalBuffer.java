package com.example;

public interface ITerminalBuffer {
    int getWidth();
    int getHeight();
    int getScrollBackMaxSize();

    void setForegroundColor(TerminalColor color); 
    void setBackgroundColor(TerminalColor color);
    void setTextStyle(TextStyle style);

    void fillLine(char character);

    String getScreenContent();
}