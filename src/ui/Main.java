
package ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuUI menu = new MainMenuUI();
            menu.setVisible(true);
        });
    }
}