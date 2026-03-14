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
}
