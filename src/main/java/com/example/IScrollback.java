package com.example;

public interface IScrollback {
    void addLine(ICell[] line);
    int getMaxSize();
    int getWidth();
    String getContent();
    String getLineString(int row);
    void clear();
    char getCharacterAt(int row, int col);
}
