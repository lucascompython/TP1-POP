package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.SearchItem;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Worker;

import java.util.List;

public final class ListMenus {
    private final Terminal terminal;
    private final MainMenu mainMenuInstance;
    private final List<Worker> workers;
    private final List<Client> clients;

    ListMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
    }

    void mainMenu() {
        var listOptions = new String[] { "Listar Trabalhador", "Listar Cliente", "Listar Arranjo", "Voltar" };

        var listOption = terminal.arrowMenu(listOptions);

        switch (listOption) {
            case 0 -> listWorker();


            case 1 -> terminal.printCenteredAndWait("Listar Cliente", Color.GREEN, Style.BOLD);

            case 2 -> terminal.printCenteredAndWait("Listar Arranjo", Color.GREEN, Style.BOLD);

            case 3 -> mainMenuInstance.mainMenu();
        }
    }


    private void listWorker() {
        SearchItem[] searchItems = new SearchItem[workers.size()];
        for (int i = 0; i < workers.size(); i++) {
            searchItems[i] = new SearchItem(i, workers.get(i).getName());
        }

        var workerIndex = terminal.searchByIdOrNameMenu(searchItems);

        terminal.printCenteredAndWait("ola: " + workerIndex, Color.GREEN, Style.BOLD);


    }
}
