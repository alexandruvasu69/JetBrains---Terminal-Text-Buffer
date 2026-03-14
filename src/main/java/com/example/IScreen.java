package com.example;

public interface IScreen {
    int getWidth();
    int getHeight();

    void writeCell(char character, int row, int col);
    char insertCell(char character, int row, int col);
    void fillLine(char character, int row);
    String getScreenString();
    void clearScreen();
}
