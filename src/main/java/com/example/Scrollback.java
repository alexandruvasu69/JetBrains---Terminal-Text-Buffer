package com.example;

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

    private void moveNext() {
        this.next = (next + 1) % maxSize;
    }

    private int getOldest() {
        if (size < maxSize) {
            return 0;
        }
        return next;
    }

    @Override
    public String getContent() {
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
    public String getLineString(int row) {
        TerminalRenderer tr = new TerminalRenderer();
        tr.appendLine(grid[row]);

        return tr.build();
    }



}
