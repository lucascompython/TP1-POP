package org.tp1;

import java.time.LocalDateTime;


public final class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        try {
            var w1 = new Worker("joao", Role.MANAGER, 1000, new Contact("ola@mecanicos.pt", 3123213));
            w1.print();

            var c1 = new Client("lucas", new NIF("999999990"), new Contact("lucas@email.com", 123));
            c1.print();

            var r1 = new Repair(new CarRegistration("11-11-AA"), VehicleType.CAR, w1.getId(), c1.getId(), "mudar oleo", 100, LocalDateTime.now(), LocalDateTime.now().plusWeeks(2));
            r1.print();
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}