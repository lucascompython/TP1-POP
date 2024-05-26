package org.tp1.Menu;

import org.tp1.TerminalUtils.*;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Repair;
import org.tp1.Workshop.Worker;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class StatisticsMenus {
    private final Terminal terminal;
    private final List<Worker> workers;
    private final List<Client> clients;
    private final List<Repair> repairs;
    private final MainMenu mainMenuInstance;

    StatisticsMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients,
            List<Repair> repairs) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
        this.repairs = repairs;
    }

    void mainMenu() {
        var options = new String[] { "Arranjos por Cliente", "Arranjos entre Datas", "Preço Total Arranjos",
                "Preço Arranjos por Cliente", "Extremos Preços Arranjos", "Calcular Impostos", "Voltar" };

        var option = terminal.arrowMenu(options);

        switch (option) {
            case 0 -> repairsByClient();
            case 1 -> repairsBetweenDates();
            // case 2 -> totalRepairsPrice();
            // case 3 -> repairsPriceByClient();
            // case 4 -> extremeRepairsPrice();
            // case 5 -> calculateTaxes();
            case 6 -> mainMenuInstance.mainMenu();
        }
    }

    private void repairsBetweenDates() {
        var repairsSize = repairs.size();

        if (repairsSize == 0) {
            terminal.printCenteredAndWait("Não existem arranjos registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        InputItem[] inputItems = new InputItem[] {
                new InputItem("Data de Início (dd-MM-yyyy)", ""),
                new InputItem("Data de Fim (dd-MM-yyyy)", "")
        };

        var result = terminal.inputMenu(inputItems);

        if (!result) {
            mainMenuInstance.mainMenu();
            return;
        }

        var startDate = LocalDate.parse(inputItems[0].value, mainMenuInstance.formatter);
        var endDate = LocalDate.parse(inputItems[1].value, mainMenuInstance.formatter);

        var repairsBetweenDates = repairs.stream()
                .filter(repair -> repair.getEntryDate().isAfter(startDate) && repair.getExitDate().isBefore(endDate))
                .toList();
        var repairsBetweenDatesSize = repairsBetweenDates.size();

        if (repairsBetweenDates.isEmpty()) {
            terminal.printCenteredAndWait("Não existem arranjos entre as datas especificadas!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] repairsSearchItems = new SearchItem[repairsBetweenDatesSize];
        for (int i = 0; i < repairsBetweenDatesSize; i++) {
            var repair = repairsBetweenDates.get(i);
            repairsSearchItems[i] = new SearchItem(repair.getId(), repair.getCarRegistration().registration());
        }

        var repairIndex = terminal.searchByIdOrNameMenu(repairsSearchItems);

        var repair = repairsBetweenDates.get(repairIndex);

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
                "€" +
                "\nData de Início: " +
                repair.getEntryDate().toString() +
                "\nData de Fim: " +
                repair.getExitDate().toString();

        terminal.printCenteredLinesAndWait(infoString, Color.GREEN, Style.BOLD);
        mainMenuInstance.mainMenu();
    }

    private void repairsByClient() {
        var clientsSize = clients.size();

        if (clientsSize == 0) {
            terminal.printCenteredAndWait("Não existem clientes registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] clientsSearchItems = new SearchItem[clientsSize];
        for (int i = 0; i < clientsSize; i++) {
            var client = clients.get(i);
            clientsSearchItems[i] = new SearchItem(client.getId(), client.getName());
        }

        var clientIndex = terminal.searchByIdOrNameMenu(clientsSearchItems);

        var client = clients.get(clientIndex);

        var clientRepairs = repairs.stream().filter(repair -> repair.getClientId() == client.getId()).toList();

        if (clientRepairs.isEmpty()) {
            terminal.printCenteredAndWait("O cliente não tem arranjos registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] repairsSearchItems = new SearchItem[clientRepairs.size()];
        for (int i = 0; i < clientRepairs.size(); i++) {
            var repair = clientRepairs.get(i);
            repairsSearchItems[i] = new SearchItem(repair.getId(), repair.getDescription());
        }

        var repairIndex = terminal.searchByIdOrNameMenu(repairsSearchItems);

        var repair = clientRepairs.get(repairIndex);

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
                client.getName() +
                " (" +
                repair.getClientId() +
                ")" +
                "\nDescrição: " +
                repair.getDescription() +
                "\nPreço: " +
                repair.getPrice() +
                "€" +
                "\nData de Início: " +
                repair.getEntryDate().toString() +
                "\nData de Fim: " +
                repair.getExitDate().toString();

        terminal.printCenteredLinesAndWait(infoString, Color.GREEN, Style.BOLD);
        mainMenuInstance.mainMenu();
    }

}
