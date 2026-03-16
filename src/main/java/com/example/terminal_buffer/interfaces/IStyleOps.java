package com.example.terminal_buffer.interfaces;

import com.example.style.Color;

// style operations
public interface IStyleOps {
    /**
     * Sets the foreground color used for subsequently written cells.
     *
     * @param foregroundColor the color to apply
     */
    void setForegroundColor(Color color); 

    /**
     * Sets the background color used for subsequently written cells.
     *
     * @param backgroundColor the color to apply
     */
    void setBackgroundColor(Color color);

    /**
     * Enables or disables bold for subsequently written cells.
     *
     * @param isBold true to enable bold
     */
    void setBold(boolean isBold);

    /**
     * Enables or disables italic for subsequently written cells.
     *
     * @param isItalic true to enable italic
     */
    void setItalic(boolean isItalic);
    
    /**
     * Enables or disables underline for subsequently written cells.
     *
     * @param isUnderline true to enable underline
     */
    void setUnderline(boolean isUnderline);
}
