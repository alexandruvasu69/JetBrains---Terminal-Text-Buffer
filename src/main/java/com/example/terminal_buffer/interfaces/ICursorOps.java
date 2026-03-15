package com.example.terminal_buffer;

// cursor operations
public interface ICursorOps {
    // move
    void moveCursorUp(int offset) throws RuntimeException;
    void moveCursorDown(int offset) throws RuntimeException;
    void moveCursorLeft(int offset) throws RuntimeException;
    void moveCursorRight(int offset) throws RuntimeException;

    // set position
    void setCursorRow(int row) throws RuntimeException;
    void setCursorColumn(int col) throws RuntimeException;
    void setCursorPosition(int row, int col) throws RuntimeException;
}
