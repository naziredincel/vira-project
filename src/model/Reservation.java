package model;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {

    private String reservationId;
    private Vehicle vehicle;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;

    // Yeni rezervasyon oluştururken
    public Reservation(Vehicle vehicle, Customer customer,
                       LocalDate startDate, LocalDate endDate) {
        this.reservationId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        int days = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        this.totalCost = vehicle.calculateRentalCost(days);
    }

    // Dosyadan okurken
    public Reservation(String reservationId, Vehicle vehicle, Customer customer,
                       LocalDate startDate, LocalDate endDate, double totalCost) {
        this.reservationId = reservationId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }

    // Bu rezervasyon, verilen tarih aralığıyla çakışıyor mu?
    public boolean isConflict(LocalDate newStart, LocalDate newEnd) {
        return (newStart.isBefore(this.endDate) && newEnd.isAfter(this.startDate));
    }

    // Dosyaya şu formatta yazar:
    // id,vehicleId,customerId,startDate,endDate,totalCost
    public String toFileString() {
        return reservationId + "," + vehicle.getVehicleId() + ","
                + customer.getCustomerId() + "," + startDate + ","
                + endDate + "," + totalCost;
    }

    public String getReservationId() { return reservationId; }
    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalCost() { return totalCost; }

    @Override
    public String toString() {
        return customer.getName() + " → " + vehicle.toString()
                + " | " + startDate + " / " + endDate
                + " | " + totalCost + " TL";
    }
}