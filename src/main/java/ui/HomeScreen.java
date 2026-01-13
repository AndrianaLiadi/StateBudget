package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση HomeScreen αποτελεί την κεντρική οθόνη πλοήγησης (Home) της εφαρμογής.
 * <p>
 * Εμφανίζεται αφού ο χρήστης εισέλθει στην εφαρμογή και λειτουργεί ως σημείο επιλογής,
 * δίνοντας τη δυνατότητα μετάβασης στην προβολή του προϋπολογισμού ή επιστροφής
 * στην οθόνη υποδοχής.
 * </p>
 */
public class HomeScreen extends JPanel {

    /**
     * Κατασκευαστής της HomeScreen.
     * <p>
     * Διαμορφώνει τη γραφική διεπαφή χρησιμοποιώντας {@link GridLayout},
     * προσθέτει τον τίτλο καλωσορίσματος και τα κουμπιά πλοήγησης.
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής ({@link AppController}) που διαχειρίζεται
     * τη μετάβαση μεταξύ των οθονών.
     */
    public HomeScreen(AppController controller) {

        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel title = new JLabel("Καλώς ήρθες στο Home", SwingConstants.CENTER);

        JButton budgetBtn = new JButton("Δες Προϋπολογισμό");
        budgetBtn.addActionListener(e -> controller.showScreen(AppController.BUDGET));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> controller.showScreen(AppController.LANDING));

        add(title);
        add(budgetBtn);
        add(backBtn);
    }
}