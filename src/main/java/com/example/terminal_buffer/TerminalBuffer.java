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

    /**
     * Writes text starting at the current cursor position, overwriting existing cells.
     * The cursor advances after each character. If the cursor reaches the end of the
     * screen, a scroll is triggered and writing continues on the new bottom row.
     *
     * @param text the text to write
     */
    @Override
    public void writeText(String text) {
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            screen.writeCell(c, attributes, cursor.getRowPosition(), cursor.getColumnPosition());
            moveCursorNext();
        }
    }

    /**
     * Inserts text at the current cursor position, shifting existing cells to the right.
     * Characters pushed off the right edge of the row are collected as overflow,
     * reversed into correct left-to-right order, and recursively flushed to subsequent rows.
     *
     * @param text the text to insert
     */
    @Override
    public void insertText(String text) {
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        List<ICell> overflow = new ArrayList<>();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            ICell cell = screen.insertCell(c, attributes, cursor.getRowPosition(), cursor.getColumnPosition());
            overflow.add(cell);

            moveCursorNext();
        }
    

        Collections.reverse(overflow);
        overflow.removeIf(ICell::isEmpty);
        if (!overflow.isEmpty()) {
            int nextRow;
            int insertRow = cursor.getRowPosition();
            if (cursor.getRowPosition() < screen.getHeight() - 1) {
                nextRow = insertRow + 1;
            } else {
                insertEmptyLine();
                nextRow = screen.getHeight() - 1;
            }
            flushOverflow(overflow, nextRow, 0);
        }
    }

    /**
     * Recursively inserts overflow cells into the given row, starting at the specified column.
     *
     * Each overflow cell is inserted left-to-right using {@code insertCell}, which shifts
     * existing content to the right and returns the cell pushed off the right edge.
     * Because forward insertion accumulates pushed-off cells in reverse order, they are
     * reversed before being combined with any unprocessed overflow cells and flushed
     * to the next row. If the current row is the last one, a scroll is triggered first.
     *
     * @param overflow the cells to insert
     * @param row      the target row
     * @param col      the starting column
     */
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

    /**
     * Fills the entire row at the current cursor position with the given character,
     * using the current cell attributes.
     *
     * @param character the character to fill the row with
     */
    @Override
    public void fillLine(char character) {
        int currentRow = this.cursor.getRowPosition();
        CellAttributes attributes = CellAttributes.cloneFrom(cellAttributes);
        screen.fillLine(character, attributes, currentRow);
    }

    /** Resets all cells in the screen to their default (empty) state. */
    @Override
    public void clearScreen() {
        screen.clearScreen();
    }

    /**
     * Scrolls the screen up by one line: the oldest (top) row is removed from
     * the screen and, if it contains any non-empty cells, appended to the
     * scrollback history. A new empty row is added at the bottom.
     */
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

    /** Returns the full screen content as a string, with rows separated by newlines. */
    @Override
    public String getScreenContent() {
        return screen.getContentAsString();
    }

    /**
     * Returns a single screen row as a string.
     *
     * @param row zero-based row index
     * @throws OutOfBoundsException if the row index is outside the screen
     */
    @Override
    public String getLineFromScreen(int row) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        return this.screen.getLineAsString(row);
    }

    /**
     * Returns the character at the given screen position.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the screen
     */
    @Override
    public char getCharacterFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getCharacterAt(row, col);
    }

    /**
     * Returns the cell attributes at the given screen position.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the screen
     */
    @Override
    public ICellAttributes getAttributesFromScreenAt(int row, int col) throws OutOfBoundsException {
        validator.validateScreenRow(row);
        validator.validateScreenCol(col);
        return this.screen.getAttributesAt(row, col);
    }

    // SCROLLBACK OPERATIONS

    /** Returns the maximum number of lines the scrollback buffer can hold. */
    @Override
    public int getScrollBackMaxSize() {
        return scrollback.getMaxSize();
    }

    /** Returns the full scrollback content as a string, with rows separated by newlines. */
    @Override
    public String getScrollbackContent() {
        return this.scrollback.getContentAsString();
    }

    /**
     * Returns a single scrollback row as a string.
     *
     * @param row zero-based row index into the scrollback buffer
     * @throws OutOfBoundsException if the row index is outside the scrollback
     */
    @Override
    public String getLineFromScrollback(int row) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        return this.scrollback.getLineAsString(row);
    }

    /**
     * Returns the character at the given scrollback position.
     *
     * @param row zero-based row index into the scrollback buffer
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the scrollback
     */
    @Override
    public char getCharacterFromScrollbackAt(int row, int col) throws OutOfBoundsException {
        validator.validateScrollbackRow(row);
        validator.validateScrollbackCol(col);

        return this.scrollback.getCharacterAt(row, col);
    }

    /**
     * Returns the cell attributes at the given scrollback position.
     *
     * @param row zero-based row index into the scrollback buffer
     * @param col zero-based column index
     * @throws RuntimeException if the position is outside the scrollback
     */
    @Override
    public ICellAttributes getAttributesFromScrollbackAt(int row, int col) throws RuntimeException {
        validator.validateScrollbackRow(row); // TODO: fix validation for scrollback
        validator.validateScrollbackCol(col);
        return this.scrollback.getAttributesAt(row, col);
    }

    // STYLE OPERATIONS

    /**
     * Sets the foreground color used for subsequently written cells.
     *
     * @param foregroundColor the color to apply
     */
    @Override
    public void setForegroundColor(Color foregroundColor) {
        this.cellAttributes.setForegroundColor(foregroundColor);
    }

    /**
     * Sets the background color used for subsequently written cells.
     *
     * @param backgroundColor the color to apply
     */
    @Override
    public void setBackgroundColor(Color backgroundColor) {
        this.cellAttributes.setBackgroundColor(backgroundColor);
    }

    /**
     * Enables or disables bold for subsequently written cells.
     *
     * @param isBold true to enable bold
     */
    @Override
    public void setBold(boolean isBold) {
        this.cellAttributes.setBold(isBold);
    }

    /**
     * Enables or disables italic for subsequently written cells.
     *
     * @param isItalic true to enable italic
     */
    @Override
    public void setItalic(boolean isItalic) {
        this.cellAttributes.setItalic(isItalic);
    }

    /**
     * Enables or disables underline for subsequently written cells.
     *
     * @param isUnderline true to enable underline
     */
    @Override
    public void setUnderline(boolean isUnderline) {
        this.cellAttributes.setUnderline(isUnderline);
    }

    // CURSOR OPERATIONS

    /**
     * Advances the cursor to the next cell position.
     * If the cursor is at the last column, it wraps to the beginning of the next row.
     * If the cursor is at the last column of the last row, a scroll is triggered
     * and the cursor moves to the beginning of the new bottom row.
     */
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

    /** Returns the current cursor row (zero-based). */
    @Override
    public int getCursorRow() {
        return this.cursorManager.getCursorRow();
    }

    /** Returns the current cursor column (zero-based). */
    @Override
    public int getCursorColumn() {
        return this.cursorManager.getCursorColumn();
    }

    /**
     * Moves the cursor down by the given number of rows.
     *
     * @param offset number of rows to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    @Override
    public void moveCursorDown(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorDown(offset);
    }

    /**
     * Moves the cursor up by the given number of rows.
     *
     * @param offset number of rows to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    @Override
    public void moveCursorUp(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorUp(offset);
    }

    /**
     * Moves the cursor left by the given number of columns.
     *
     * @param offset number of columns to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    @Override
    public void moveCursorLeft(int offset) throws OutOfBoundsException, OffsetValueException {
        this.cursorManager.moveCursorLeft(offset);
    }

    /**
     * Moves the cursor right by the given number of columns.
     *
     * @param offset number of columns to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     */
    @Override
    public void moveCursorRight(int offset) throws OutOfBoundsException {
        this.cursorManager.moveCursorRight(offset);
    }

    /**
     * Sets the cursor column directly.
     *
     * @param col zero-based column index
     * @throws OutOfBoundsException if the column is outside the screen
     */
    @Override
    public void setCursorColumn(int col) throws OutOfBoundsException {
        this.cursorManager.setCursorColumn(col);
    }

    /**
     * Sets the cursor row directly.
     *
     * @param row zero-based row index
     * @throws OutOfBoundsException if the row is outside the screen
     */
    @Override
    public void setCursorRow(int row) throws OutOfBoundsException {
        this.cursorManager.setCursorRow(row);
    }

    /**
     * Sets the cursor position directly.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws RuntimeException if the position is outside the screen
     */
    @Override
    public void setCursorPosition(int row, int col) throws RuntimeException {
        this.cursorManager.setCursorPosition(row, col);
    }

    /** Clears both the screen and the scrollback history. */
    @Override
    public void clearScreenAndScrollback() {
        clearScreen();
        this.scrollback.clear();
    }

    /**
     * Returns the combined screen and scrollback content as a single string,
     * with the screen content first, followed by a newline, then the scrollback content.
     */
    @Override
    public String getScreenAndScrollbackContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getScreenContent()).append('\n').append(this.getScrollbackContent());

        return sb.toString();
    }



}
