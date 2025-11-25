package src.main.java.ui;
import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public JTextField inputField;
    public JButton submitButton;
    public JLabel infoLabel;

    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenu helpMenu;
    public JMenuItem exitItem;
    public JMenuItem aboutItem;

    public MainApp() {
        setTitle("Main App");
        setSize(300, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenu();

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

    public void setupMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        helpMenu = new JMenu("Help");

        exitItem = new JMenuItem("Exit");
        aboutItem = new JMenuItem("About");

        exitItem.addActionListener(e -> System.exit(0));

        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Basic App\nCreated for a simple Java assignment.",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        fileMenu.add(exitItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}


