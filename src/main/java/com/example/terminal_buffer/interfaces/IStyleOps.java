package com.example.terminal_buffer;

import com.example.style.Color;

// style operations
public interface IStyleOps {
    void setForegroundColor(Color color); 
    void setBackgroundColor(Color color);
    void setBold(boolean isBold);
    void setItalic(boolean isItalic);
    void setUnderline(boolean isUnderline);
}
