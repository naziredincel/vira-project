
package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {

    public MainMenuUI() {
        setTitle("Vira - Kurumsal Araç Filo Yönetim Sistemi");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // ekranın ortasında açılır
        setResizable(false);

        // Ana panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setLayout(new BorderLayout());

        // Üst başlık
        JLabel titleLabel = new JLabel("VIRA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(new Color(52, 152, 219));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 5, 0));

        JLabel subLabel = new JLabel("Kurumsal Araç Filo Yönetim Sistemi", SwingConstants.CENTER);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subLabel.setForeground(new Color(180, 180, 180));
        subLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 30, 40));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subLabel, BorderLayout.SOUTH);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 40));
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 40, 60));

        JButton btnVehicles = createButton("🚗  Araç Yönetimi", new Color(52, 152, 219));
        JButton btnCustomers = createButton("👤  Müşteri Yönetimi", new Color(46, 204, 113));
        JButton btnReservations = createButton("📅  Rezervasyon Yönetimi", new Color(155, 89, 182));

        // Buton aksiyonları
        btnVehicles.addActionListener(e -> {
            new VehicleUI().setVisible(true);
        });

        btnCustomers.addActionListener(e -> {
            new CustomerUI().setVisible(true);
        });

        btnReservations.addActionListener(e -> {
            new ReservationUI().setVisible(true);
        });

        buttonPanel.add(btnVehicles);
        buttonPanel.add(btnCustomers);
        buttonPanel.add(btnReservations);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Şık buton oluşturucu
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}