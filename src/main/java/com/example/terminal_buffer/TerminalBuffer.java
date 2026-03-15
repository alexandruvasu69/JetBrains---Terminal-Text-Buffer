package com.example.terminal_buffer;

import com.example.cell.CellAttributes;
import com.example.cell.ICell;
import com.example.cell.ICellAttributes;
import com.example.cursor.Cursor;
import com.example.cursor.ICursor;
import com.example.exceptions.OffsetValueException;
import com.example.exceptions.OutOfBoundsException;
import com.example.screen.IScreen;
import com.example.screen.Screen;
import com.example.scrollback.IScrollback;
import com.example.scrollback.Scrollback;
import com.example.style.Color;
import com.example.terminal_buffer.helpers.BoundsValidator;
import com.example.terminal_buffer.helpers.CursorManager;

public class TerminalBuffer implements ITerminalBuffer{
    private IScreen screen;
    private ICursor cursor;
    private IScrollback scrollback; 
    private CellAttributes cellAttributes;
    private BoundsValidator validator;
    private CursorManager cursorManager;

    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        this.screen = new Screen(width, height);
        this.scrollback = new Scrollback(width, scrollBackMaxSize);
        this.cellAttributes = CellAttributes.getDefaultAttributes();
        this.cursor = new Cursor(0, 0);
        this.validator = new BoundsValidator(this.screen, this.scrollback);
        this.cursorManager = new CursorManager(this.cursor, this.screen, this.validator);
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

        if (lastCell.isEmpty()) {
            return;
        }

        int nextRow = row + 1;

        if (!validator.isScreenPositionOutOfBounds(nextRow, 0)) {
            insertChar(lastCell.getCharacter(), lastCell.getAttributes(), nextRow, 0);
        } else {
            insertEmptyLine();
            insertChar(lastCell.getCharacter(), lastCell.getAttributes(), screen.getHeight() - 1, 0);
        }
    }

    @Override
    public void fillLine(char character) {
        int currentRow = this.cursor.getRowPosition();
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        screen.fillLine(character, attributes, currentRow);
    }

    @Override
    public void clearScreen() {
        screen.clearScreen();
    }

    @Override
    public void insertEmptyLine() {
        ICell[] cell = this.screen.insertEmptyLine();
        this.scrollback.addLine(cell);
    }

    @Override
    public String getScreenContent() {
        return screen.getContentAsString();
    }

    @Override
    public String getLineFromScreen(int row) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        return this.screen.getLineAsString(row);
    }

    @Override
    public char getCharacterFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getCharacterAt(row, col);
    }

    @Override
    public ICellAttributes getAttributesFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getAttributesAt(row, col);
    }

    // SCROLLBACK OPERATIONS
    @Override
    public int getScrollBackMaxSize() {
        return scrollback.getMaxSize();
    }

    @Override
    public String getScrollbackContent() {
        return this.scrollback.getContentAsString();
    }

    @Override
    public String getLineFromScrollback(int row) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        return this.scrollback.getLineAsString(row);
    }


    @Override
    public char getCharacterFromScrollbackAt(int row, int col) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        validator.validateScrollbackCol(col);

        return this.scrollback.getCharacterAt(row, col);
    }


    @Override
    public ICellAttributes getAttributesFromScrollbackAt(int row, int col) throws RuntimeException {
        validator.validateScrollbackRow(row); // TODO: fix validation for scrollback
        validator.validateScrollbackCol(col);
        return this.scrollback.getAttributesAt(row, col);
    }

    // STYLE OPERATIONS
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

    // CURSOR OPERATIONS
    private void moveCursorNext() {
        this.cursorManager.moveCursorNext();
    }

    @Override
    public void moveCursorDown(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorDown(offset);
    }

    @Override
    public void moveCursorUp(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorUp(offset);
    }

    @Override
    public void moveCursorLeft(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorLeft(offset);
    }

    @Override
    public void moveCursorRight(int offset) throws OutOfBoundsException {
        this.cursorManager.moveCursorRight(offset);
    }

    @Override
    public void setCursorColumn(int col) throws OutOfBoundsException {
        this.cursorManager.setCursorColumn(col);
    }

    @Override
    public void setCursorRow(int row) throws OutOfBoundsException {
        this.cursorManager.setCursorRow(row);
    }

    @Override
    public void setCursorPosition(int row, int col) throws RuntimeException {
        this.cursorManager.setCursorPosition(row, col);
    }

    @Override
    public void clearScreenAndScrollback() {
        clearScreen();
        this.scrollback.clear();
    }

    @Override
    public String getScreenAndScrollbackContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getScreenContent()).append('\n').append(this.getScrollbackContent());

        return sb.toString();
    }

    
    
}
