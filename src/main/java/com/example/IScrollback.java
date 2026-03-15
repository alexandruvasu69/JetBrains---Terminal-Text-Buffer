package com.example;

public interface IScrollback {
    void addLine(ICell[] line);
    int getMaxSize();
    String getContent();
    void clear();
}
