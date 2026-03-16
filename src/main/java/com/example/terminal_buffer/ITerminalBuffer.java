package com.example.terminal_buffer;

import com.example.terminal_buffer.interfaces.ICursorOps;
import com.example.terminal_buffer.interfaces.IScreenOps;
import com.example.terminal_buffer.interfaces.IScrollbackOps;
import com.example.terminal_buffer.interfaces.IStyleOps;

public interface ITerminalBuffer extends IScreenOps, IScrollbackOps, ICursorOps, IStyleOps {
    /** Clears both the screen and the scrollback history. */
    void clearScreenAndScrollback();
    
    /**
     * Returns the combined screen and scrollback content as a single string,
     * with the screen content first, followed by a newline, then the scrollback content.
     */
    String getScreenAndScrollbackContent();
}