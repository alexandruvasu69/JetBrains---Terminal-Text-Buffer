package com.example.cell;

public class Cell implements ICell {
    private final static char EMTPTY_CHAR = ' ';
    private char character;
    private CellAttributes attributes;
    
    public Cell() {
        this.character = EMTPTY_CHAR;
        this.attributes = new CellAttributes();
    }

    public Cell(char character, CellAttributes attributes) {
        this.character = character;
        this.attributes = attributes;
    }

    private Cell(ICell cell) {
        this.character = cell.getCharacter();
        this.attributes = CellAttributes.cloneFrom(cell.getAttributes());
    }

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public CellAttributes getAttributes() {
        return this.attributes;
    }

    @Override
    public void setCharacter(char character) {
        this.character = character;
    }

    @Override
    public void setCellAttributes(CellAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean isEmpty() {
        return character == EMTPTY_CHAR;
    }

    @Override
    public void resetCell() {
        this.character = EMTPTY_CHAR;
        this.attributes = CellAttributes.getDefaultAttributes();
    }

    public static ICell cloneFrom(ICell cell) {
        return new Cell(cell);
    }
}
