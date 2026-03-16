package com.example.terminal_buffer.interfaces;

import com.example.cell.ICellAttributes;

// screen operations
public interface IScreenOps {
    int getScreenWidth();
    int getScreenHeight();

    // editing
     /**
     * Writes text starting at the current cursor position, overwriting existing cells.
     * The cursor advances after each character. If the cursor reaches the end of the
     * screen, a scroll is triggered and writing continues on the new bottom row.
     * If there is no bottom row, a new empty row is inserted and the write starts
     * again from the first column of that row.
     *
     * @param text the text to write
     * @throws IllegalArgumentException if input text is empty or null
     */
    void writeText(String text);

    /**
     * Inserts text at the current cursor position, shifting existing cells to the right.
     * Characters pushed off the right edge of the row are collected as overflow,
     * reversed into correct left-to-right order, and recursively flushed to subsequent rows.
     *
     * @param text the text to insert
     * @throws IllegalArgumentException if input text is empty or null
     */
    void insertText(String text);

    /**
     * Fills the entire row at the current cursor position with the given character,
     * using the current cell attributes.
     *
     * @param character the character to fill the row with
     * @throws IllegalArgumentException if input contains special whitespace characters('\n','\t','\r')
     */
    void fillLine(char character);

    /** Resets all cells in the screen to their default (empty) state. */
    void clearScreen();

    /**
     * Scrolls the screen up by one line: the oldest (top) row is removed from
     * the screen and, if it contains any non-empty cells, appended to the
     * scrollback history. A new empty row is added at the bottom.
     */
    void insertEmptyLine();

    // content access

    /** Returns the full screen content as a string, with rows separated by newlines. */
    String getScreenContent();

    /**
     * Returns a single screen row as a string.
     *
     * @param row zero-based row index
     * @throws OutOfBoundsException if the row index is outside the screen
     */
    String getLineFromScreen(int row) throws RuntimeException;
    
    /**
     * Returns the character at the given screen position.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the screen
     */
    char getCharacterFromScreenAt(int row, int col) throws RuntimeException;

    /**
     * Returns the cell attributes at the given screen position.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the screen
     */
    ICellAttributes getAttributesFromScreenAt(int row, int col) throws RuntimeException;
}
