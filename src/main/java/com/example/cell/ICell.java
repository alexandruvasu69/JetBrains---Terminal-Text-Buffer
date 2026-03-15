package com.example.cell;

public interface ICell {
    char getCharacter();
    CellAttributes getAttributes();

    void setCharacter(char character);
    void setCellAttributes(CellAttributes attributes);

    boolean isEmpty();

    void resetCell();
}