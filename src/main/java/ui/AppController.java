package src.main.java.ui;

import javax.swing.*;
import java.awt.*;

public class AppController extends JFrame {

    private CardLayout layout;
    private JPanel container;

    public static final String REGISTRATION = "registration";
    public static final String LANDING = "landing";
    public static final String HOME = "home";
    public static final String ABOUT = "about";
    public static final String CONTACT = "contact";

    public AppController() {
        setTitle("State Budget App");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        container = new JPanel(layout);

        // Οθόνες
        container.add(new RegistrationScreen(this), REGISTRATION);
        container.add(new LandingScreen(this), LANDING);
        container.add(new HomeScreen(this), HOME);
        container.add(new AboutScreen(this), ABOUT);
        container.add(new ContactScreen(this), CONTACT);

        add(container);

        showScreen(REGISTRATION); // Αρχική οθόνη
    }

    public void showScreen(String screen) {
        layout.show(container, screen);
    }
}