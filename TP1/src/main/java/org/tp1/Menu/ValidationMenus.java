package org.tp1.Menu;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.InputItem;
import org.tp1.TerminalUtils.Style;
import org.tp1.Workshop.CarRegistration;
import org.tp1.Workshop.Contact;
import org.tp1.Workshop.NIF;
import org.tp1.TerminalUtils.Terminal;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class ValidationMenus {

    static void validateEmailAndPhone(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (!Contact.validateEmail(inputItems[2].value)) {
            terminal.printCenteredAndWait("Email inválido!", Color.RED, Style.BOLD);
            var emailInput = new InputItem("Email", inputItems[2].value);
            var result = terminal.inputMenu(new InputItem[] { emailInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[2].value = emailInput.value;
        }

        while (!Contact.validatePhone(inputItems[3].value)) {
            terminal.printCenteredAndWait("Telefone inválido!", Color.RED, Style.BOLD);
            var phoneInput = new InputItem("Telefone", inputItems[3].value);
            var result = terminal.inputMenu(new InputItem[] { phoneInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[3].value = phoneInput.value;
        }
    }

    static void validateNIF(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (!NIF.validate(inputItems[1].value)) {
            terminal.printCenteredAndWait("NIF inválido!", Color.RED, Style.BOLD);
            var nifInput = new InputItem("NIF", inputItems[1].value);
            var result = terminal.inputMenu(new InputItem[] { nifInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[1].value = nifInput.value;
        }
    }

    static void validateSalary(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (true) {
            try {
                if (Float.parseFloat(inputItems[1].value) >= 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                // Do nothing
            }
            terminal.printCenteredAndWait("Salário inválido!", Color.RED, Style.BOLD);
            var salaryInput = new InputItem("Salário (€)", inputItems[1].value);
            var result = terminal.inputMenu(new InputItem[] { salaryInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[1].value = salaryInput.value;
        }
    }

    static void validateEmpty(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        for (var item : inputItems) {
            while (item.value.isEmpty()) {
                terminal.printCenteredAndWait("Campo vazio: " + item.label + "!", Color.RED, Style.BOLD);
                var result = terminal.inputMenu(new InputItem[] { item });
                if (!result) {
                    mainMenuInstance.mainMenu();
                    return;
                }
            }
        }
    }

    static void validatePrice(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (true) {
            try {
                if (Float.parseFloat(inputItems[4].value) >= 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                // Do nothing
            }
            terminal.printCenteredAndWait("Preço inválido!", Color.RED, Style.BOLD);
            var priceInput = new InputItem("Preço (€)", inputItems[4].value);
            var result = terminal.inputMenu(new InputItem[] { priceInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[4].value = priceInput.value;
        }
    }

    static void validateCarRegistration(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (!CarRegistration.validateRegistration(inputItems[0].value)) {
            terminal.printCenteredAndWait("Matrícula inválida!", Color.RED, Style.BOLD);
            var registrationInput = new InputItem("Matrícula (AA-11-BB)", inputItems[0].value);
            var result = terminal.inputMenu(new InputItem[] { registrationInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[0].value = registrationInput.value;
        }
    }

    static void validateDates(InputItem[] inputItems, Terminal terminal, MainMenu mainMenuInstance) {
        while (true) {
            try {
                var startDate = LocalDate.parse(inputItems[5].value, mainMenuInstance.formatter);
                var endDate = LocalDate.parse(inputItems[6].value, mainMenuInstance.formatter);
                if (startDate.isBefore(endDate)) {
                    break;
                }
            } catch (DateTimeParseException e) {
                // Do nothing
            }
            terminal.printCenteredAndWait("Datas inválidas!", Color.RED, Style.BOLD);
            var startDateInput = new InputItem("Data de início (dd-MM-yyyy)", inputItems[5].value);
            var endDateInput = new InputItem("Data de fim (dd-MM-yyyy)", inputItems[6].value);
            var result = terminal.inputMenu(new InputItem[] { startDateInput, endDateInput });
            if (!result) {
                mainMenuInstance.mainMenu();
                return;
            }
            inputItems[5].value = startDateInput.value;
            inputItems[6].value = endDateInput.value;
        }
    }

}
