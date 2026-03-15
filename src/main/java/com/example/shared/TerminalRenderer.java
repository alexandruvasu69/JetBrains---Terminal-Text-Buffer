package com.example.shared;

import com.example.cell.ICell;

public class TerminalRenderer {
    private final StringBuilder sb;

    public TerminalRenderer() {
        this.sb = new StringBuilder();
    }

    public TerminalRenderer appendLine(ICell[] line) {
        for (ICell cell : line) {
            sb.append(cell.getCharacter());
        }

        return this;
    }

    public TerminalRenderer appendNewLine() {
        sb.append('\n');

        return this;
    }

    public TerminalRenderer removeLastChar() {
        if (sb.length() >= 1) {
            sb.setLength(sb.length() - 1);
        }

        return this;
    }

    public String build() {
        return sb.toString();
    }

}
