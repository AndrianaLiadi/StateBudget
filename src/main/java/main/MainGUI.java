package main;

import ui.AppController;
import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppController app = new AppController();
            app.setVisible(true);
        });
    }
}