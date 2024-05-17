package org.tp1;

import java.time.LocalDateTime;

public final class Repair {
    public static int count = 0;
    private final int id;
    private final CarRegistration carRegistration;
    private final VehicleType vehicleType;
    private final int workerId;
    private final int clientId;
    private final String description;
    private final float price;
    private final LocalDateTime entryDate;
    private LocalDateTime exitDate;

    public Repair(CarRegistration carRegistration, VehicleType vehicleType, int workerId, int clientId,
            String description, float price, LocalDateTime entryDate, LocalDateTime exitDate) {
        this.id = ++count;
        this.carRegistration = carRegistration;
        this.vehicleType = vehicleType;
        this.workerId = workerId;
        this.clientId = clientId;
        this.description = description;
        this.price = price;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
    }

    public int getId() {
        return id;
    }

    public CarRegistration getCarRegistration() {
        return carRegistration;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getWorkerId() {
        return workerId;
    }

    public int getClientId() {
        return clientId;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public void print() {
        System.out.println("Repair [id=" + id + ", carRegistration=" + carRegistration + ", vehicleType=" + vehicleType
                + ", workerId=" + workerId + ", clientId=" + clientId + ", description=" + description + ", price="
                + price + ", entryDate=" + entryDate + ", exitDate=" + exitDate + "]");
    }
}
