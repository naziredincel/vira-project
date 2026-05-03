package ui;

import dao.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerUI extends JFrame {

    private CustomerDAO customerDAO = new CustomerDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    public CustomerUI() {
        setTitle("Vira - Müşteri Yönetimi");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Müşteri Yönetimi", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(46, 204, 113));

        String[] columns = {"Ad Soyad", "Telefon", "Ehliyet No"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setBackground(new Color(40, 40, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 60, 80));
        table.getTableHeader().setBackground(new Color(46, 204, 113));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(30, 150, 80));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(40, 40, 55));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.setBackground(new Color(30, 30, 40));

        JButton btnAdd = createButton("+ Müşteri Ekle", new Color(46, 204, 113));
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

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.loadAll();
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getName(),
                c.getPhone(),
                c.getLicenseNo()
            });
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Yeni Müşteri Ekle", true);
        dialog.setSize(300, 230);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(0, 2, 8, 8));
        dialog.getContentPane().setBackground(new Color(40, 40, 55));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField licenseField = new JTextField();

        styleField(nameField);
        styleField(phoneField);
        styleField(licenseField);

        dialog.add(makeLabel("Ad Soyad:")); dialog.add(nameField);
        dialog.add(makeLabel("Telefon:")); dialog.add(phoneField);
        dialog.add(makeLabel("Ehliyet No:")); dialog.add(licenseField);

        JButton btnSave = createButton("Kaydet", new Color(46, 204, 113));
        JButton btnCancel = createButton("İptal", new Color(100, 100, 100));

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String license = licenseField.getText().trim();

                if (name.isEmpty() || phone.isEmpty() || license.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Lütfen tüm alanları doldurun!");
                    return;
                }

                Customer c = new Customer(name, phone, license);
                customerDAO.add(c);
                loadTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Hata oluştu!");
            }
        });

        dialog.add(btnSave);
        dialog.add(btnCancel);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek istediğiniz müşteriyi seçin!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Müşteriyi silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Customer> customers = customerDAO.loadAll();
            customerDAO.delete(customers.get(row).getCustomerId());
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