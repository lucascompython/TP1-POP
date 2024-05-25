package org.tp1.Workshop;

public enum VehicleType {
    CAR,
    MOTORCYCLE,
    TRUCK;

    public static VehicleType fromString(String type) {
        return switch (type) {
            case "0" -> CAR;
            case "1" -> MOTORCYCLE;
            case "2" -> TRUCK;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
