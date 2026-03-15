package com.example.cell;

import com.example.style.Color;

public interface ICellAttributes {
    Color getForegroundColor();
    Color getBackgroundColor();
    boolean isBold();
    boolean isItalic();
    boolean isUnderline();
}
