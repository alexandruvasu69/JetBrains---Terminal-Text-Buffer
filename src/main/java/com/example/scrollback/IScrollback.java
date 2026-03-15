package com.example.scrollback;

import com.example.cell.ICell;
import com.example.shared.IContentAccess;

public interface IScrollback extends IContentAccess {
    int getWidth();
    int getMaxSize();

    void addLine(ICell[] line);
    void clear();
}
