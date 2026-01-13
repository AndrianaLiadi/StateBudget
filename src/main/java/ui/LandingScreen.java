package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση LandingScreen αποτελεί την οθόνη υποδοχής (Landing Page) της εφαρμογής.
 * <p>
 * Είναι το πρώτο σημείο επαφής του χρήστη με το γραφικό περιβάλλον και παρέχει
 * το κεντρικό μενού πλοήγησης προς τις βασικές ενότητες (Home, About, Contact).
 * </p>
 */
public class LandingScreen extends JPanel {

    /**
     * Κατασκευαστής της LandingScreen.
     * <p>
     * Ορίζει τη διάταξη των στοιχείων (GridLayout) και δημιουργεί τα κουμπιά
     * που επιτρέπουν την πλοήγηση μέσω του {@link AppController}.
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής της εφαρμογής που διαχειρίζεται
     * την εναλλαγή των οθονών.
     */
    public LandingScreen(AppController controller) {

        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel welcome = new JLabel("Καλώς ήρθες στην εφαρμογή!", SwingConstants.CENTER);

        JButton homeBtn = new JButton("Home");
        JButton aboutBtn = new JButton("About");
        JButton contactBtn = new JButton("Contact");

        homeBtn.addActionListener(e -> controller.showScreen(AppController.HOME));
        aboutBtn.addActionListener(e -> controller.showScreen(AppController.ABOUT));
        contactBtn.addActionListener(e -> controller.showScreen(AppController.CONTACT));

        add(welcome);
        add(homeBtn);
        add(aboutBtn);
        add(contactBtn);
    }
}