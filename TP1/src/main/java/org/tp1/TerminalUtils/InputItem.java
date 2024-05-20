package org.tp1.TerminalUtils;

public final class InputItem {
    public final String label;
    public String value;
    public final boolean isCheckbox;
    public final String[] checkboxOptions;

    public InputItem(String label, String value) {
        this.label = label;
        this.value = value;
        this.isCheckbox = false;
        this.checkboxOptions = null;
    }

    public InputItem(String label, String value, String[] checkboxOptions) {
        this.label = label;
        this.value = value;
        this.isCheckbox = true;
        this.checkboxOptions = checkboxOptions;
    }
}
