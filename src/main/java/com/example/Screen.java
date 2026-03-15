package com.example;

public class Screen implements IScreen{
    private ICell[][] grid;
    private int width;
    private int height;
    private int firstRow;

    public Screen(int width, int height) {
        this.grid = new ICell[height][width];
        this.width = width;
        this.height = height;
        this.firstRow = 0;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell();
            }
        }
    }

    private int toPhysicalRow(int logicalRow) {
        return (firstRow + logicalRow) % height;
    }

    @Override
    public void fillLine(char character, CellAttributes attributes, int row) {
        for (int col = 0; col < width; col++) {
            writeCell(character, attributes, row, col);
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public String getScreenString() {
        TerminalRenderer tr = new TerminalRenderer();
        for (int row = 0; row < height; row++) {
            tr.appendLine(grid[toPhysicalRow(row)]).appendNewLine();
        } 
        tr.removeLastChar();

        return tr.build();
    }

    @Override
    public void writeCell(char character, CellAttributes attributes, int row, int col) {
        this.grid[toPhysicalRow(row)][col].setCharacter(character);
        this.grid[toPhysicalRow(row)][col].setCellAttributes(attributes);
    }

    @Override
    public ICell insertCell(char character, CellAttributes attributes, int row, int col) {
        int physicalRow = toPhysicalRow(row);
        ICell lastCell = Cell.cloneFrom(grid[physicalRow][width-1]);

        for (int indexCol = width - 1; indexCol > col; indexCol--) {
            grid[physicalRow][indexCol] = grid[physicalRow][indexCol - 1];
        }

        ICell cell = new Cell(character, attributes);
        this.grid[physicalRow][col] = cell;

        return lastCell;
    }

    @Override
    public void clearScreen() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.grid[row][col].resetCell();
            }
        }
        this.firstRow = 0;
    }

    @Override
    public ICell[] insertEmptyLine() {
        int oldTopPhysicalRow = toPhysicalRow(0);
        ICell[] removedLine = grid[oldTopPhysicalRow];

        firstRow = (firstRow + 1) % height;

        int bottomPhysicalRow = toPhysicalRow(height - 1);
        grid[bottomPhysicalRow] = new ICell[width];
        for (int col = 0; col < width; col++) {
            grid[bottomPhysicalRow][col] = new Cell('-', CellAttributes.getDefaultAttributes());
        }

        return removedLine;
    }

    @Override
    public String getLineString(int row) {
        TerminalRenderer tr = new TerminalRenderer();
        tr.appendLine(grid[row]);
        return tr.build();
    }


}
