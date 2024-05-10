package org.tp1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        Worker w1 = new Worker("lucas", Role.MANAGER, 1000);

        w1.print();
    }
}