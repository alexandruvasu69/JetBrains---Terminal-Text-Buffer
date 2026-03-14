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
        this.cursor = new Cursor(height - 2, width - 7);
    }

    public TerminalBuffer(int width, int height, int scrollBackMaxSize, ICursor cursor) {
        this(width, height, scrollBackMaxSize);
        this.cursor = cursor;
    }

    // screen operations
    @Override
    public int getScreenWidth() {
        return screen.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return screen.getHeight();
    }

    @Override
    public void writeText(String text) {
        for (char c : text.toCharArray()) {
            screen.writeCell(c, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    @Override
    public void insertText(String text) {
        for (char c : text.toCharArray()) {
            insertChar(c, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    private void insertChar(char c, int row, int col) {
        char lastChar = screen.insertCell(c, row, col);

        if ((lastChar != ' ')  && !isPositionOutOfBounds(row, col)) {
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

    @Override
    public void clearScreen() {
        screen.clearScreen();
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
    private boolean isPositionOutOfBounds(int row, int col) {
        return isRowOutOfBounds(row) || isColOutOfBounds(col);
    }
    
    private boolean isColOutOfBounds(int col) {
        return col >= screen.getWidth() || col < 0;
    }

    private boolean isRowOutOfBounds(int row) {
        return row >= screen.getHeight() || row < 0;
    }

    private void validateOffset(int offset) {
        if (offset < 0) {
            throw new OffsetValueException();
        }
    }

    private void validateCol(int col) {
        if (isColOutOfBounds(col)) {
            throw new OutOfBoundsException();
        }
    }

    private void validateRow(int row) {
        if (isRowOutOfBounds(row)) {
            throw new OutOfBoundsException();
        }
    }

    // cursor operations
    private void moveCursorNext() {
        int col = cursor.getColumnPosition();

        try {
            moveCursorRight(1);
        } catch(OutOfBoundsException e1) {
            try {
                setCursorPosition(0, col + 1);
            } catch (OutOfBoundsException e2) {

                setCursorPosition(screen.getHeight() - 1, screen.getWidth() - 1);
            }
        }
    }

    @Override
    public void moveCursorDown(int offset) throws OutOfBoundsException, OffsetValueException {
        validateOffset(offset);
        int newRow = cursor.getRowPosition() + offset;

        validateRow(newRow);

        cursor.setRowPosition(newRow);
    }

    @Override
    public void moveCursorUp(int offset) throws OutOfBoundsException, OffsetValueException {
        validateOffset(scrollBackMaxSize);
        int newRow = cursor.getRowPosition() - offset;

        validateRow(newRow);

        cursor.setRowPosition(newRow);
    }

    @Override
    public void moveCursorLeft(int offset) throws OutOfBoundsException, OffsetValueException {
        validateOffset(offset);
        int newCol = cursor.getColumnPosition() + offset;

        validateCol(newCol);

        cursor.setColumnPosition(newCol);
    }

    @Override
    public void moveCursorRight(int offset) throws OutOfBoundsException {
        validateOffset(offset);
        int newCol = cursor.getColumnPosition() - offset;

        validateCol(newCol);

        cursor.setColumnPosition(newCol);
    }

    @Override
    public void setCursorColumn(int col) throws OutOfBoundsException {
        validateCol(col);
        cursor.setColumnPosition(col);
    }

    @Override
    public void setCursorRow(int row) throws OutOfBoundsException {
        validateRow(row);
        cursor.setRowPosition(row);
    }

    @Override
    public void setCursorPosition(int row, int col) throws RuntimeException {
        setCursorRow(row);
        setCursorColumn(col);
    }
}
