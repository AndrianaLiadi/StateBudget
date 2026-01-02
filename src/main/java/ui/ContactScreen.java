package ui;

import javax.swing.*;
import java.awt.*;

public class ContactScreen extends JPanel {

    public ContactScreen(AppController controller) {

        setLayout(new BorderLayout());

        String contactText =
                "Για οποιαδήποτε ερώτηση ή σχόλιο σχετικά με την εφαρμογή StateBudgetManager,\n" +
                "παρακαλούμε επικοινωνήστε μαζί μας.\n\n" +
                "Email: statebudgetmanager@gmail.com\n" +
                "Τηλέφωνο: 2100000000";

        JTextArea textArea = new JTextArea(contactText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(textArea);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(scroll, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}