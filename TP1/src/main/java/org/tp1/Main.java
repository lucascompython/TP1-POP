package org.tp1;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;

public final class Main {
    public static void main(String[] args) {

        try (var terminal = new Terminal()) {

            terminal.printCenteredAndWait("Bem vindo ao sistema de gestão da oficina!", Color.GREEN, Style.BOLD);

            var options = new String[] { "Gestão de Clientes", "Gestão de Mecânicos", "Gestão de Reparações",
                    "Gestão de Veículos", "Gestão de Peças" };

            var option = terminal.arrowMenu(options);

            terminal.clear();

            switch (option) {
                case 0:
                    terminal.printCenteredAndWait("Gestão de Clientes", Color.GREEN, Style.BOLD);
                    break;
                case 1:
                    terminal.printCenteredAndWait("Gestão de Mecânicos", Color.GREEN, Style.BOLD);
                    break;
                case 2:
                    terminal.printCenteredAndWait("Gestão de Reparações", Color.GREEN, Style.BOLD);
                    break;
                case 3:
                    terminal.printCenteredAndWait("Gestão de Veículos", Color.GREEN, Style.BOLD);
                    break;
                case 4:
                    terminal.printCenteredAndWait("Gestão de Peças", Color.GREEN, Style.BOLD);
                    break;
            }


            // try {
            // var w1 = new Worker("joao", Role.MANAGER, 1000, new
            // Contact("ola@mecanicos.pt", 928155170));
            // w1.print();
            //
            // var c1 = new Client("lucas", new NIF("999999990"), new
            // Contact("lucas@email.com", 111111111));
            // c1.print();
            //
            // var r1 = new Repair(new CarRegistration("11-11-AA"), VehicleType.CAR,
            // w1.getId(), c1.getId(), "mudar oleo", 100, LocalDateTime.now(),
            // LocalDateTime.now().plusWeeks(2));
            // r1.print();
            // }
            // catch (IllegalArgumentException e) {
            // System.err.println(e.getMessage());
            // }
        }

    }
}