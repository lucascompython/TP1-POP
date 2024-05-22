package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.InputItem;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;
import org.tp1.Workshop.*;

import java.util.List;

public final class RegisterMenus {
    private final Terminal terminal;
    private final List<Worker> workers;
    private final List<Client> clients;
    private final MainMenu mainMenuInstance;

   RegisterMenus(MainMenu mainMenuInstance, Terminal terminal, List<Worker> workers, List<Client> clients) {
        this.mainMenuInstance = mainMenuInstance;
        this.terminal = terminal;
        this.workers = workers;
        this.clients = clients;
    }

    void mainMenu() {
        var registerOptions = new String[] { "Registar Trabalhador", "Registar Cliente", "Registar Arranjo", "Voltar" };

        var registerOption = terminal.arrowMenu(registerOptions);

        switch (registerOption) {
            case 0 -> registerWorkerMenu();

            case 1 -> registerClient();

            case 2 -> terminal.printCenteredAndWait("Registar Arranjo", Color.GREEN, Style.BOLD);

            case 3 -> mainMenuInstance.mainMenu();
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
