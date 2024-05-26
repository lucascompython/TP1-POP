package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Repair;
import org.tp1.Workshop.Worker;

import java.util.List;

public final class MainMenu {
    private final Terminal terminal;
    private final RegisterMenus registerMenus;
    private final ListMenus listMenus;
    private final ModifyMenus modifyMenus;

    public MainMenu(List<Worker> workers, List<Client> clients, List<Repair> repairs) {

        try (var terminal = new Terminal()) {
            this.terminal = terminal;

            this.registerMenus = new RegisterMenus(this, terminal, workers, clients, repairs);
            this.listMenus = new ListMenus(this, terminal, workers, clients, repairs);
            this.modifyMenus = new ModifyMenus(this, terminal, workers, clients, repairs);

            terminal.printCenteredAndWait("Bem vindo ao sistema de gestão da oficina!", Color.GREEN, Style.BOLD);
            mainMenu();
        }
    }

    void mainMenu() {
        var options = new String[] { "Registar", "Modificar", "Listar", "Estatisticas", "Sair" };

        var option = terminal.arrowMenu(options);

        switch (option) {
            case 0 -> registerMenus.mainMenu();
            case 1 -> modifyMenus.mainMenu();
            case 2 -> listMenus.mainMenu();
            case 3 -> terminal.printCenteredAndWait("Gestão de Veículos", Color.GREEN, Style.BOLD);
            case 4 -> {
                terminal.close();
                System.exit(0);
            } // exit

        }

    }

}
