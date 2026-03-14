package com.example;

public class Cell implements ICell {
    char character = ' ';

    @Override
    public char getCharacter() {
        return character;
    }

    @Override
    public void setCharacter(char character) {
        this.character = character;
    }

    @Override
    public boolean isEmpty() {
        return character == ' ';
    }
    
}
