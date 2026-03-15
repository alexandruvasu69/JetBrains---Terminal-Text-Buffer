package com.example.terminal_buffer;

import com.example.cell.ICellAttributes;

// screen operations
public interface IScreenOps {
    int getScreenWidth();
    int getScreenHeight();

    // editing
    void writeText(String text);
    void insertText(String text);
    void fillLine(char character);
    void clearScreen();
    void insertEmptyLine();

    // content access
    String getScreenContent();
    String getLineFromScreen(int row) throws RuntimeException;
    char getCharacterFromScreenAt(int row, int col) throws RuntimeException;
    ICellAttributes getAttributesFromScreenAt(int row, int col) throws RuntimeException;
}
