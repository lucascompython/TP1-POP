package org.tp1.Menu;

import org.tp1.TerminalUtils.*;
import org.tp1.Workshop.*;

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

            // case 1 -> modifyClient();
            //
            // case 2 -> modifyRepair();

            case 3 -> mainMenuInstance.mainMenu();
        }
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
