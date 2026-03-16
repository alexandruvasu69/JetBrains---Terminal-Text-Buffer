package com.example.scrollback;

import com.example.cell.Cell;
import com.example.cell.ICell;
import com.example.cell.ICellAttributes;
import com.example.shared.TerminalRenderer;

public class Scrollback implements IScrollback{
    private int maxSize;
    private int width;
    private int next;
    private int size;
    private ICell[][] grid;

    public Scrollback(int width, int maxSize) {
        this.maxSize = maxSize;
        this.width = width;
        this.next = 0;
        this.size = 0;
        grid = new ICell[maxSize][width];
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void addLine(ICell[] line) {
        if (line == null) {
            throw new IllegalArgumentException("line cannot be null");
        }
        if (line.length != width) {
            throw new IllegalArgumentException("invalid line width");
        }
        grid[next] = line;
        moveNext();

        if (size < maxSize) {
            size++;
        }
    }


    @Override
    public void clear() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col].resetCell();
            }
       } 

       this.next = 0;
       this.size = 0;
    }
    

    @Override
    public String getContentAsString() {
        int oldest = getOldest();

        TerminalRenderer tr = new TerminalRenderer();
        for (int i = 0; i < size; i++) {
            int physicalIndex = (oldest + i) % maxSize;
            tr.appendLine(grid[physicalIndex]).appendNewLine();
        }
        tr.removeLastChar();

        return tr.build();
    }


    @Override
    public String getLineAsString(int row) {
        if (row < 0 || row >= this.maxSize) {
            throw new IllegalArgumentException();
        }

        int physicalIndex = (getOldest() + row) % maxSize;
        ICell[] line = grid[physicalIndex];

        if (line[0] == null) {
            return String.valueOf(Cell.EMTPTY_CHAR).repeat(width);
        }


        TerminalRenderer tr = new TerminalRenderer();
        tr.appendLine(grid[physicalIndex]);

        return tr.build();
    }

    @Override
    public char getCharacterAt(int row, int col) {
        if (row < 0 || col < 0 || row >= this.maxSize || col >= this.width) {
            throw new IllegalArgumentException();
        }

        int physicalIndex = (getOldest() + row) % maxSize;
        ICell[] line = grid[physicalIndex];

        if (line[0] == null) {
            return Cell.EMTPTY_CHAR;
        }

        return line[col].getCharacter();
    }


    @Override
    public ICellAttributes getAttributesAt(int row, int col) {
        if (row < 0 || col < 0 || row >= this.maxSize || col >= this.width) {
            throw new IllegalArgumentException();
        }

        int physicalIndex = (getOldest() + row) % maxSize;
        ICell[] line = grid[physicalIndex];

        if (line[0] == null) {
            return null;
        }


        return line[col].getAttributes();
    }

    private void moveNext() {
        this.next = (next + 1) % maxSize;
    }

    private int getOldest() {
        if (size < maxSize) {
            return 0;
        }
        return next;
    }
}
