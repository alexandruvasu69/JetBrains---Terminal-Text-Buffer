package com.example.scrollback;

import com.example.IAttributesAccess;
import com.example.cell.ICell;

public interface IScrollback extends IAttributesAccess{
    void addLine(ICell[] line);
    int getMaxSize();
    int getWidth();
    String getContent();
    String getLineString(int row);
    void clear();
    char getCharacterAt(int row, int col);
}
