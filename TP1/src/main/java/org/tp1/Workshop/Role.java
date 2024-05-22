package org.tp1.Workshop;

public enum Role {
    MECHANIC,
    MANAGER,
    RECEPTIONIST;

    /**
     * Converts a String to a Role, the String must be "0", "1" or "2"
     */
    public static Role fromString(String roleString) {
        return switch (roleString) {
            case "0" -> MECHANIC;
            case "1" -> MANAGER;
            case "2" -> RECEPTIONIST;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
