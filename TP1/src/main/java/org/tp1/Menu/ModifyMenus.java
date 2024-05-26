package org.tp1.Menu;

import org.tp1.TerminalUtils.*;
import org.tp1.Workshop.*;

import java.time.LocalDate;
import java.util.List;

public final class ModifyMenus {
    private final Terminal terminal;
    private final List<Worker> workers;
    private final List<Client> clients;
    private final List<Repair> repairs;
    private final MainMenu mainMenuInstance;

    ModifyMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients,
            List<Repair> repairs) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
        this.repairs = repairs;
    }

    void mainMenu() {
        var modifyOptions = new String[] { "Modificar Trabalhador", "Modificar Cliente", "Modificar Arranjo",
                "Voltar" };

        var modifyOption = terminal.arrowMenu(modifyOptions);

        switch (modifyOption) {
            case 0 -> modifyWorker();

            case 1 -> modifyClient();

            case 2 -> modifyRepair();

            case 3 -> mainMenuInstance.mainMenu();
        }
    }

    private void modifyRepair() {
        var repairsSize = repairs.size();

        if (repairsSize == 0) {
            terminal.printCenteredAndWait("Não existem arranjos registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] repairsSearchItems = new SearchItem[repairsSize];
        for (int i = 0; i < repairsSize; i++) {
            var repair = repairs.get(i);
            repairsSearchItems[i] = new SearchItem(repair.getId(), repair.getCarRegistration().registration());
        }

        var repairIndex = terminal.searchByIdOrNameMenu(repairsSearchItems);

        var repair = repairs.get(repairIndex);

        var mechanics = workers.stream().filter(worker -> worker.getRole() == Role.MECHANIC).toList();
        var mechanicSize = mechanics.size();

        SearchItem[] mechanicsSearchItems = new SearchItem[mechanicSize];
        for (int i = 0; i < mechanicSize; i++) {
            var mechanic = mechanics.get(i);
            mechanicsSearchItems[i] = new SearchItem(mechanic.getId(), mechanic.getName());
        }

        SearchItem[] clientsSearchItems = new SearchItem[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            var client = clients.get(i);
            clientsSearchItems[i] = new SearchItem(client.getId(), client.getName());
        }

        var inputItems = new InputItem[] {
                new InputItem("Matrícula", repair.getCarRegistration().registration()),
                new InputItem("Trabalhador", Integer.toString(repair.getWorkerId()), mechanicsSearchItems),
                new InputItem("Cliente", Integer.toString(repair.getClientId()), clientsSearchItems),
                new InputItem("Descrição", repair.getDescription()),
                new InputItem("Preço (€)", Float.toString(repair.getPrice())),
                new InputItem("Data de Início", repair.getEntryDate().format(mainMenuInstance.dateFormatter)),
                new InputItem("Data de Fim", repair.getExitDate().format(mainMenuInstance.dateFormatter)),
                new InputItem("Tipo de Veículo", repair.getVehicleType().toNumberString(),
                        new String[] { "Carro", "Mota", "Camião" })
        };

        var result = terminal.inputMenu(inputItems);

        if (!result) {
            mainMenuInstance.mainMenu();
            return;
        }

        ValidationMenus.validateEmpty(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validatePrice(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validateCarRegistration(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validateDates(inputItems, terminal, mainMenuInstance);

        var carRegistration = new CarRegistration(inputItems[0].value);
        var workerId = inputItems[1].value;
        var clientId = inputItems[2].value;
        var description = inputItems[3].value;
        var price = Float.parseFloat(inputItems[4].value);

        var entryDate = LocalDate.parse(inputItems[5].value, mainMenuInstance.dateFormatter);
        var exitDate = LocalDate.parse(inputItems[6].value, mainMenuInstance.dateFormatter);

        var vehicleType = VehicleType.fromString(inputItems[7].value);

        repair.setCarRegistration(carRegistration);
        repair.setVehicleType(vehicleType);
        repair.setWorkerId(Integer.parseInt(workerId));
        repair.setClientId(Integer.parseInt(clientId));
        repair.setDescription(description);
        repair.setPrice(price);
        repair.setEntryDate(entryDate);
        repair.setExitDate(exitDate);

        terminal.printCenteredAndWait("Arranjo modificado com sucesso!", Color.GREEN, Style.BOLD);
        mainMenuInstance.mainMenu();
    }

    private void modifyClient() {
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

        var inputItems = new InputItem[] {
                new InputItem("Nome", client.getName()),
                new InputItem("NIF", client.getNIF().to_string()),
                new InputItem("Email", client.getContact().getEmail()),
                new InputItem("Telefone", Integer.toString(client.getContact().getPhone()))
        };

        var result = terminal.inputMenu(inputItems);

        if (!result) {
            mainMenuInstance.mainMenu();
            return;
        }

        ValidationMenus.validateEmailAndPhone(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validateNIF(inputItems, terminal, mainMenuInstance);

        var name = inputItems[0].value;
        var nif = new NIF(inputItems[1].value);
        var email = inputItems[2].value;
        var phone = Integer.parseInt(inputItems[3].value);

        client.setName(name);
        client.setNIF(nif);
        client.getContact().setEmail(email);
        client.getContact().setPhone(phone);

        terminal.printCenteredAndWait("Cliente modificado com sucesso!", Color.GREEN, Style.BOLD);
        mainMenuInstance.mainMenu();
    }

    private void modifyWorker() {
        var workersSize = workers.size();

        if (workersSize == 0) {
            terminal.printCenteredAndWait("Não existem trabalhadores registados!", Color.RED, Style.BOLD);
            mainMenuInstance.mainMenu();
            return;
        }

        SearchItem[] workersSearchItems = new SearchItem[workersSize];
        for (int i = 0; i < workersSize; i++) {
            var worker = workers.get(i);
            workersSearchItems[i] = new SearchItem(worker.getId(), worker.getName());
        }

        var workerIndex = terminal.searchByIdOrNameMenu(workersSearchItems);

        var worker = workers.get(workerIndex);

        var inputItems = new InputItem[] {
                new InputItem("Nome", worker.getName()),
                new InputItem("Salário (€)", Float.toString(worker.getSalary())),
                new InputItem("Email", worker.getContact().getEmail()),
                new InputItem("Telefone", Integer.toString(worker.getContact().getPhone())),
                new InputItem("Tipo de Trabalhador", worker.getRole().toNumberString(),
                        new String[] { "Mecânico", "Gerente", "Rececionista" })
        };

        var result = terminal.inputMenu(inputItems);

        if (!result) {
            mainMenuInstance.mainMenu();
            return;
        }

        ValidationMenus.validateEmailAndPhone(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validateSalary(inputItems, terminal, mainMenuInstance);
        ValidationMenus.validateEmailAndPhone(inputItems, terminal, mainMenuInstance);

        var name = inputItems[0].value;
        var salary = Float.parseFloat(inputItems[1].value);
        var email = inputItems[2].value;
        var phone = Integer.parseInt(inputItems[3].value);
        var role = Role.fromString(inputItems[4].value);

        worker.setName(name);
        worker.setSalary(salary);
        worker.getContact().setEmail(email);
        worker.getContact().setPhone(phone);
        worker.setRole(role);

        terminal.printCenteredAndWait("Trabalhador modificado com sucesso!", Color.GREEN, Style.BOLD);
        mainMenuInstance.mainMenu();

    }
}
