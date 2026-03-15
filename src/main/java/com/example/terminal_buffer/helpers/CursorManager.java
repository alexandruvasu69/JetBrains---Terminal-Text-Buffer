package com.example.terminal_buffer.helpers;

import com.example.cursor.ICursor;
import com.example.exceptions.OutOfBoundsException;
import com.example.screen.IScreen;
import com.example.terminal_buffer.interfaces.ICursorOps;

public class CursorManager implements ICursorOps {
    private final ICursor cursor;
    private final IScreen screen;
    private final BoundsValidator validator;

    public CursorManager(ICursor cursor, IScreen screen, BoundsValidator validator) {
        this.cursor = cursor;
        this.screen = screen;
        this.validator = validator;
    }

    @Override
    public void moveCursorDown(int offset) throws RuntimeException {
        validator.validateOffset(offset);
        int newRow = cursor.getRowPosition() + offset;

        validator.validateScreenRow(newRow);

        cursor.setRowPosition(newRow);
    }

    @Override
    public void moveCursorLeft(int offset) throws RuntimeException {
        validator.validateOffset(offset);
        int newCol = cursor.getColumnPosition() - offset;

        validator.validateScreenCol(newCol);

        cursor.setColumnPosition(newCol);
    }

    @Override
    public void moveCursorRight(int offset) throws RuntimeException {
        validator.validateOffset(offset);
        int newCol = cursor.getColumnPosition() + offset;

        validator.validateScreenCol(newCol);

        cursor.setColumnPosition(newCol);
    }

    @Override
    public void moveCursorUp(int offset) throws RuntimeException {
        validator.validateOffset(offset);
        int newRow = cursor.getRowPosition() - offset;

        validator.validateScreenRow(newRow);

        cursor.setRowPosition(newRow);
    }

    @Override
    public void setCursorColumn(int col) throws RuntimeException {
        validator.validateScreenCol(col);
        cursor.setColumnPosition(col);
    }

    @Override
    public void setCursorPosition(int row, int col) throws RuntimeException {
        setCursorRow(row);
        setCursorColumn(col);
    }

    @Override
    public void setCursorRow(int row) throws RuntimeException {
        validator.validateScreenRow(row);
        cursor.setRowPosition(row);
    }
    
    public void moveCursorNext() {
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
}
