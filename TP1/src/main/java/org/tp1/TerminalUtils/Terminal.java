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
            write_text(strSegment);
        }
    }

    public void printCentered(String str, Color color, Style style, int row_offset) {
        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(str);
            write_centered_text(strSegment, (byte) color.ordinal(), (byte) style.ordinal(), (short) row_offset);
        }
    }

    public byte arrowMenu(String[] options) {
        StringBuilder sb = new StringBuilder();
        for (String option : options) {
            sb.append(option);
            sb.append("\n");
        }

        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(sb.toString());
            return arrow_menu(strSegment);
        }
    }

    public void printCenteredAndWait(String str, Color color, Style style) {
        printCentered(str, color, style, -1);
        printCentered("Pressione qualquer tecla para continuar", Color.NULL, Style.UNDERLINE, 1);
        readKey();
    }

    public boolean inputMenu(InputItem[] inputs) {
        try (var arena = Arena.ofConfined()) {
            var inputsSegment = Input.allocateArray(inputs.length, arena);

            for (int i = 0; i < inputs.length; i++) {
                MemorySegment input = Input.asSlice(inputsSegment, i);

                var labelSegment = arena.allocateFrom(inputs[i].label);
                Input.label(input, labelSegment);

                var valueSegment = arena.allocateFrom(inputs[i].value);
                Input.value(input, valueSegment);

                var isCheckbox = inputs[i].isCheckbox;
                Input.is_checkbox(input, isCheckbox);

                if (isCheckbox) {
                    var checkboxOptions = inputs[i].checkboxOptions;
                    var sb = new StringBuilder();

                    for (String option : checkboxOptions) {
                        sb.append(option);
                        sb.append("\n");
                    }

                    var checkboxOptionsSegment = arena.allocateFrom(sb.toString());
                    Input.checkbox_options(input, checkboxOptionsSegment);
                }

            }
            return input_menu(inputsSegment, (byte)inputs.length);
        }
    }

    @Override
    public void close() {
        disable_raw_mode();
        leave_alternate_screen();
        showCursor();
    }
}
