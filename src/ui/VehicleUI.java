package ui;

import dao.VehicleDAO;
import model.Car;
import model.ElectricCar;
import model.SUV;
import model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VehicleUI extends JFrame {

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    public VehicleUI() {
        setTitle("Vira - Araç Yönetimi");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Başlık
        JLabel title = new JLabel("Araç Yönetimi", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(52, 152, 219));

        // Tablo
        String[] columns = {"Tür", "Marka", "Model", "Plaka", "Günlük Fiyat", "KM", "Durum"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setBackground(new Color(40, 40, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 80));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(52, 100, 160));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(40, 40, 55));

        // Alt butonlar
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setBackground(new Color(30, 30, 40));

        JButton btnAdd = createButton("+ Araç Ekle", new Color(52, 152, 219));
        JButton btnDelete = createButton("Sil", new Color(231, 76, 60));

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

    // Tabloyu doldur
    private void loadTable() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = vehicleDAO.loadAll();
        for (Vehicle v : vehicles) {
            tableModel.addRow(new Object[]{
                v.getType(),
                v.getBrand(),
                v.getModel(),
                v.getLicensePlate(),
                v.getBaseDailyPrice() + " TL",
                v.getMileage() + " km",
                v.isAvailable() ? "Müsait" : "Dolu"
            });
        }
    }

    // Araç ekleme dialog penceresi
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Yeni Araç Ekle", true);
        dialog.setSize(350, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(0, 2, 8, 8));
        dialog.getContentPane().setBackground(new Color(40, 40, 55));

        // Alanlar
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Car", "SUV", "ElectricCar"});
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField plateField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField mileageField = new JTextField();
        JCheckBox extraBox = new JCheckBox("Otomatik / 4WD / Batarya(kWh)");
        JTextField extraField = new JTextField();

        styleField(brandField); styleField(modelField);
        styleField(plateField); styleField(priceField);
        styleField(mileageField); styleField(extraField);
        extraBox.setBackground(new Color(40, 40, 55));
        extraBox.setForeground(Color.WHITE);

        dialog.add(makeLabel("Tür:")); dialog.add(typeBox);
        dialog.add(makeLabel("Marka:")); dialog.add(brandField);
        dialog.add(makeLabel("Model:")); dialog.add(modelField);
        dialog.add(makeLabel("Plaka:")); dialog.add(plateField);
        dialog.add(makeLabel("Günlük Fiyat:")); dialog.add(priceField);
        dialog.add(makeLabel("KM:")); dialog.add(mileageField);
        dialog.add(makeLabel("Ekstra (sayı):")); dialog.add(extraField);

        JButton btnSave = createButton("Kaydet", new Color(46, 204, 113));
        JButton btnCancel = createButton("İptal", new Color(100, 100, 100));

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                String plate = plateField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int mileage = Integer.parseInt(mileageField.getText().trim());

                Vehicle v = null;
                if (type.equals("Car")) {
                    boolean isAuto = extraField.getText().trim().equals("1");
                    v = new Car(brand, model, plate, price, mileage, isAuto);
                } else if (type.equals("SUV")) {
                    boolean has4WD = extraField.getText().trim().equals("1");
                    v = new SUV(brand, model, plate, price, mileage, has4WD);
                } else if (type.equals("ElectricCar")) {
                    int battery = Integer.parseInt(extraField.getText().trim());
                    v = new ElectricCar(brand, model, plate, price, mileage, battery);
                }

                if (v != null) {
                    vehicleDAO.add(v);
                    loadTable();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lütfen tüm alanları doğru doldurun!");
            }
        });

        dialog.add(btnSave);
        dialog.add(btnCancel);
        dialog.setVisible(true);
    }

    // Seçili aracı sil
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz aracı seçin!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Aracı silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Vehicle> vehicles = vehicleDAO.loadAll();
            vehicleDAO.delete(vehicles.get(row).getVehicleId());
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