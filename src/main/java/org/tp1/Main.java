package org.tp1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        try {
            Worker w1 = new Worker("joao", Role.MANAGER, 1000, new Contact("ola@mecanicos.pt", 3123213));
            w1.print();

            Client c1 = new Client("lucas", new NIF("999999990"), new Contact("lucas@email.com", 123));
            c1.print();
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}