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

            case 1 -> listClient();

            case 2 -> terminal.printCenteredAndWait("Listar Arranjo", Color.GREEN, Style.BOLD);

            case 3 -> mainMenuInstance.mainMenu();
        }
    }

    private void listClient() {
        var clientsSize = clients.size();

        if (clientsSize == 0) {
            terminal.printCenteredAndWait("Não existem clientes registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] searchItems = new SearchItem[clientsSize];

        for (int i = 0; i < clientsSize; i++) {
            var client = clients.get(i);
            searchItems[i] = new SearchItem(client.getId(), client.getName());
        }

        var clientIndex = terminal.searchByIdOrNameMenu(searchItems);

        var client = clients.get(clientIndex);

        String infoString = "Nome: " +
                client.getName() +
                "\nID: " +
                client.getId() +
                "\nNIF: " +
                client.getNIF().to_string() +
                "\nEmail: " +
                client.getContact().email() +
                "\nTelefone: " +
                client.getContact().phone();

        terminal.printCenteredLinesAndWait(infoString, Color.GREEN, Style.BOLD);

        mainMenuInstance.mainMenu();

    }

    private void listWorker() {
        var workersSize = workers.size();

        if (workersSize == 0) {
            terminal.printCenteredAndWait("Não existem trabalhadores registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] searchItems = new SearchItem[workersSize];

        for (int i = 0; i < workersSize; i++) {
            var worker = workers.get(i);
            searchItems[i] = new SearchItem(worker.getId(), worker.getName());
        }

        var workerIndex = terminal.searchByIdOrNameMenu(searchItems);

        var worker = workers.get(workerIndex);

        String infoString = "Nome: " +
                worker.getName() +
                "\nID: " +
                worker.getId() +
                "\nCargo: " +
                worker.getRole().toString() +
                "\nSalário: " +
                worker.getSalary() +
                "\nEmail: " +
                worker.getContact().email() +
                "\nTelefone: " +
                worker.getContact().phone();

        terminal.printCenteredLinesAndWait(infoString, Color.GREEN, Style.BOLD);

        mainMenuInstance.mainMenu();

    }
}
