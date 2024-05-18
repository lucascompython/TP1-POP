package org.tp1;

import org.tp1.TerminalUtils.Color;
import org.tp1.TerminalUtils.Style;
import org.tp1.TerminalUtils.Terminal;

public final class Main {
    public static void main(String[] args) {

        try (var terminal = new Terminal()) {

            terminal.print_centered("Hello and welcome!", Color.RED, Style.UNDERLINE);

            var key = terminal.readKey();

            if (key == 'q') {
                System.out.print("Goodbye!");
            } else {

                terminal.print("You pressed: " + (char) key + " (" + key + ")");
                terminal.print("Press 'q' to quit.\nNew Line");

                // sleep 5 seconds

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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