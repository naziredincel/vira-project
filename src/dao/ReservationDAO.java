package dao;

import model.Customer;
import model.Reservation;
import model.Vehicle;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private static final String FILE_NAME = "reservations.txt";

    public void saveAll(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (Reservation r : reservations) {
                writer.println(r.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Rezervasyon kaydetme hatası: " + e.getMessage());
        }
    }

    public List<Reservation> loadAll(VehicleDAO vehicleDAO, CustomerDAO customerDAO) {
        List<Reservation> reservations = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return reservations;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Reservation r = parseLine(line, vehicleDAO, customerDAO);
                if (r != null) reservations.add(r);
            }
        } catch (IOException e) {
            System.out.println("Rezervasyon okuma hatası: " + e.getMessage());
        }

        return reservations;
    }

    public void add(Reservation reservation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(reservation.toFileString());
        } catch (IOException e) {
            System.out.println("Rezervasyon ekleme hatası: " + e.getMessage());
        }
    }

    public void delete(String reservationId, VehicleDAO vehicleDAO, CustomerDAO customerDAO) {
        List<Reservation> reservations = loadAll(vehicleDAO, customerDAO);
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
        saveAll(reservations);
    }

    public boolean hasConflict(String vehicleId, LocalDate newStart, LocalDate newEnd,
                               VehicleDAO vehicleDAO, CustomerDAO customerDAO) {
        for (Reservation r : loadAll(vehicleDAO, customerDAO)) {
            if (r.getVehicle().getVehicleId().equals(vehicleId)) {
                if (r.isConflict(newStart, newEnd)) return true;
            }
        }
        return false;
    }

    private Reservation parseLine(String line, VehicleDAO vehicleDAO, CustomerDAO customerDAO) {
        String[] parts = line.split(",");
        try {
            String id = parts[0];
            String vehicleId = parts[1];
            String customerId = parts[2];
            LocalDate startDate = LocalDate.parse(parts[3]);
            LocalDate endDate = LocalDate.parse(parts[4]);
            double totalCost = Double.parseDouble(parts[5]);

            Vehicle vehicle = vehicleDAO.findById(vehicleId);
            Customer customer = customerDAO.findById(customerId);

            if (vehicle == null || customer == null) return null;

            return new Reservation(id, vehicle, customer, startDate, endDate, totalCost);
        } catch (Exception e) {
            System.out.println("Satır okunamadı: " + line);
        }
        return null;
    }
}
