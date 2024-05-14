package org.tp1;

public record CarRegistration(String registration) {

    public CarRegistration {
        if (!validateRegistration(registration)) throw new IllegalArgumentException("Invalid registration");
    }

    public static boolean validateRegistration(String registration) {
        return registration.matches("[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}");
    }
}
