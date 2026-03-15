package com.example.shared;

import com.example.cell.ICellAttributes;

public interface IContentAccess {
    String getContentAsString();
    String getLineAsString(int row);
    char getCharacterAt(int row, int col);
    ICellAttributes getAttributesAt(int row, int col);
}
