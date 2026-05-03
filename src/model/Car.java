package model;

public class Car extends Vehicle {

    private boolean isAutomatic; // otomatik vites mi?

    // Yeni araç oluştururken
    public Car(String brand, String model, String licensePlate,
               double baseDailyPrice, int mileage, boolean isAutomatic) {
        super(brand, model, licensePlate, baseDailyPrice, mileage, "Car");
        this.isAutomatic = isAutomatic;
    }

    // Dosyadan okurken
    public Car(String vehicleId, String brand, String model, String licensePlate,
               double baseDailyPrice, int mileage, boolean isAvailable, boolean isAutomatic) {
        super(vehicleId, brand, model, licensePlate, baseDailyPrice, mileage, isAvailable, "Car");
        this.isAutomatic = isAutomatic;
    }

    // Otomatik vitesliyse günlük 20 TL ek ücret
    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseDailyPrice() * days;
        if (isAutomatic) cost += 20 * days;
        return cost;
    }

    // Dosyaya şu formatta yazar:
    // Car,id,brand,model,plate,price,mileage,isAvailable,isAutomatic
    @Override
    public String toFileString() {
        return "Car," + getVehicleId() + "," + getBrand() + "," + getModel() + ","
                + getLicensePlate() + "," + getBaseDailyPrice() + ","
                + getMileage() + "," + isAvailable() + "," + isAutomatic;
    }

    public boolean isAutomatic() { return isAutomatic; }
}