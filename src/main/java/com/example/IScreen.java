package com.example;

public interface IScreen {
    int getWidth();
    int getHeight();

    void writeCell(char character, CellAttributes attributes, int row, int col);
    ICell insertCell(char character, CellAttributes attributes, int row, int col);
    void fillLine(char character, CellAttributes attributes, int row);
    ICell[] insertEmptyLine();
    String getScreenString();
    void clearScreen();
    String getLineString(int row);
    char getCharacterAt(int row, int col);
}
