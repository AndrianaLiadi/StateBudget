package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση Scenario αναπαριστά ένα σενάριο τροποποίησης του κρατικού προϋπολογισμού.
 * <p>
 * Επιτρέπει στον χρήστη να δημιουργήσει μια παραλλαγή του αρχικού προϋπολογισμού (Base Budget),
 * εφαρμόζοντας μια σειρά από αλλαγές ({@link BudgetChange}). Η κλάση διατηρεί τόσο τον
 * αρχικό όσο και τον τροποποιημένο προϋπολογισμό, καθώς και μια σύνοψη των αλλαγών.
 * </p>
 */
public class Scenario {

    /** Το όνομα ή η περιγραφή του σεναρίου. */
    private String itemName;
    
    /** Ο αρχικός (βασικός) προϋπολογισμός πάνω στον οποίο βασίζεται το σενάριο. */
    private Budget baseBudget;
    
    /** Η λίστα με τις αλλαγές που έχουν προστεθεί στο σενάριο. */
    private List<BudgetChange> changes;
    
    /** Ο νέος προϋπολογισμός που προκύπτει μετά την εφαρμογή των αλλαγών. */
    private Budget modifiedBudget;
    
    /** Κειμενική σύνοψη των αλλαγών και των αποτελεσμάτων του σεναρίου. */
    private String summary;

    /**
     * Κατασκευαστής της κλάσης Scenario.
     *
     * @param baseBudget Ο βασικός προϋπολογισμός (Base Budget).
     * @param itemName   Το όνομα του σεναρίου.
     */
    public Scenario(Budget baseBudget, String itemName) {
        this.baseBudget = baseBudget;
        this.itemName = itemName;
        this.changes = new ArrayList<>();
    }

    /**
     * Επιστρέφει το όνομα του σεναρίου.
     *
     * @return Το όνομα του σεναρίου.
     */
    public String getitemName() {
        return itemName;
    }

    /**
     * Επιστρέφει τον βασικό προϋπολογισμό.
     *
     * @return Το αντικείμενο {@link Budget} που αποτελεί τη βάση του σεναρίου.
     */
    public Budget getBaseBudget() {
        return baseBudget;
    }

    /**
     * Επιστρέφει τη λίστα των αλλαγών του σεναρίου.
     *
     * @return Μια λίστα με αντικείμενα {@link BudgetChange}.
     */
    public List<BudgetChange> getChanges() {
        return changes;
    }
    
    /**
     * Επιστρέφει τον τροποποιημένο προϋπολογισμό.
     * <p>
     * Σημείωση: Επιστρέφει {@code null} αν δεν έχει κληθεί ακόμα η μέθοδος {@link #applyChanges()}.
     * </p>
     *
     * @return Ο τροποποιημένος {@link Budget} ή null.
     */
    public Budget getModifiedBudget() {
        return modifiedBudget;
    }

    /**
     * Ορίζει τον τροποποιημένο προϋπολογισμό.
     *
     * @param modifiedBudget Ο νέος προϋπολογισμός.
     */
    public void setModifiedBudget(Budget modifiedBudget) {
        this.modifiedBudget = modifiedBudget;
    }

    /**
     * Επιστρέφει τη σύνοψη του σεναρίου.
     *
     * @return Ένα String με την περιγραφή των αλλαγών.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Ορίζει τη λίστα των αλλαγών για το σενάριο.
     *
     * @param changes Η νέα λίστα αλλαγών.
     */
    public void setChanges(List<BudgetChange> changes) {
        this.changes = changes;
    }

    /**
     * Εφαρμόζει τις αλλαγές που έχουν οριστεί στο σενάριο και δημιουργεί τον τροποποιημένο προϋπολογισμό.
     * <p>
     * Η μέθοδος λειτουργεί ως εξής:
     * <ol>
     * <li>Δημιουργεί αντίγραφα των κονδυλίων του βασικού προϋπολογισμού (για να μην αλλοιωθεί ο αρχικός).</li>
     * <li>Διατρέχει τη λίστα των αλλαγών ({@code changes}).</li>
     * <li>Εντοπίζει το αντίστοιχο κονδύλιο και ενημερώνει το ποσό του.</li>
     * <li>Αν το κονδύλιο δεν υπάρχει (π.χ. νέος κωδικός), το δημιουργεί και το προσθέτει.</li>
     * <li>Αποθηκεύει το αποτέλεσμα στο πεδίο {@code modifiedBudget}.</li>
     * </ol>
     * </p>
     */
    public void applyChanges() {
        List<BudgetItem> newItems = new ArrayList<>();

        // Δημιουργία αντιγράφων των αρχικών αντικειμένων (Deep Copy logic)
        for (BudgetItem item : baseBudget.getItems()) {
            BudgetItem newItem = new BudgetItem(item.getCode(), item.getName(), item.getType(), item.getAmount());
            newItems.add(newItem);
        }

        // Εφαρμογή των αλλαγών
        for (BudgetChange change : changes) {
            BudgetItem targetItem = null;
            // Αναζήτηση του κονδυλίου στη νέα λίστα
            for (BudgetItem it : newItems) {
                if (it.getCode().equals(change.getItemCode())) {
                    targetItem = it;
                    break;
                }
            }

            if (targetItem != null) {
                // Ενημέρωση υπάρχοντος κονδυλίου
                targetItem.updateAmount(change.getNewValue());
            } else {
                // Δημιουργία νέου κονδυλίου αν δεν υπάρχει
                BudgetItem y = new BudgetItem(
                        change.getItemCode(),
                        change.getItemName(),
                        change.getType(),
                        change.getNewValue());
                
                newItems.add(y);
            }
        }

        // Δημιουργία του νέου αντικειμένου Budget
        this.modifiedBudget = new Budget(baseBudget.getYear(), newItems);
    }

    /**
     * Παράγει μια κειμενική σύνοψη (Report) σχετικά με τις αλλαγές που πραγματοποιήθηκαν.
     * <p>
     * Η σύνοψη περιλαμβάνει το όνομα του σεναρίου και μια λίστα με τις αλλαγές,
     * δείχνοντας την παλιά και τη νέα τιμή για κάθε επηρεαζόμενο κονδύλιο.
     * Το αποτέλεσμα αποθηκεύεται στο πεδίο {@code summary}.
     * </p>
     */
    public void generateSummary() {
        if (changes == null || changes.isEmpty()) {
            this.summary = "Δεν υπάρχει καμία αλλαγή στο συγκεκριμένο σενάριο";
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Σενάριο: ").append(itemName).append("\n");
        sb.append("Αλλαγές που εφαρμόστηκαν:\n\n");

        for (BudgetChange change : changes) {
            sb.append("- ")
              .append(change.getItemName()).append(" (")
              .append(change.getItemCode()).append(")")
              .append(": από ")
              .append(change.getOldValue())
              .append(" σε ")
              .append(change.getNewValue())
              .append("\n");
        }

        this.summary = sb.toString();
    }
}
