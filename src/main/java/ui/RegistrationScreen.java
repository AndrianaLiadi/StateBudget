package ui;

import javax.swing.*;
import java.awt.*;

public class RegistrationScreen extends JPanel {

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JButton registerBtn;

    public RegistrationScreen(AppController controller) {

        setLayout(new GridLayout(4, 1, 10, 10));

        nameField = new JTextField("Name");
        surnameField = new JTextField("Surname");
        emailField = new JTextField("Email");
        registerBtn = new JButton("Register");

        registerBtn.addActionListener(e -> {
            controller.showScreen(AppController.LANDING);
        });

        add(nameField);
        add(surnameField);
        add(emailField);
        add(registerBtn);
    }
}

