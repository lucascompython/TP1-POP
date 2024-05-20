package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.InputItem;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;

public class Menu {
    private final Terminal terminal;

    private void mainMenu() {

        var options = new String[] { "Registar", "Modificar", "Listar", "Estatisticas", "Sair" };

        var option = terminal.arrowMenu(options);


        switch (option) {
            case 0 -> {
                var registerOptions = new String[] { "Registar Trabalhador", "Registar Cliente", "Registar Arranjo" };

                var registerOption = terminal.arrowMenu(registerOptions);

                switch (registerOption) {
                    case 0 -> {
                        var inputItems = new InputItem[] {
                                new InputItem("Nome", ""),
                                new InputItem("NIF", ""),
                                new InputItem("Email", ""),
                                new InputItem("Telefone", ""),
                                new InputItem("Tipo de Trabalhador", "", new String[] { "Mecânico", "Gestor" })
                        };

                        var result = terminal.inputMenu(inputItems);

                        if (result) {
                            terminal.printCenteredAndWait("Trabalhador registado com sucesso!", Color.GREEN, Style.BOLD);
                            mainMenu();
                        }
                        else {
                           mainMenu();
                        }
                    }
                    case 1 -> terminal.printCenteredAndWait("Registar Cliente", Color.GREEN, Style.BOLD);
                    case 2 -> terminal.printCenteredAndWait("Registar Arranjo", Color.GREEN, Style.BOLD);
                }

            }
            case 1 -> terminal.printCenteredAndWait("Gestão de Mecânicos", Color.GREEN, Style.BOLD);
            case 2 -> terminal.printCenteredAndWait("Gestão de Reparações", Color.GREEN, Style.BOLD);
            case 3 -> terminal.printCenteredAndWait("Gestão de Veículos", Color.GREEN, Style.BOLD);
            case 4 -> {} // exit

        }

    }


    public Menu() {

        try (var terminal = new Terminal()) {
            this.terminal = terminal;
            terminal.printCenteredAndWait("Bem vindo ao sistema de gestão da oficina!", Color.GREEN, Style.BOLD);
            mainMenu();
        }
    }
}
