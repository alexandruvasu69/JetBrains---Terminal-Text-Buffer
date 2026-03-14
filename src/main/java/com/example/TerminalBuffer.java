package com.example;

public class TerminalBuffer {
    private int width;
    private int height;
    private int scrollBackMaxSize; 

    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        this.width = width;
        this.height = height;
        this.scrollBackMaxSize = scrollBackMaxSize;
        this.foregroundColor = TerminalColor.BLACK;
        this.backgroundColor = TerminalColor.BLACK;
        this.cursor = new Cursor(width, height);
    }

    public TerminalBuffer(int width, int height, int scrollBackMaxSize, ICursor cursor) {
        this(width, height, scrollBackMaxSize);
        this.cursor = cursor;
    }

    private TerminalColor foregroundColor;
    private TerminalColor backgroundColor;
    private TextStyle textStyle;
    private ICursor cursor;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScrollBackMaxSize() {
        return scrollBackMaxSize;
    }

    public void setForegroundColor(TerminalColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundColor(TerminalColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    


}
