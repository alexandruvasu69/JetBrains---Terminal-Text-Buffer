package com.example;

public class TerminalBuffer implements ITerminalBuffer{
    private IScreen screen;
    private ICursor cursor;
    private int scrollBackMaxSize; 
    private TerminalColor foregroundColor;
    private TerminalColor backgroundColor;
    private TextStyle textStyle;

    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        this.screen = new Screen(width, height);
        this.scrollBackMaxSize = scrollBackMaxSize;
        this.foregroundColor = TerminalColor.BLACK;
        this.backgroundColor = TerminalColor.BLACK;
        this.cursor = new Cursor(height - 2, width - 2);
    }

    public TerminalBuffer(int width, int height, int scrollBackMaxSize, ICursor cursor) {
        this(width, height, scrollBackMaxSize);
        this.cursor = cursor;
    }


    // screen operations
    @Override
    public int getWidth() {
        return screen.getWidth();
    }

    @Override
    public int getHeight() {
        return screen.getHeight();
    }

    @Override
    public void writeText(String text) {
    }

    @Override
    public void insertText(String text) {
        for (char c : text.toCharArray()) {
            insertChar(c, cursor.getRowPosition(), cursor.getColumnPosition());
        }
    }

    private void insertChar(char c, int row, int col) {
        char lastChar = screen.insertCell(c, row, col);

        if ((lastChar != ' ') && !isColOutOfBounds(col)) {
            insertChar(lastChar, row + 1, 0);
        }
    }

    @Override
    public void fillLine(char character) {
        int currentRow = this.cursor.getRowPosition();
        screen.fillLine(character, currentRow);
    }

    @Override
    public String getScreenContent() {
        return screen.getScreenString();
    }

    // scrollback operations
    @Override
    public int getScrollBackMaxSize() {
        return scrollBackMaxSize;
    }

    // global operations
    @Override
    public void setForegroundColor(TerminalColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void setBackgroundColor(TerminalColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    // helpers
    private boolean isColOutOfBounds(int col) {
        return col >= screen.getHeight() || col < 0; 
    }
}
