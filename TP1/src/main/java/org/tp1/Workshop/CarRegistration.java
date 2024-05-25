package org.tp1.Workshop;

public record CarRegistration(String registration) {
    public static boolean validateRegistration(String registration) {
        return registration.matches("[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}");
    }
}
