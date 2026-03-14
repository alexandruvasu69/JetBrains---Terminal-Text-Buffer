package com.example;

public class Cell implements ICell {
    private char character = ' ';

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

    public static ICell cloneFrom(ICell cell) {
        return new Cell(cell);
    }
}
