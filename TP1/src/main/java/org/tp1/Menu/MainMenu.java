package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Worker;

import java.util.List;

public final class MainMenu {
    private final Terminal terminal;
    private final RegisterMenus registerMenus;

    public MainMenu(List<Worker> workers, List<Client> clients) {

        try (var terminal = new Terminal()) {
            this.terminal = terminal;
            this.registerMenus = new RegisterMenus(this, terminal, workers, clients);
            terminal.printCenteredAndWait("Bem vindo ao sistema de gestão da oficina!", Color.GREEN, Style.BOLD);
            mainMenu();
        }
    }

    void mainMenu() {
        var options = new String[] { "Registar", "Modificar", "Listar", "Estatisticas", "Sair" };

        var option = terminal.arrowMenu(options);

        switch (option) {
            case 0 -> registerMenus.mainMenu();
            case 1 -> terminal.printCenteredAndWait("Gestão de Mecânicos", Color.GREEN, Style.BOLD);
            case 2 -> terminal.printCenteredAndWait("Gestão de Reparações", Color.GREEN, Style.BOLD);
            case 3 -> terminal.printCenteredAndWait("Gestão de Veículos", Color.GREEN, Style.BOLD);
            case 4 -> {
            } // exit

        }

    }


}
