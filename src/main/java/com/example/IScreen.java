package com.example;

public interface IScreen {
    void fillLine(char character, int line);

    int getWidth();
    int getHeight();

    String getScreenString();
}
