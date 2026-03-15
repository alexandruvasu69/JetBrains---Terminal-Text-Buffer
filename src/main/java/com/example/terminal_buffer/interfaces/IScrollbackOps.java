package com.example.terminal_buffer.interfaces;

import com.example.cell.ICellAttributes;

// scrollback operations
public interface IScrollbackOps {
    int getScrollBackMaxSize();

    // content access
    String getScrollbackContent(); // TO-DO: make this screen + scrollback content
    String getLineFromScrollback(int row) throws RuntimeException;
    char getCharacterFromScrollbackAt(int row, int col) throws RuntimeException;
    ICellAttributes getAttributesFromScrollbackAt(int row, int col) throws RuntimeException;
}
