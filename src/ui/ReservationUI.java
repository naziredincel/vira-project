package ui;

import dao.CustomerDAO;
import dao.ReservationDAO;
import dao.VehicleDAO;
import model.Customer;
import model.Reservation;
import model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReservationUI extends JFrame {

    private ReservationDAO reservationDAO = new ReservationDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    public ReservationUI() {
        setTitle("Vira - Rezervasyon Yönetimi");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Rezervasyon Yönetimi", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(155, 89, 182));

        String[] columns = {"Müşteri", "Araç", "Başlangıç", "Bitiş", "Toplam Ücret"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setBackground(new Color(40, 40, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 80));
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(100, 60, 140));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(40, 40, 55));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setBackground(new Color(30, 30, 40));

        JButton btnAdd = createButton("+ Rezervasyon Ekle", new Color(155, 89, 182));
        JButton btnDelete = createButton("İptal Et", new Color(231, 76, 60));

        btnAdd.addActionListener(e -> showAddDialog());
        btnDelete.addActionListener(e -> deleteSelected());

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadTable();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = reservationDAO.loadAll(vehicleDAO, customerDAO);
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                r.getCustomer().getName(),
                r.getVehicle().toString(),
                r.getStartDate(),
                r.getEndDate(),
                r.getTotalCost() + " TL"
            });
        }
    }

    private void showAddDialog() {
        // Müsait araç ve müşteri listelerini al
        List<Vehicle> vehicles = vehicleDAO.getAvailable();
        List<Customer> customers = customerDAO.loadAll();

        if (vehicles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Müsait araç bulunmuyor!");
            return;
        }
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Önce müşteri eklemelisiniz!");
            return;
        }

        JDialog dialog = new JDialog(this, "Yeni Rezervasyon", true);
        dialog.setSize(350, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(0, 2, 8, 8));
        dialog.getContentPane().setBackground(new Color(40, 40, 55));

        // Araç seçimi
        JComboBox<Vehicle> vehicleBox = new JComboBox<>();
        for (Vehicle v : vehicles) vehicleBox.addItem(v);

        // Müşteri seçimi
        JComboBox<Customer> customerBox = new JComboBox<>();
        for (Customer c : customers) customerBox.addItem(c);

        // Tarih alanları (YYYY-MM-DD formatında)
        JTextField startField = new JTextField(LocalDate.now().toString());
        JTextField endField = new JTextField(LocalDate.now().plusDays(1).toString());

        styleField(startField);
        styleField(endField);

        dialog.add(makeLabel("Araç:")); dialog.add(vehicleBox);
        dialog.add(makeLabel("Müşteri:")); dialog.add(customerBox);
        dialog.add(makeLabel("Başlangıç (YYYY-MM-DD):")); dialog.add(startField);
        dialog.add(makeLabel("Bitiş (YYYY-MM-DD):")); dialog.add(endField);

        JButton btnSave = createButton("Kaydet", new Color(155, 89, 182));
        JButton btnCancel = createButton("İptal", new Color(100, 100, 100));

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                Vehicle selectedVehicle = (Vehicle) vehicleBox.getSelectedItem();
                Customer selectedCustomer = (Customer) customerBox.getSelectedItem();
                LocalDate start = LocalDate.parse(startField.getText().trim());
                LocalDate end = LocalDate.parse(endField.getText().trim());

                if (!end.isAfter(start)) {
                    JOptionPane.showMessageDialog(dialog, "Bitiş tarihi başlangıçtan sonra olmalı!");
                    return;
                }

                // Çakışma kontrolü
                boolean conflict = reservationDAO.hasConflict(
                    selectedVehicle.getVehicleId(), start, end, vehicleDAO, customerDAO
                );

                if (conflict) {
                    JOptionPane.showMessageDialog(dialog, "Bu araç seçilen tarihlerde dolu!");
                    return;
                }

                Reservation r = new Reservation(selectedVehicle, selectedCustomer, start, end);
                reservationDAO.add(r);

                // Aracı dolu olarak işaretle
                selectedVehicle.setAvailable(false);
                List<Vehicle> allVehicles = vehicleDAO.loadAll();
                for (Vehicle v : allVehicles) {
                    if (v.getVehicleId().equals(selectedVehicle.getVehicleId())) {
                        v.setAvailable(false);
                    }
                }
                vehicleDAO.saveAll(allVehicles);

                loadTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Rezervasyon oluşturuldu! Toplam: " + r.getTotalCost() + " TL");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Tarih formatı hatalı! YYYY-MM-DD kullanın.");
            }
        });

        dialog.add(btnSave);
        dialog.add(btnCancel);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen iptal etmek istediğiniz rezervasyonu seçin!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Rezervasyonu iptal etmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Reservation> reservations = reservationDAO.loadAll(vehicleDAO, customerDAO);
            Reservation selected = reservations.get(row);

            // Aracı tekrar müsait yap
            List<Vehicle> allVehicles = vehicleDAO.loadAll();
            for (Vehicle v : allVehicles) {
                if (v.getVehicleId().equals(selected.getVehicle().getVehicleId())) {
                    v.setAvailable(true);
                }
            }
            vehicleDAO.saveAll(allVehicles);

            reservationDAO.delete(selected.getReservationId(), vehicleDAO, customerDAO);
            loadTable();
        }
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        return l;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(55, 55, 70));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
    }
}
