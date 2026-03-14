package com.example;

public class TerminalBuffer implements ITerminalBuffer{
    private IScreen screen;
    private ICursor cursor;
    private int scrollBackMaxSize; 
    private TerminalColor foregroundColor;
    private TerminalColor backgroundColor;
    private TextStyle textStyle;

    public TerminalBuffer(int width, int height, int scrollBackMaxSize) {
        this.screen = new Screen(width, height);
        this.scrollBackMaxSize = scrollBackMaxSize;
        this.foregroundColor = TerminalColor.BLACK;
        this.backgroundColor = TerminalColor.BLACK;
        this.cursor = new Cursor(height - 2, width - 2);
    }

    public TerminalBuffer(int width, int height, int scrollBackMaxSize, ICursor cursor) {
        this(width, height, scrollBackMaxSize);
        this.cursor = cursor;
    }


    @Override
    public int getWidth() {
        return screen.getWidth();
    }

    @Override
    public int getHeight() {
        return screen.getHeight();
    }

    @Override
    public int getScrollBackMaxSize() {
        return scrollBackMaxSize;
    }

    @Override
    public void setForegroundColor(TerminalColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public void setBackgroundColor(TerminalColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }

    @Override
    public void fillLine(char character) {
        int row = cursor.getRowPosition();
        screen.fillLine(character, row);
    }

    @Override
    public String getScreenContent() {
        return screen.getScreenString();
    }

    
}
