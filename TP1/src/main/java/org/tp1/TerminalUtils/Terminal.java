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
            return arrow_menu(arena.allocateFrom(sb.toString()));
        }
    }

    public void printCenteredAndWait(String str, Color color, Style style) {
        try (var arena = Arena.ofConfined()) {
            print_centered_and_wait(arena.allocateFrom(str), (byte) color.ordinal(), (byte) style.ordinal());
        }
    }

    public void printCenteredLinesAndWait(String str, Color color, Style style) {
        try (var arena = Arena.ofConfined()) {
            print_centered_lines_and_wait(arena.allocateFrom(str));
        }
    }

    public boolean inputMenu(InputItem[] inputs) {
        try (var arena = Arena.ofConfined()) {
            var inputsSegment = Input.allocateArray(inputs.length, arena);

            for (int i = 0; i < inputs.length; i++) {
                MemorySegment input = Input.asSlice(inputsSegment, i);

                Input.label(input, arena.allocateFrom(inputs[i].label));

                Input.is_checkbox(input, inputs[i].isCheckbox);

                MemorySegment valueSegment;
                if (inputs[i].isCheckbox) {
                    valueSegment = arena.allocate(2); // 1 byte for the value and 1 byte for the null terminator

                    var sb = new StringBuilder();

                    for (String option : inputs[i].checkboxOptions) {
                        sb.append(option);
                        sb.append("\n");
                    }

                    var checkboxOptionsSegment = arena.allocateFrom(sb.toString());
                    Input.checkbox_options(input, checkboxOptionsSegment);
                } else {
                    valueSegment = arena.allocate(40);
                }

                byte[] valueBytes = inputs[i].value.getBytes();
                MemorySegment.copy(valueBytes, 0, valueSegment, ValueLayout.JAVA_BYTE, 0, valueBytes.length);
                Input.value(input, valueSegment);

            }
            var result = input_menu(inputsSegment, (byte) inputs.length);

            // get the values back
            for (int i = 0; i < inputs.length; i++) {
                MemorySegment input = Input.asSlice(inputsSegment, i);
                inputs[i].value = Input.value(input).getString(0);
            }

            return result;
        }
    }

    public byte searchMenu(String[] options) {
        StringBuilder sb = new StringBuilder();
        for (String option : options) {
            sb.append(option);
            sb.append("\n");
        }

        try (var arena = Arena.ofConfined()) {
            return search_menu(arena.allocateFrom(sb.toString()));
        }
    }

    public int searchByIdOrNameMenu(SearchItem[] items) {
        try (var arena = Arena.ofConfined()) {
            var itemsSegment = SearchInput.allocateArray(items.length, arena);

            for (int i = 0; i < items.length; i++) {
                MemorySegment item = SearchInput.asSlice(itemsSegment, i);

                SearchInput.id(item, items[i].id);
                SearchInput.text(item, arena.allocateFrom(items[i].name));
            }

            return search_menu_by_id_or_text(itemsSegment, (byte) items.length);
        }

    }

    @Override
    public void close() {
        deinit();
    }
}
