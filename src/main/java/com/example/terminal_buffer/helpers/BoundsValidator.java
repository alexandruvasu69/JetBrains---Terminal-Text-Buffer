package com.example.terminal_buffer.helpers;

import com.example.exceptions.OffsetValueException;
import com.example.exceptions.OutOfBoundsException;
import com.example.screen.IScreen;
import com.example.scrollback.IScrollback;

public class BoundsValidator {
    private final int screenWidth;
    private final int screenHeight;
    private final int scrollbackMaxSize;
    private final int scrollbackWidth;

    public BoundsValidator(IScreen screen, IScrollback scrollback) {
        this.screenWidth = screen.getWidth();
        this.screenHeight = screen.getHeight();
        this.scrollbackMaxSize = scrollback.getMaxSize();
        this.scrollbackWidth = scrollback.getWidth();
    }

    public void validateOffset(int offset) {
        if (offset < 0) throw new OffsetValueException();
    }

    public void validateScreenRow(int row) {
        if (row >= screenHeight || row < 0) throw new OutOfBoundsException();
    }

    public void validateScreenCol(int col) {
        if (col >= screenWidth || col < 0)
            throw new OutOfBoundsException();
    }
    
    public void validateScrollbackRow(int row) {
        if (row >= scrollbackMaxSize || row < 0) throw new OutOfBoundsException();
    }

    public void validateScrollbackCol(int col) {
        if (col >= scrollbackWidth || col < 0) throw new OutOfBoundsException();
    }

    public boolean isScreenPositionOutOfBounds(int row, int col) {
        try {
            validateScreenRow(row);
            validateScreenCol(col);
        } catch (Exception e) {
            return true;
        }

        return false;
    }
}
