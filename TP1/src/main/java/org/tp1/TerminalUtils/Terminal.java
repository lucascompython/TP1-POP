package org.tp1.TerminalUtils;

import static org.tp1.bindings.bindings_h.*;
import org.tp1.bindings.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public final class Terminal implements AutoCloseable {

    public Terminal() {
        enable_raw_mode();
        enter_alternate_screen();
        hideCursor();
        clear();
        moveCursor((short) 0, (short) 0);
    }

    public void clear() {
        clear_screen();
    }

    public void moveCursor(short x, short y) {
        move_cursor(x, y);
    }

    public void hideCursor() {
        hide_cursor();
    }

    public void showCursor() {
        show_cursor();
    }

    public byte readKey() {
        return read_key();
    }

    public void print(String str) {
        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(str);
            write_text(strSegment, strSegment.byteSize());
        }
    }

    public void print_centered(String str, Color color, Style style) {
        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(str);
            write_centered_text(strSegment, strSegment.byteSize(), (byte)color.ordinal(), (byte)style.ordinal());
        }
    }

    @Override
    public void close() {
        disable_raw_mode();
        leave_alternate_screen();
        showCursor();
    }
}
