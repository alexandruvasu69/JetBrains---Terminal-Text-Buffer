package com.example;

public class TerminalBuffer implements ITerminalBuffer{
    private IScreen screen;
    private ICursor cursor;
    private IScrollback scrollback; 
    private CellAttributes cellAttributes;

    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        this.screen = new Screen(width, height);
        this.scrollback = new Scrollback(width, scrollBackMaxSize);
        this.cellAttributes = CellAttributes.getDefaultAttributes();
        this.cursor = new Cursor(0, 0);
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
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes); 
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            screen.writeCell(c, attributes, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    @Override
    public void insertText(String text) {
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            insertChar(c, attributes, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    private void insertChar(char c, CellAttributes attributes, int row, int col) {
        ICell lastCell = screen.insertCell(c, attributes, row, col);

        if (!(lastCell.isEmpty())  && !isPositionOutOfBounds(row + 1, col)) {
            insertChar(lastCell.getCharacter(), lastCell.getAttributes(), row + 1, 0);
        }
    }

    @Override
    public void fillLine(char character) {
        int currentRow = this.cursor.getRowPosition();
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        screen.fillLine(character, attributes, currentRow);
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
        return scrollback.getMaxSize();
    }

    // global operations
    @Override
    public void setForegroundColor(Color foregroundColor) {
        this.cellAttributes.setForegroundColor(foregroundColor);
    }

    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.cellAttributes.setBackgroundColor(backgroundColor);
    }

    @Override
    public void setBold(boolean isBold) {
        this.cellAttributes.setBold(isBold);
    }

    @Override
    public void setItalic(boolean isItalic) {
        this.cellAttributes.setItalic(isItalic);
    }

    @Override
    public void setUnderline(boolean isUnderline) {
        this.cellAttributes.setUnderline(isUnderline);
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

    private void validateOffset(int offset) throws OffsetValueException {
        if (offset < 0) {
            throw new OffsetValueException();
        }
    }

    private void validateCol(int col) throws OutOfBoundsException {
        if (isColOutOfBounds(col)) {
            throw new OutOfBoundsException();
        }
    }

    private void validateRow(int row) throws OutOfBoundsException {
        if (isRowOutOfBounds(row)) {
            throw new OutOfBoundsException();
        }
    }

    // cursor operations
    private void moveCursorNext() {
        int row = cursor.getRowPosition();

        try {
            moveCursorRight(1);
        } catch(OutOfBoundsException e1) {
            try {
                setCursorPosition(row + 1, 0);
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
        validateOffset(offset);
        int newRow = cursor.getRowPosition() - offset;

        validateRow(newRow);

        cursor.setRowPosition(newRow);
    }

    @Override
    public void moveCursorLeft(int offset) throws OutOfBoundsException, OffsetValueException {
        validateOffset(offset);
        int newCol = cursor.getColumnPosition() - offset;

        validateCol(newCol);

        cursor.setColumnPosition(newCol);
    }

    @Override
    public void moveCursorRight(int offset) throws OutOfBoundsException {
        validateOffset(offset);
        int newCol = cursor.getColumnPosition() + offset;

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

    @Override
    public void insertEmptyLine() {
        ICell[] cell = this.screen.insertEmptyLine();
        this.scrollback.addLine(cell);
    }

    @Override
    public String getScrollbackContent() {
        return this.scrollback.getContent();
    }

    @Override
    public String getLineFromScreen(int row) throws OutOfBoundsException {
        validateRow(row);
        return this.screen.getLineString(row);
    }

    @Override
    public String getLineFromScrollback(int row) throws OutOfBoundsException {
        validateRow(row);
        return this.scrollback.getLineString(row);
    }


}
