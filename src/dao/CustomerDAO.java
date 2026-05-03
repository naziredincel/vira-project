package dao;

import model.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private static final String FILE_NAME = "customers.txt";

    // Tüm müşterileri dosyaya yazar
    public void saveAll(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, false))) {
            for (Customer c : customers) {
                writer.println(c.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Müşteri kaydetme hatası: " + e.getMessage());
        }
    }

    // Dosyadaki tüm müşterileri okur
    public List<Customer> loadAll() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return customers;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Customer c = parseLine(line);
                if (c != null) customers.add(c);
            }
        } catch (IOException e) {
            System.out.println("Müşteri okuma hatası: " + e.getMessage());
        }

        return customers;
    }

    // Yeni müşteri ekle
    public void add(Customer customer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(customer.toFileString());
        } catch (IOException e) {
            System.out.println("Müşteri ekleme hatası: " + e.getMessage());
        }
    }

    // Id'ye göre müşteri sil
    public void delete(String customerId) {
        List<Customer> customers = loadAll();
        customers.removeIf(c -> c.getCustomerId().equals(customerId));
        saveAll(customers);
    }

    // Id'ye göre müşteri bul
    public Customer findById(String customerId) {
        for (Customer c : loadAll()) {
            if (c.getCustomerId().equals(customerId)) return c;
        }
        return null;
    }

    // Satır formatı: id,name,phone,licenseNo
    private Customer parseLine(String line) {
        String[] parts = line.split(",");
        try {
            return new Customer(parts[0], parts[1], parts[2], parts[3]);
        } catch (Exception e) {
            System.out.println("Satır okunamadı: " + line);
        }
        return null;
    }
}
