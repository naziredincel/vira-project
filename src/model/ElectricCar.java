package model;

public class ElectricCar extends Vehicle {

    private int batteryCapacity; // kWh cinsinden batarya kapasitesi

    // Yeni elektrikli araç oluştururken
    public ElectricCar(String brand, String model, String licensePlate,
                       double baseDailyPrice, int mileage, int batteryCapacity) {
        super(brand, model, licensePlate, baseDailyPrice, mileage, "ElectricCar");
        this.batteryCapacity = batteryCapacity;
    }

    // Dosyadan okurken
    public ElectricCar(String vehicleId, String brand, String model, String licensePlate,
                       double baseDailyPrice, int mileage, boolean isAvailable, int batteryCapacity) {
        super(vehicleId, brand, model, licensePlate, baseDailyPrice, mileage, isAvailable, "ElectricCar");
        this.batteryCapacity = batteryCapacity;
    }

    // Elektrikli araçlara %15 indirim uygulanıyor
    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseDailyPrice() * days;
        return cost - (cost * 0.15);
    }

    // Dosyaya şu formatta yazar:
    // ElectricCar,id,brand,model,plate,price,mileage,isAvailable,batteryCapacity
    @Override
    public String toFileString() {
        return "ElectricCar," + getVehicleId() + "," + getBrand() + "," + getModel() + ","
                + getLicensePlate() + "," + getBaseDailyPrice() + ","
                + getMileage() + "," + isAvailable() + "," + batteryCapacity;
    }

    public int getBatteryCapacity() { return batteryCapacity; }
}