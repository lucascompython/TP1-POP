package org.tp1;

import org.tp1.Menu.MainMenu;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Worker;

import java.util.ArrayList;
import java.util.List;

public final class Main {
    public static void main(String[] args) {

        List<Worker> workers = new ArrayList<>();
        List<Client> clients = new ArrayList<>();

        new MainMenu(workers, clients);

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