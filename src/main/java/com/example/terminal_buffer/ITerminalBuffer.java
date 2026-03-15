package com.example.terminal_buffer;

import com.example.terminal_buffer.interfaces.ICursorOps;
import com.example.terminal_buffer.interfaces.IScreenOps;
import com.example.terminal_buffer.interfaces.IScrollbackOps;
import com.example.terminal_buffer.interfaces.IStyleOps;

public interface ITerminalBuffer extends IScreenOps, IScrollbackOps, ICursorOps, IStyleOps {
    void clearScreenAndScrollback();
    String getScreenAndScrollbackContent();
}