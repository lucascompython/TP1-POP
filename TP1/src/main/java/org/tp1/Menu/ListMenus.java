package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.SearchItem;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Repair;
import org.tp1.Workshop.Worker;

import java.util.List;
import java.util.Objects;

public final class ListMenus {
    private final Terminal terminal;
    private final MainMenu mainMenuInstance;
    private final List<Worker> workers;
    private final List<Client> clients;
    private final List<Repair> repairs;

    ListMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients,
            List<Repair> repairs) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
        this.repairs = repairs;
    }

    void mainMenu() {
        var listOptions = new String[] { "Listar Trabalhador", "Listar Cliente", "Listar Arranjo", "Voltar" };

        var listOption = terminal.arrowMenu(listOptions);

        switch (listOption) {
            case 0 -> listWorker();

            case 1 -> listClient();

            case 2 -> listRepair();

            case 3 -> mainMenuInstance.mainMenu();
        }
    }

    private void listRepair() {
        var repairsSize = repairs.size();

        if (repairsSize == 0) {
            terminal.printCenteredAndWait("Não existem arranjos registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] searchItems = new SearchItem[repairsSize];

        for (int i = 0; i < repairsSize; i++) {
            var repair = repairs.get(i);
            searchItems[i] = new SearchItem(repair.getClientId(), repair.getCarRegistration().registration());
        }

        var repairIndex = terminal.searchByIdOrNameMenu(searchItems);

        var repair = repairs.get(repairIndex);

        String infoString = "Matrícula: " +
                repair.getCarRegistration().registration() +
                "\nID: " +
                repair.getId() +
                "\nTipo de Veículo: " +
                repair.getVehicleType().toString() +
                "\nMecânico: " +
                Objects.requireNonNull(workers.stream().filter(worker -> worker.getId() == repair.getWorkerId())
                        .findFirst().orElse(null)).getName()
                +
                " (" +
                repair.getWorkerId() +
                ")" +
                "\nCliente: " +
                Objects.requireNonNull(clients.stream().filter(client -> client.getId() == repair.getClientId())
                        .findFirst().orElse(null)).getName()
                +
                " (" +
                repair.getClientId() +
                ")" +
                "\nDescrição: " +
                repair.getDescription() +
                "\nPreço: " +
                repair.getPrice() +
                "\nData de Início: " +
                repair.getEntryDate().format(mainMenuInstance.dateFormatter) +
                "\nData de Fim: " +
                repair.getExitDate().format(mainMenuInstance.dateFormatter);

        terminal.printCenteredLinesAndWait(infoString);

        mainMenuInstance.mainMenu();

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
                client.getContact().getEmail() +
                "\nTelefone: " +
                client.getContact().getPhone();

        terminal.printCenteredLinesAndWait(infoString);

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
                "\nIdade: " +
                worker.getAge() +
                "\nID: " +
                worker.getId() +
                "\nCargo: " +
                worker.getRole().toString() +
                "\nSalário: " +
                worker.getSalary() +
                "\nEmail: " +
                worker.getContact().getEmail() +
                "\nTelefone: " +
                worker.getContact().getPhone();

        terminal.printCenteredLinesAndWait(infoString);

        mainMenuInstance.mainMenu();

    }
}
