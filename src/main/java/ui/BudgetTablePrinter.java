package ui;

import model.Budget;
import model.BudgetItem;
import java.util.List;

/**
 * Η κλάση BudgetTablePrinter είναι υπεύθυνη για την εκτύπωση των δεδομένων του προϋπολογισμού
 * στην τυπική έξοδο (κονσόλα/τερματικό).
 * <p>
 * Οργανώνει τα δεδομένα σε ευανάγνωστους πίνακες, διαχωρίζοντας τα Έσοδα από τα Έξοδα,
 * και παρουσιάζει τα συνολικά αθροίσματα και το δημοσιονομικό ισοζύγιο.
 * </p>
 */
public class BudgetTablePrinter {

    /**
     * Εκτυπώνει μια πλήρη αναφορά του κρατικού προϋπολογισμού.
     * <p>
     * Η μέθοδος καλεί διαδοχικά την εκτύπωση των ενοτήτων (Εσόδων και Εξόδων)
     * και στη συνέχεια υπολογίζει και εμφανίζει τα συγκεντρωτικά αποτελέσματα
     * (Σύνολο Εσόδων, Σύνολο Εξόδων και Ισοζύγιο).
     * </p>
     *
     * @param budget Το αντικείμενο {@link Budget} που περιέχει τα δεδομένα προς εκτύπωση.
     */
    public void printBudget(Budget budget) {
        System.out.println("---------ΚΡΑΤΙΚΟΣ ΠΡΟΫΠΟΛΟΓΙΣΜΟΣ---------");

        printSection("ΕΣΟΔΑ", budget.getItemsByType("REVENUE"));
        printSection("ΕΞΟΔΑ", budget.getItemsByType("EXPENDITURE"));

        System.out.println("    ΣΥΝΟΛΙΚΑ    ");
        System.out.println("Έσοδα: " + budget.totalRevenue());
        System.out.println("Έξοδα: " + budget.totalExpenditure());
        // Σημείωση: Η surplusdeficitFinder εκτυπώνει και η ίδια μήνυμα, πέρα από την επιστροφή τιμής
        System.out.println("Ισοζύγιο: " + budget.surplusdeficitFinder(budget.totalRevenue(), budget.totalExpenditure()));
        System.out.println();
    }

    /**
     * Βοηθητική μέθοδος που εκτυπώνει μια συγκεκριμένη ενότητα (λίστα κονδυλίων).
     * <p>
     * Διαμορφώνει τα δεδομένα σε μορφή πίνακα με στήλες: Κώδικας | Ονομασία | Ποσό.
     * </p>
     *
     * @param title Ο τίτλος της ενότητας (π.χ. "ΕΣΟΔΑ").
     * @param items Η λίστα των αντικειμένων {@link BudgetItem} που ανήκουν στην ενότητα.
     */
    public void printSection(String title, List<BudgetItem> items) {
        System.out.println(title);
        System.out.println("Κώδικας | Ονομασία | Ποσό");
        System.out.println("------------------------------------------------");

        items.stream()
                .forEach(item -> {
                    System.out.println(
                        item.getCode() + " | " +
                        item.getName() + " | " +
                        item.getAmount()
                    );
                });

        System.out.println();
    }
}