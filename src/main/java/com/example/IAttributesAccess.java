package com.example;

import com.example.cell.ICellAttributes;

public interface IAttributesAccess {
    ICellAttributes getAttributesAt(int row, int col);
}
