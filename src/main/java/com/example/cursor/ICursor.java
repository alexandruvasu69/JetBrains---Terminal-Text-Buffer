package com.example.cursor;

public interface ICursor {
    int getRowPosition();
    int getColumnPosition();

    void setRowPosition(int rowPosition);
    void setColumnPosition(int columnPosition);

    void moveUp(int offset);
    void moveDown(int offset);
    void moveLeft(int offset);
    void moveRight(int offset);
}
