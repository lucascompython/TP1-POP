package org.tp1.TerminalUtils;

public final class InputItem {
    public final String label;
    public String value;
    public final boolean isCheckbox;
    public final boolean isSearchInput;
    public final String[] checkboxOptions;
    public final SearchItem[] searchItems;

    public InputItem(String label, String value) {
        this.label = label;
        this.value = value;
        this.isCheckbox = false;
        this.checkboxOptions = null;
        this.isSearchInput = false;
        this.searchItems = null;
    }

    public InputItem(String label, String value, String[] checkboxOptions) {
        this.label = label;
        this.value = value;
        this.isCheckbox = true;
        this.isSearchInput = false;
        this.searchItems = null;
        this.checkboxOptions = checkboxOptions;
    }

    public InputItem(String label, String value, SearchItem[] searchItems) {
        this.label = label;
        this.value = value;
        this.isCheckbox = false;
        this.checkboxOptions = null;
        this.isSearchInput = true;
        this.searchItems = searchItems;
    }
}
