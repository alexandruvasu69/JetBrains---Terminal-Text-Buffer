package com.example;

public enum Color {
    RED,
    GREEN,
    BLUE,
    INDIGO,
    ORANGE,
    YELLOW,
    VIOLET,
    GREY,
    MAROON,
    WHITE,
    BLACK,
    OLIVE,
    CYAN,
    PINK,
    MAGENTA,
    TAN,
    TEAL;

    public static Color getDefaultForeground() {
        return WHITE;
    }

    public static Color getDefaultBackground() {
        return BLACK;
    }
}
