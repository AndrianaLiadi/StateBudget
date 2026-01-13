package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση ContactScreen υλοποιεί την οθόνη επικοινωνίας της εφαρμογής.
 * <p>
 * Εμφανίζει στους χρήστες τα απαραίτητα στοιχεία (email, τηλέφωνο) για την
 * επικοινωνία με την ομάδα υποστήριξης ή τους δημιουργούς του StateBudgetManager.
 * </p>
 */
public class ContactScreen extends JPanel {

    /**
     * Κατασκευάζει το πάνελ της οθόνης επικοινωνίας.
     * <p>
     * Δημιουργεί μια περιοχή κειμένου (JTextArea) με τα στοιχεία επικοινωνίας
     * και προσθέτει ένα κουμπί για την επιστροφή στην αρχική οθόνη (Landing Screen).
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής της εφαρμογής ({@link AppController})
     * που επιτρέπει την πλοήγηση πίσω.
     */
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
        
        // Προσθήκη περιθωρίου για αισθητικούς λόγους
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(textArea);

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(scroll, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}