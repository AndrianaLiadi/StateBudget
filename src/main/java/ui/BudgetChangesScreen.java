package ui;

import model.BudgetChange;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Η κλάση BudgetChangesScreen είναι υπεύθυνη για την εμφάνιση μιας λίστας με τις αλλαγές
 * που έχουν πραγματοποιηθεί σε ένα σενάριο προϋπολογισμού.
 * <p>
 * Εμφανίζει τα αποτελέσματα σε μορφή απλού κειμένου (Text Area), παραθέτοντας
 * τον κωδικό, το όνομα, την αρχική και την τελική τιμή για κάθε τροποποίηση.
 * </p>
 */
public class BudgetChangesScreen extends JPanel {

    /**
     * Κατασκευαστής της οθόνης αλλαγών.
     * <p>
     * Δημιουργεί τη διεπαφή χρήστη, μορφοποιεί τα δεδομένα των αλλαγών σε κείμενο
     * και προσθέτει το κουμπί επιστροφής στην αρχική οθόνη.
     * </p>
     *
     * @param controller Ο ελεγκτής της εφαρμογής ({@link AppController}) για την πλοήγηση.
     * @param changes    Η λίστα με τα αντικείμενα {@link BudgetChange} που θα προβληθούν.
     */
    public BudgetChangesScreen(AppController controller, List<BudgetChange> changes) {

        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("ΑΛΛΑΓΜΕΝΟΣ ΠΙΝΑΚΑΣ\n\n");

        for (BudgetChange ch : changes) {
            sb.append(ch.getItemCode()).append(" : ")
              .append(ch.getItemName())
              .append(" Από ").append(ch.getOldValue())
              .append(" -> ").append(ch.getNewValue())
              .append("\n");
        }

        sb.append("\nΣύνολο αλλαγών: ").append(changes.size());

        area.setText(sb.toString());

        add(new JScrollPane(area), BorderLayout.CENTER);

        JButton back = new JButton("Back");
        // Σημείωση: Εδώ επιστρέφει στο HOME, αλλά ίσως θα βόλευε να επιστρέφει στο SCENARIO ή REPORTS
        back.addActionListener(e -> controller.showScreen(AppController.HOME));

        add(back, BorderLayout.SOUTH);
    }
}