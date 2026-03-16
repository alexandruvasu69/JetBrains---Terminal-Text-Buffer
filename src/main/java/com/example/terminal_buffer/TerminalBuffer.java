package com.example.terminal_buffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

/**
 * Main facade for the terminal text buffer library.
 *
 * Composes a visible screen grid, a scrollback history buffer, a cursor,
 * and per-cell text styling into a single API that simulates console behavior.
 * Implements {@link ITerminalBuffer}, which aggregates screen, cursor,
 * scrollback, and style operations.
 */
public class TerminalBuffer implements ITerminalBuffer{
    private IScreen screen;
    private ICursor cursor;
    private IScrollback scrollback;
    private CellAttributes cellAttributes;
    private BoundsValidator validator;
    private CursorManager cursorManager;

    /**
     * Creates a new terminal buffer.
     *
     * @param width             number of columns in the screen grid (must be &gt; 0)
     * @param height            number of rows in the screen grid (must be &gt; 0)
     * @param scrollBackMaxSize maximum number of lines retained in scrollback history (must be &ge; 0)
     * @throws IllegalArgumentException if width or height is not positive, or scrollBackMaxSize is negative
     */
    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be greater than 0, got: " + width);
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0, got: " + height);
        }
        if (scrollBackMaxSize < 0) {
            throw new IllegalArgumentException("ScrollBack max size must be >= 0, got: " + scrollBackMaxSize);
        }

        this.screen = new Screen(width, height);
        this.scrollback = new Scrollback(width, scrollBackMaxSize);
        this.cellAttributes = CellAttributes.getDefaultAttributes();
        this.cursor = new Cursor(0, 0);
        this.validator = new BoundsValidator(this.screen, this.scrollback);
        this.cursorManager = new CursorManager(this.cursor, this.screen, this.validator);
    }

    // SCREEN OPERATIONS

    @Override
    public int getScreenWidth() {
        return screen.getWidth();
    }

    @Override
    public int getScreenHeight() {
        return screen.getHeight();
    }

    /** {@inheritDoc} */
    @Override
    public void writeText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null!");
        }
        if (text.length() == 0) {
            throw new IllegalArgumentException("Text must not be empty!");
        }

        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            screen.writeCell(c, attributes, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void insertText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null!");
        }
        if (text.length() == 0) {
            throw new IllegalArgumentException("Text must not be empty!");
        }

        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        List<ICell> overflow = new ArrayList<>();
        int lastInsertRow = cursor.getRowPosition();

        for (int i = 0; i < text.length(); i++) {
            int row = cursor.getRowPosition();
            int col = cursor.getColumnPosition();

            char c = text.charAt(i);
            ICell cell = screen.insertCell(c, attributes, row, col);
            lastInsertRow = row;
            overflow.add(cell);

            moveCursorNext();
        }
    

        Collections.reverse(overflow);
        overflow.removeIf(ICell::isEmpty);
        if (!overflow.isEmpty()) {
            int nextRow;
            if (lastInsertRow < screen.getHeight() - 1) {
                nextRow = lastInsertRow + 1;
            } else {
                insertEmptyLine();
                nextRow = screen.getHeight() - 1;
            }
            flushOverflow(overflow, nextRow, 0);
        }
    }

    private void flushOverflow(List<ICell> overflow, int row, int col) {
        if (overflow.isEmpty()) return;

        List<ICell> pushedOff = new ArrayList<>();
        List<ICell> remaining = new ArrayList<>();
        int currentCol = col;

        for (int i = 0; i < overflow.size(); i++) {
            if (currentCol >= screen.getWidth()) {
                remaining = new ArrayList<>(overflow.subList(i, overflow.size()));
                break;
            }
            ICell pushed = screen.insertCell(
                overflow.get(i).getCharacter(),
                overflow.get(i).getAttributes(),
                row, currentCol);
            pushedOff.add(pushed);
            currentCol++;
        }

        Collections.reverse(pushedOff);
        List<ICell> newOverflow = new ArrayList<>(remaining);
        newOverflow.addAll(pushedOff);
        newOverflow.removeIf(ICell::isEmpty);

        if (!newOverflow.isEmpty()) {
            int nextRow;
            if (row < screen.getHeight() - 1) {
                nextRow = row + 1;
            } else {
                insertEmptyLine();
                nextRow = screen.getHeight() - 1;
            }
            flushOverflow(newOverflow, nextRow, 0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void fillLine(char character) {
        if (character == '\n' || character == '\t' || character == '\r') {
            throw new IllegalArgumentException();
        }
        int currentRow = this.cursor.getRowPosition();
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        screen.fillLine(character, attributes, currentRow);
    }

    /** {@inheritDoc} */
    @Override
    public void clearScreen() {
        screen.clearScreen();
    }

    /** {@inheritDoc} */
    @Override
    public void insertEmptyLine() {
        ICell[] cells = this.screen.insertEmptyLine();
        boolean allEmpty = true;
        for (ICell cell : cells) {
            if (!cell.isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (!allEmpty) {
            this.scrollback.addLine(cells);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getScreenContent() {
        return screen.getContentAsString();
    }

    /** {@inheritDoc} */
    @Override
    public String getLineFromScreen(int row) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        return this.screen.getLineAsString(row);
    }

    /** {@inheritDoc} */
    @Override
    public char getCharacterFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getCharacterAt(row, col);
    }

    /** {@inheritDoc} */
    @Override
    public ICellAttributes getAttributesFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getAttributesAt(row, col);
    }

    // SCROLLBACK OPERATIONS

    /** {@inheritDoc} */
    @Override
    public int getScrollBackMaxSize() {
        return scrollback.getMaxSize();
    }

    /** {@inheritDoc} */
    @Override
    public String getScrollbackContent() {
        return this.scrollback.getContentAsString();
    }

    /** {@inheritDoc} */
    @Override
    public String getLineFromScrollback(int row) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        return this.scrollback.getLineAsString(row);
    }

    /** {@inheritDoc} */
    @Override
    public char getCharacterFromScrollbackAt(int row, int col) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        validator.validateScrollbackCol(col);

        return this.scrollback.getCharacterAt(row, col);
    }

    /** {@inheritDoc} */
    @Override
    public ICellAttributes getAttributesFromScrollbackAt(int row, int col) throws RuntimeException {
        validator.validateScrollbackRow(row); // TODO: fix validation for scrollback
        validator.validateScrollbackCol(col);
        return this.scrollback.getAttributesAt(row, col);
    }

    // STYLE OPERATIONS

    /** {@inheritDoc} */
    @Override
    public void setForegroundColor(Color foregroundColor) {
        this.cellAttributes.setForegroundColor(foregroundColor);
    }

    /** {@inheritDoc} */
    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.cellAttributes.setBackgroundColor(backgroundColor);
    }

    /** {@inheritDoc} */
    @Override
    public void setBold(boolean isBold) {
        this.cellAttributes.setBold(isBold);
    }

    /** {@inheritDoc} */
    @Override
    public void setItalic(boolean isItalic) {
        this.cellAttributes.setItalic(isItalic);
    }

    /** {@inheritDoc} */
    @Override
    public void setUnderline(boolean isUnderline) {
        this.cellAttributes.setUnderline(isUnderline);
    }

    // CURSOR OPERATIONS

    private void moveCursorNext() {
        int row = cursor.getRowPosition();
        int col = cursor.getColumnPosition();

        if (col < screen.getWidth() - 1) {
            cursor.setColumnPosition(col + 1);
            return;
        }

        if (row < screen.getHeight() - 1) {
            cursor.setRowPosition(row + 1);
            cursor.setColumnPosition(0);
            return;
        }

        insertEmptyLine();
        cursor.setRowPosition(screen.getHeight() - 1);
        cursor.setColumnPosition(0);
    }

    /** {@inheritDoc} */
    @Override
    public int getCursorRow() {
        return this.cursorManager.getCursorRow();
    }

    /** {@inheritDoc} */
    @Override
    public int getCursorColumn() {
        return this.cursorManager.getCursorColumn();
    }

    /** {@inheritDoc} */
    @Override
    public void moveCursorDown(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorDown(offset);
    }

    /** {@inheritDoc} */
    @Override
    public void moveCursorUp(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorUp(offset);
    }

    /** {@inheritDoc} */
    @Override
    public void moveCursorLeft(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorLeft(offset);
    }

    /** {@inheritDoc} */
    @Override
    public void moveCursorRight(int offset) throws OutOfBoundsException {
        this.cursorManager.moveCursorRight(offset);
    }

    /** {@inheritDoc} */
    @Override
    public void setCursorColumn(int col) throws OutOfBoundsException {
        this.cursorManager.setCursorColumn(col);
    }

    /** {@inheritDoc} */
    @Override
    public void setCursorRow(int row) throws OutOfBoundsException {
        this.cursorManager.setCursorRow(row);
    }

    /** {@inheritDoc} */
    @Override
    public void setCursorPosition(int row, int col) throws RuntimeException {
        this.cursorManager.setCursorPosition(row, col);
    }

    /** {@inheritDoc} */
    @Override
    public void clearScreenAndScrollback() {
        clearScreen();
        this.scrollback.clear();
    }

    /** {@inheritDoc} */
    @Override
    public String getScreenAndScrollbackContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getScreenContent()).append('\n').append(this.getScrollbackContent());

        return sb.toString();
    }
}
