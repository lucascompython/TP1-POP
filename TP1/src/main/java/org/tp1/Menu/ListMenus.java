package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;

public final class ListMenus {
    private final Terminal terminal;
    private final MainMenu mainMenuInstance;

    ListMenus(MainMenu mainMenuInstance, Terminal terminal) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
    }

    void mainMenu() {
        var listOptions = new String[] { "Listar Trabalhador", "Listar Cliente", "Listar Arranjo", "Voltar" };

        var listOption = terminal.searchMenu(listOptions);

        switch (listOption) {
            case 0 -> terminal.printCenteredAndWait("Listar Trabalhador", Color.GREEN, Style.BOLD);

            case 1 -> terminal.printCenteredAndWait("Listar Cliente", Color.GREEN, Style.BOLD);

            case 2 -> terminal.printCenteredAndWait("Listar Arranjo", Color.GREEN, Style.BOLD);

            case 3 -> mainMenuInstance.mainMenu();
        }
    }


}
