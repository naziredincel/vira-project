
package dao;

import model.Car;
import model.ElectricCar;
import model.SUV;
import model.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    private static final String FILE_NAME = "vehicles.txt";

    public void saveAll(List<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (Vehicle v : vehicles) {
                writer.println(v.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Araç kaydetme hatası: " + e.getMessage());
        }
    }

    public List<Vehicle> loadAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return vehicles;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Vehicle v = parseLine(line);
                if (v != null) vehicles.add(v);
            }
        } catch (IOException e) {
            System.out.println("Araç okuma hatası: " + e.getMessage());
        }
        return vehicles;
    }

    public void add(Vehicle vehicle) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(vehicle.toFileString());
        } catch (IOException e) {
            System.out.println("Araç ekleme hatası: " + e.getMessage());
        }
    }

    public void delete(String vehicleId) {
        List<Vehicle> vehicles = loadAll();
        vehicles.removeIf(v -> v.getVehicleId().equals(vehicleId));
        saveAll(vehicles);
    }

    public List<Vehicle> getAvailable() {
        List<Vehicle> all = loadAll();
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle v : all) {
            if (v.isAvailable()) available.add(v);
        }
        return available;
    }

    public Vehicle findById(String vehicleId) {
        for (Vehicle v : loadAll()) {
            if (v.getVehicleId().equals(vehicleId)) return v;
        }
        return null;
    }

    private Vehicle parseLine(String line) {
        String[] parts = line.split(",");
        try {
            String type = parts[0];
            String id = parts[1];
            String brand = parts[2];
            String model = parts[3];
            String plate = parts[4];
            double price = Double.parseDouble(parts[5]);
            int mileage = Integer.parseInt(parts[6]);
            boolean isAvailable = Boolean.parseBoolean(parts[7]);

            if (type.equals("SUV")) {
                boolean has4WD = Boolean.parseBoolean(parts[8]);
                return new SUV(id, brand, model, plate, price, mileage, isAvailable, has4WD);
            } else if (type.equals("ElectricCar")) {
                int battery = Integer.parseInt(parts[8]);
                return new ElectricCar(id, brand, model, plate, price, mileage, isAvailable, battery);
            } else if (type.equals("Car")) {
                boolean isAutomatic = Boolean.parseBoolean(parts[8]);
                return new Car(id, brand, model, plate, price, mileage, isAvailable, isAutomatic);
            }
        } catch (Exception e) {
            System.out.println("Satır okunamadı: " + line);
        }
        return null;
    }
}