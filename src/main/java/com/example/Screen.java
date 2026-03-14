package com.example;

public class Screen implements IScreen{
    private ICell[][] grid;
    private int width;
    private int height;

    public Screen(int width, int height) {
        this.grid = new Cell[height][width];
        this.width = width;
        this.height = height;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell();
            }
        }
    }

    @Override
    public void fillLine(char character, int row) {
        for (int col = 0; col < width; col++) {
            grid[row][col].setCharacter(character);
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
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                sb.append(grid[row][col].getCharacter());
            }
            sb.append('\n');
        } 

        return sb.toString();
    }

    @Override
    public void writeCell(char character, int row, int col) {
        this.grid[row][col].setCharacter(character);
    }

    @Override
    public char insertCell(char character, int row, int col) {
        ICell lastCell = Cell.cloneFrom(grid[row][width-1]);

        for (int indexCol = width - 1; indexCol > col; indexCol--) {
            grid[row][indexCol] = Cell.cloneFrom(grid[row][indexCol - 1]);
        }

        grid[row][col].setCharacter(character);

        return lastCell.getCharacter();
    }

    @Override
    public void clearScreen() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                this.grid[row][col].resetCell();
            }
        }
    }
}
