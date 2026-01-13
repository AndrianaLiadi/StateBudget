package ui;

import model.BudgetChange;
import java.util.List;

/**
 * Η κλάση BudgetChangeTable είναι υπεύθυνη για την απεικόνιση των αλλαγών του προϋπολογισμού
 * σε μορφή κειμενικού πίνακα στην κονσόλα (CLI).
 * <p>
 * Λαμβάνει μια λίστα με αντικείμενα {@link BudgetChange} και τα εκτυπώνει στη γραμμή εντολών,
 * δείχνοντας τον κωδικό, την περιγραφή και τη μεταβολή των τιμών (Από -> Σε).
 * </p>
 */
public class BudgetChangeTable {

    private List<BudgetChange> changes; 

    /**
     * Κατασκευαστής της κλάσης.
     *
     * @param changes Η λίστα με τις αλλαγές που θα εκτυπωθούν.
     */
    public BudgetChangeTable(List<BudgetChange> changes) {
        this.changes = changes;
    }

    /**
     * Εκτυπώνει τον πίνακα των αλλαγών στην τυπική έξοδο (System.out).
     * <p>
     * Η εκτύπωση περιλαμβάνει τίτλο, μια γραμμή για κάθε αλλαγή με τη μορφή
     * "Κωδικός : Όνομα Από X -> Y" και το συνολικό πλήθος των αλλαγών στο τέλος.
     * </p>
     */
    public void printTable() {
        System.out.println(" ΑΛΛΑΓΜΕΝΟΣ ΠΙΝΑΚΑΣ ");
        for (BudgetChange ch : changes) {
            System.out.println(ch.getItemCode() + " : " + ch.getItemName() + " Από " + ch.getOldValue() + " -> " + ch.getNewValue());
        }
        System.out.println("Σύνολο αλλαγών: " + changes.size());
    }
}