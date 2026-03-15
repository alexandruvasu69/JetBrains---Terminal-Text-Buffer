package com.example.cursor;

public class Cursor implements ICursor {
    int rowPosition;
    int columnPosition;

    public Cursor(int rowPosition, int columnPosition) {
        this.rowPosition = rowPosition;
        this.columnPosition = columnPosition;
    }

    @Override
    public int getRowPosition() {
        return rowPosition;
    }

    @Override
    public int getColumnPosition() {
        return columnPosition;
    }

    @Override
    public void setRowPosition(int rowPosition) {
        this.rowPosition = rowPosition;
    }

    @Override
    public void setColumnPosition(int columnPosition) {
        this.columnPosition = columnPosition;
    }

    @Override
    public void moveUp(int offset) {
        this.rowPosition -= offset;
    }

    @Override
    public void moveDown(int offset) {
        this.rowPosition += offset;
    }

    @Override
    public void moveLeft(int offset) {
        this.columnPosition -= offset;
    }

    @Override
    public void moveRight(int offset) {
        this.columnPosition += offset;
    }

}
