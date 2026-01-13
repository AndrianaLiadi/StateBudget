package ui;

import javax.swing.*;

import model.Budget;
import model.Scenario;

import java.awt.*;

/**
 * Η κλάση ReportScreen υλοποιεί την οθόνη παρουσίασης των αποτελεσμάτων (Αναφορών).
 * <p>
 * Συνδυάζει κειμενική περιγραφή (σύνοψη) και γραφική απεικόνιση (μέσω του {@link ChartPanel})
 * για να παρουσιάσει τις διαφορές μεταξύ του βασικού προϋπολογισμού και του τροποποιημένου σεναρίου.
 * </p>
 */
public class ReportScreen extends JPanel {

    /**
     * Κατασκευαστής της οθόνης αναφορών.
     * <p>
     * Ελέγχει αρχικά αν υπάρχει διαθέσιμο σενάριο.
     * <ul>
     * <li>Αν το σενάριο είναι {@code null}, εμφανίζει μήνυμα που προτρέπει τον χρήστη να δημιουργήσει ένα.</li>
     * <li>Αν υπάρχει σενάριο, εμφανίζει τη σύνοψη των αλλαγών σε περιοχή κειμένου (αριστερά)
     * και το συγκριτικό γράφημα (κέντρο).</li>
     * </ul>
     * </p>
     *
     * @param controller Ο κεντρικός ελεγκτής της εφαρμογής για την πλοήγηση.
     * @param scenario   Το αντικείμενο του σεναρίου που περιέχει τα δεδομένα προς απεικόνιση.
     */
     public ReportScreen(AppController controller, Scenario scenario) {

        setLayout(new BorderLayout());

        JButton back = new JButton("Back");
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        
        // Έλεγχος αν δεν έχει επιλεγεί/δημιουργηθεί σενάριο
        if (scenario == null) {
            area.setText("Δεν υπάρχει σενάριο ακόμη. Πήγαινε στο Scenarios και δημιούργησε ένα.");
            add(new JScrollPane(area), BorderLayout.CENTER);
            add(back, BorderLayout.SOUTH);
            return;
        }
        
        // Ρύθμιση δεδομένων εφόσον υπάρχει σενάριο
        area.setText(scenario.getSummary());
        Budget base = scenario.getBaseBudget();
        Budget modified = scenario.getModifiedBudget();

        ChartPanel chart = new ChartPanel(base, modified);

        
        back.addActionListener(e ->
            controller.showScreen(AppController.HOME)
        );

        add(new JScrollPane(area), BorderLayout.WEST);
        add(chart, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
    }
}