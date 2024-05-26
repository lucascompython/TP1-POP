package org.tp1.Workshop;

import java.time.LocalDate;

public final class Repair {
    public static int count = 0;
    private final int id;
    private CarRegistration carRegistration;
    private VehicleType vehicleType;
    private int workerId;
    private int clientId;
    private String description;
    private float price;
    private LocalDate entryDate;
    private LocalDate exitDate;

    public Repair(CarRegistration carRegistration, int workerId, int clientId, String description, float price,
            LocalDate entryDate, LocalDate exitDate, VehicleType vehicleType) {
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

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public void setCarRegistration(CarRegistration carRegistration) {
        this.carRegistration = carRegistration;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public void print() {
        System.out.println("Repair [id=" + id + ", carRegistration=" + carRegistration + ", vehicleType=" + vehicleType
                + ", workerId=" + workerId + ", clientId=" + clientId + ", description=" + description + ", price="
                + price + ", entryDate=" + entryDate + ", exitDate=" + exitDate + "]");
    }
}
