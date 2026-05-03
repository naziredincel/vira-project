package model;

public class SUV extends Vehicle {

    private boolean has4WD;

    // Yeni SUV oluştururken
    public SUV(String brand, String model, String licensePlate,
               double baseDailyPrice, int mileage, boolean has4WD) {
        super(brand, model, licensePlate, baseDailyPrice, mileage, "SUV");
        this.has4WD = has4WD;
    }

    // Dosyadan okurken
    public SUV(String vehicleId, String brand, String model, String licensePlate,
               double baseDailyPrice, int mileage, boolean isAvailable, boolean has4WD) {
        super(vehicleId, brand, model, licensePlate, baseDailyPrice, mileage, isAvailable, "SUV");
        this.has4WD = has4WD;
    }

    // 4WD varsa günlük 35 TL ek ücret
    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseDailyPrice() * days;
        if (has4WD) cost += 35 * days;
        return cost;
    }

    // Dosyaya şu formatta yazar:
    // SUV,id,brand,model,plate,price,mileage,isAvailable,has4WD
    @Override
    public String toFileString() {
        return "SUV," + getVehicleId() + "," + getBrand() + "," + getModel() + ","
                + getLicensePlate() + "," + getBaseDailyPrice() + ","
                + getMileage() + "," + isAvailable() + "," + has4WD;
    }

    public boolean isHas4WD() { return has4WD; }
}
