package com.example;

public interface IScreen {
    int getWidth();
    int getHeight();

    void writeCell(char character, CellAttributes attributes, int row, int col);
    ICell insertCell(char character, CellAttributes attributes, int row, int col);
    void fillLine(char character, CellAttributes attributes, int row);
    String getScreenString();
    void clearScreen();
}
