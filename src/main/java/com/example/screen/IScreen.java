package com.example.screen;

import com.example.cell.CellAttributes;
import com.example.cell.ICell;
import com.example.shared.IContentAccess;

public interface IScreen extends IContentAccess {
    int getWidth();
    int getHeight();

    // editing
    void writeCell(char character, CellAttributes attributes, int row, int col);
    ICell insertCell(char character, CellAttributes attributes, int row, int col);
    void fillLine(char character, CellAttributes attributes, int row);
    ICell[] insertEmptyLine();
    void clearScreen();
}
