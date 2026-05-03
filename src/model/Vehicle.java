package model;

import java.util.UUID;

public abstract class Vehicle {

    private String vehicleId;
    private String brand;
    private String model;
    private String licensePlate;
    private double baseDailyPrice;
    private int mileage;
    private boolean isAvailable;
    private String type;

    // Yeni araç oluştururken
    public Vehicle(String brand, String model, String licensePlate,
                   double baseDailyPrice, int mileage, String type) {
        this.vehicleId = UUID.randomUUID().toString();
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.baseDailyPrice = baseDailyPrice;
        this.mileage = mileage;
        this.isAvailable = true;
        this.type = type;
    }

    // Dosyadan okurken (id zaten var)
    public Vehicle(String vehicleId, String brand, String model, String licensePlate,
                   double baseDailyPrice, int mileage, boolean isAvailable, String type) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.baseDailyPrice = baseDailyPrice;
        this.mileage = mileage;
        this.isAvailable = isAvailable;
        this.type = type;
    }

    public abstract double calculateRentalCost(int days);
    public abstract String toFileString();

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getLicensePlate() { return licensePlate; }
    public double getBaseDailyPrice() { return baseDailyPrice; }
    public int getMileage() { return mileage; }
    public boolean isAvailable() { return isAvailable; }
    public String getType() { return type; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean needsMaintenance() {
        return mileage > 15000;
    }

    @Override
    public String toString() {
        return brand + " " + model + " [" + licensePlate + "]";
    }
}