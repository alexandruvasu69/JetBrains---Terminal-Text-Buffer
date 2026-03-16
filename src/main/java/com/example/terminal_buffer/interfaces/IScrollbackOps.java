package com.example.terminal_buffer.interfaces;

import com.example.cell.ICellAttributes;

// scrollback operations
public interface IScrollbackOps {
    /** Returns the maximum number of lines the scrollback buffer can hold. */
    int getScrollBackMaxSize();

    // content access

    /** Returns the full scrollback content as a string, with rows separated by newlines. */
    String getScrollbackContent();

    /**
     * Returns a single scrollback row as a string.
     *
     * @param row zero-based row index into the scrollback buffer
     * @throws OutOfBoundsException if the row index is outside the scrollback
     */
    String getLineFromScrollback(int row) throws RuntimeException;

    /**
     * Returns the character at the given scrollback position.
     *
     * @param row zero-based row index into the scrollback buffer
     * @param col zero-based column index
     * @throws OutOfBoundsException if the position is outside the scrollback
     */
    char getCharacterFromScrollbackAt(int row, int col) throws RuntimeException;
    
    /**
     * Returns the cell attributes at the given scrollback position.
     *
     * @param row zero-based row index into the scrollback buffer
     * @param col zero-based column index
     * @throws RuntimeException if the position is outside the scrollback
     */
    ICellAttributes getAttributesFromScrollbackAt(int row, int col) throws RuntimeException;
}
