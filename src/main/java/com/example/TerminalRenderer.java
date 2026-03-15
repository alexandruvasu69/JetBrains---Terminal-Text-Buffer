package com.example;

public class TerminalRenderer {
    private final StringBuilder sb;

    public TerminalRenderer() {
        this.sb = new StringBuilder();
    }

    public TerminalRenderer appendLine(ICell[] line) {
        for (ICell cell : line) {
            sb.append(cell.getCharacter());
        }
        sb.append('\n');

        return this;
    }

    public TerminalRenderer removeLastChar() {
        sb.setLength(sb.length() - 1);

        return this;
    }

    public String build() {
        return sb.toString();
    }

}
