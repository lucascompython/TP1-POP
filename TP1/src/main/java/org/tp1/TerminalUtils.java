package org.tp1;

import static org.tp1.bindings.bindings_h.*;
import org.tp1.bindings.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

public final class TerminalUtils implements AutoCloseable {

    public TerminalUtils() {
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

    public TerminalSize getTerminalSize() {
        try (var arena = Arena.ofConfined()) {
            MemorySegment terminalSize = TermSize.reinterpret(get_terminal_size(), arena,
                    bindings_h::free_terminal_size);

            return new TerminalSize(
                    TermSize.rows(terminalSize),
                    TermSize.cols(terminalSize));
        }
    }

    public void print(String str) {
        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(str);
            write_text(strSegment, strSegment.byteSize());
        }
    }

    public void print_centered(String str) {
        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(str);
            write_centered_text(strSegment, strSegment.byteSize());
        }
    }

    @Override
    public void close() {
        disable_raw_mode();
        leave_alternate_screen();
        showCursor();
    }
}
