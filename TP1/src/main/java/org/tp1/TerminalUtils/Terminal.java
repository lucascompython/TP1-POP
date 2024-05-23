package org.tp1.TerminalUtils;

import static org.tp1.bindings.bindings_h.*;
import org.tp1.bindings.*;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public final class Terminal implements AutoCloseable {

    public Terminal() {
        init();
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
        try (var arena = Arena.ofConfined()) {
                var strSegment = arena.allocateFrom(str);
                print_centered_and_wait(strSegment, (byte) color.ordinal(), (byte) style.ordinal());
        }
    }

    public boolean inputMenu(InputItem[] inputs) {
        try (var arena = Arena.ofConfined()) {
            var inputsSegment = Input.allocateArray(inputs.length, arena);

            for (int i = 0; i < inputs.length; i++) {
                MemorySegment input = Input.asSlice(inputsSegment, i);

                var labelSegment = arena.allocateFrom(inputs[i].label);
                Input.label(input, labelSegment);

                var isCheckbox = inputs[i].isCheckbox;
                Input.is_checkbox(input, isCheckbox);

                MemorySegment valueSegment;
                if (isCheckbox) {
                    valueSegment = arena.allocate(2); // 1 byte for the value and 1 byte for the null terminator

                    var checkboxOptions = inputs[i].checkboxOptions;
                    var sb = new StringBuilder();

                    for (String option : checkboxOptions) {
                        sb.append(option);
                        sb.append("\n");
                    }

                    var checkboxOptionsSegment = arena.allocateFrom(sb.toString());
                    Input.checkbox_options(input, checkboxOptionsSegment);
                } else {
                    valueSegment = arena.allocate(40);
                }

                var value = inputs[i].value;
                byte[] valueBytes = value.getBytes();
                int length = valueBytes.length;
                MemorySegment.copy(valueBytes, 0, valueSegment, ValueLayout.JAVA_BYTE, 0, length);
                Input.value(input, valueSegment);

            }
            var result = input_menu(inputsSegment, (byte) inputs.length);

            // get the values back
            for (int i = 0; i < inputs.length; i++) {
                MemorySegment input = Input.asSlice(inputsSegment, i);
                var value = Input.value(input);
                inputs[i].value = value.getString(0);
            }

            return result;
        }
    }

    public byte fuzzyMenu(String[] options) {
        StringBuilder sb = new StringBuilder();
        for (String option : options) {
            sb.append(option);
            sb.append("\n");
        }

        try (var arena = Arena.ofConfined()) {
            var strSegment = arena.allocateFrom(sb.toString());
            return fuzzy_search_menu(strSegment);
        }
    }

    @Override
    public void close() {
        deinit();
    }
}
