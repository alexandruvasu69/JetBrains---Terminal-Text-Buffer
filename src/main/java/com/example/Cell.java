package com.example;

public class Cell implements ICell {
    private static char EMTPTY_CHAR = ' ';
    private char character = EMTPTY_CHAR;

    public Cell() {}

    private Cell(ICell cell) {
        this.character = cell.getCharacter();
    }

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public void setCharacter(char character) {
        this.character = character;
    }

    @Override
    public boolean isEmpty() {
        return character == ' ';
    }

    @Override
    public void resetCell() {
        this.character = EMTPTY_CHAR;
    }

    public static ICell cloneFrom(ICell cell) {
        return new Cell(cell);
    }
}
