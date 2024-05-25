package org.tp1.Menu;

import org.tp1.TerminalUtils.*;
import org.tp1.Workshop.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class RegisterMenus {
    private final Terminal terminal;
    private final List<Worker> workers;
    private final List<Client> clients;
    private final List<Repair> repairs;
    private final MainMenu mainMenuInstance;

    RegisterMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients,
            List<Repair> repairs) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
        this.repairs = repairs;
    }

    void mainMenu() {
        var registerOptions = new String[] { "Registar Trabalhador", "Registar Cliente", "Registar Arranjo", "Voltar" };

        var registerOption = terminal.arrowMenu(registerOptions);

        switch (registerOption) {
            case 0 -> registerWorkerMenu();

            case 1 -> registerClient();

            case 2 -> registerRepair();

            case 3 -> mainMenuInstance.mainMenu();
        }
    }

    private void registerRepair() {
        var workerSize = workers.size();
        if (workerSize == 0) {
            terminal.printCenteredAndWait("Não existem trabalhadores registados!", Color.RED, Style.BOLD);
            mainMenu();
            return;
        }
        var clientSize = clients.size();
        if (clientSize == 0) {
            terminal.printCenteredAndWait("Não existem clientes registados!", Color.RED, Style.BOLD);
            mainMenu();
            return;
        }

        SearchItem[] workersSearchItems = new SearchItem[workerSize];
        for (int i = 0; i < workerSize; i++) {
            var worker = workers.get(i);
            workersSearchItems[i] = new SearchItem(worker.getId(), worker.getName());
        }
        SearchItem[] clientsSearchItems = new SearchItem[clientSize];
        for (int i = 0; i < clientSize; i++) {
            var client = clients.get(i);
            clientsSearchItems[i] = new SearchItem(client.getId(), client.getName());
        }

        var inputItems = new InputItem[] {
                new InputItem("Matrícula", ""),
                new InputItem("Trabalhador", "", workersSearchItems),
                new InputItem("Cliente", "", clientsSearchItems),
                new InputItem("Descrição", ""),
                new InputItem("Preço (€)", ""),
                new InputItem("Data de Início", ""),
                new InputItem("Data de Fim", ""),
                new InputItem("Tipo de Veículo", "",
                        new String[] { "Carro", "Mota", "Camião" }),
        };

        var result = terminal.inputMenu(inputItems);

        if (result) {
            // TODO: Maybe make the validation methods return a value
            ValidationMenus.validateEmpty(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validatePrice(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateCarRegistration(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateDates(inputItems, terminal, mainMenuInstance);

            // the value of the search item is the id of the worker or client
            var workerId = Integer.parseInt(inputItems[1].value.trim());
            var clientId = Integer.parseInt(inputItems[2].value.trim());

            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            var startDate = LocalDate.parse(inputItems[5].value, formatter);
            var endDate = LocalDate.parse(inputItems[6].value, formatter);

            repairs.add(
                    new Repair(
                            new CarRegistration(inputItems[0].value),
                            workerId,
                            clientId,
                            inputItems[3].value,
                            Float.parseFloat(inputItems[4].value),
                            startDate,
                            endDate,
                            VehicleType.fromString(inputItems[7].value)));

            terminal.printCenteredAndWait("Arranjo registado com sucesso!", Color.GREEN, Style.BOLD);
            mainMenu();
        } else {
            mainMenu();
        }
    }

    private void registerClient() {
        var inputItems = new InputItem[] {
                new InputItem("Nome", ""),
                new InputItem("NIF", ""),
                new InputItem("Email", ""),
                new InputItem("Telefone", "")
        };

        var result = terminal.inputMenu(inputItems);

        if (result) {
            ValidationMenus.validateEmpty(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateNIF(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateEmailAndPhone(inputItems, terminal, mainMenuInstance);

            clients.add(
                    new Client(
                            inputItems[0].value,
                            new NIF(inputItems[1].value),
                            new Contact(inputItems[2].value, Integer.parseInt(inputItems[3].value))));

            terminal.printCenteredAndWait("Cliente registado com sucesso!", Color.GREEN, Style.BOLD);
            mainMenu();
        } else {
            mainMenu();
        }
    }

    private void registerWorkerMenu() {
        var inputItems = new InputItem[] {
                new InputItem("Nome", ""),
                new InputItem("Salário (€)", ""),
                new InputItem("Email", ""),
                new InputItem("Telefone", ""),
                new InputItem("Tipo de Trabalhador", "",
                        new String[] { "Mecânico", "Gerente", "Rececionista" })
        };

        var result = terminal.inputMenu(inputItems);

        if (result) {
            ValidationMenus.validateEmpty(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateSalary(inputItems, terminal, mainMenuInstance);
            ValidationMenus.validateEmailAndPhone(inputItems, terminal, mainMenuInstance);

            workers.add(
                    new Worker(inputItems[0].value,
                            Role.fromString(inputItems[4].value),
                            Float.parseFloat(inputItems[1].value),
                            new Contact(inputItems[2].value, Integer.parseInt(inputItems[3].value))));

            terminal.printCenteredAndWait("Trabalhador registado com sucesso!", Color.GREEN,
                    Style.BOLD);

            mainMenu();
        } else {
            mainMenu();
        }
    }

}
