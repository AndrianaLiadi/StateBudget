package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση RegistrationScreen υλοποιεί την οθόνη εγγραφής/εισόδου χρήστη.
 * <p>
 * Παρέχει μια απλή φόρμα με πεδία κειμένου για την εισαγωγή ονόματος, επωνύμου
 * και email, επιτρέποντας στον χρήστη να αποκτήσει πρόσβαση στην κύρια εφαρμογή.
 * </p>
 */
public class RegistrationScreen extends JPanel {

    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JButton registerBtn;

    /**
     * Κατασκευαστής της RegistrationScreen.
     * <p>
     * Αρχικοποιεί τη διάταξη (GridLayout) και τα γραφικά στοιχεία της φόρμας.
     * Ορίζει τη λειτουργία του κουμπιού "Register" ώστε να οδηγεί τον χρήστη
     * στην οθόνη υποδοχής (Landing Screen) μέσω του {@link AppController}.
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής της εφαρμογής που διαχειρίζεται την πλοήγηση.
     */
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