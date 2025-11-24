import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public JTextField inputField;
    public JButton submitButton;
    public JLabel infoLabel;

    public MainApp() {
        setTitle("Main App");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField();
        submitButton = new JButton("Submit");
        infoLabel = new JLabel("Type something and press Submit");

        submitButton.addActionListener(e -> {
            String text = inputField.getText().trim();
            if (text.isEmpty()) {
                infoLabel.setText("Please enter text");
            } else {
                infoLabel.setText("You typed: " + text);
            }
        });

        setLayout(new GridLayout(3, 1));
        add(inputField);
        add(submitButton);
        add(infoLabel);
    }
}



