package ui;

import javax.swing.*;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppController controller = new AppController();
            controller.setVisible(true);
        });
    }
}
