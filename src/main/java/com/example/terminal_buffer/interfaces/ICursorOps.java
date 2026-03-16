package com.example.terminal_buffer.interfaces;

// cursor operations
public interface ICursorOps {
    // move

    /**
     * Moves the cursor up by the given number of rows.
     *
     * @param offset number of rows to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    void moveCursorUp(int offset) throws RuntimeException;

    /**
     * Moves the cursor down by the given number of rows.
     *
     * @param offset number of rows to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    void moveCursorDown(int offset) throws RuntimeException;

    /**
     * Moves the cursor left by the given number of columns.
     *
     * @param offset number of columns to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     * @throws OffsetValueException if the offset is invalid
     */
    void moveCursorLeft(int offset) throws RuntimeException;

    /**
     * Moves the cursor right by the given number of columns.
     *
     * @param offset number of columns to move
     * @throws OutOfBoundsException if the resulting position is outside the screen
     */
    void moveCursorRight(int offset) throws RuntimeException;

    // set position

    /**
     * Sets the cursor row directly.
     *
     * @param row zero-based row index
     * @throws OutOfBoundsException if the row is outside the screen
     */
    void setCursorRow(int row) throws RuntimeException;

    /**
     * Sets the cursor column directly.
     *
     * @param col zero-based column index
     * @throws OutOfBoundsException if the column is outside the screen
     */
    void setCursorColumn(int col) throws RuntimeException;
    
    /**
     * Sets the cursor position directly.
     *
     * @param row zero-based row index
     * @param col zero-based column index
     * @throws RuntimeException if the position is outside the screen
     */
    void setCursorPosition(int row, int col) throws RuntimeException;

    // getters
    /** Returns the current cursor row (zero-based). */
    int getCursorRow();

    /** Returns the current cursor column (zero-based). */
    int getCursorColumn();
}
