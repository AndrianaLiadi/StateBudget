package logic;

import data.BudgetDataLoader;
import model.Budget;
import model.BudgetItem;
import model.BudgetChange;
import model.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση BudgetService περιέχει την επιχειρησιακή λογική για τη διαχείριση των προϋπολογισμών.
 * <p>
 * Παρέχει λειτουργίες για τη φόρτωση δεδομένων, τον υπολογισμό ελλειμμάτων/πλεονασμάτων,
 * τη σύγκριση μεταξύ διαφορετικών προϋπολογισμών και την παραγωγή αναφορών ανάλυσης σεναρίων.
 * </p>
 */
public class BudgetService {
    
    /** Αντικείμενο για τη φόρτωση δεδομένων από αρχεία. */
    private final BudgetDataLoader objloader;
    
    /** Αντικείμενο για τη δημιουργία κειμενικών αναφορών. */
    private final ReportGenerator reportGenerator;

    /**
     * Κατασκευαστής της υπηρεσίας BudgetService.
     * * @param objloader Ο loader που θα χρησιμοποιηθεί για την ανάγνωση των δεδομένων.
     * @param reportGenerator Η γεννήτρια που θα χρησιμοποιηθεί για την παραγωγή αναφορών.
     */
    public BudgetService(BudgetDataLoader objloader, ReportGenerator reportGenerator) {
        this.objloader = objloader;
        this.reportGenerator = reportGenerator;
    }

    /**
     * Φορτώνει έναν προϋπολογισμό από ένα αρχείο CSV για το συγκεκριμένο έτος.
     * * @param year Το έτος του προϋπολογισμού.
     * @param filePath Η διαδρομή του αρχείου CSV.
     * @return Ένα αντικείμενο {@link Budget} με τα φορτωμένα δεδομένα.
     * @throws RuntimeException Αν υπάρξει σφάλμα κατά την ανάγνωση του αρχείου.
     */
    public Budget loadBudget(int year, String filePath) {
        try {
            return objloader.loadFromCSV(filePath, year);
        } catch (Exception e) {
            throw new RuntimeException("Σφάλμα κατά τη φόρτωση του προϋπολογισμού (" + filePath + ")", e);
        }
    }

    /**
     * Υπολογίζει το δημοσιονομικό αποτέλεσμα (Πλεόνασμα ή Έλλειμμα).
     * <p>
     * Αφαιρεί το σύνολο των εξόδων από το σύνολο των εσόδων.
     * </p>
     * * @param budget Ο προϋπολογισμός προς υπολογισμό.
     * @return Το ποσό του αποτελέσματος (θετικό για πλεόνασμα, αρνητικό για έλλειμμα).
     */
    public double calculateSurplusOrDeficit(Budget budget) {
        return budget.totalRevenue() - budget.totalExpenditure();
    }

    /**
     * Συγκρίνει δύο προϋπολογισμούς και εντοπίζει τις διαφορές τους.
     * <p>
     * Η μέθοδος ελέγχει για αλλαγές στα ποσά, διαγραφές κονδυλίων (υπάρχουν στο A αλλά όχι στο B)
     * και προσθήκες νέων κονδυλίων (υπάρχουν στο B αλλά όχι στο A).
     * </p>
     * * @param budgetA Ο βασικός προϋπολογισμός (Base).
     * @param budgetB Ο συγκριτικός προϋπολογισμός (Σενάριο).
     * @return Μια λίστα από αντικείμενα {@link BudgetChange} που περιγράφουν τις διαφορές.
     */
    public List<BudgetChange> compareBudgets(Budget budgetA, Budget budgetB) {
        List<BudgetChange> differences = new ArrayList<>();
        List<String> processedCodes = new ArrayList<>(); 
        
        // Έλεγχος των αντικειμένων του budgetA
        for (BudgetItem itemA : budgetA.getItems()) {
            String code = itemA.getCode();
            processedCodes.add(code);
            
            BudgetItem itemB = budgetB.getItemByCode(code); 
            long amountA = itemA.getAmount();
            
            // Έλεγχος αν διαγράφηκε το κονδύλιο (αν δεν υπάρχει στο B, το ποσό είναι 0)
            long amountB = (itemB != null) ? itemB.getAmount() : 0L;
            
            if (amountA != amountB) {
                BudgetChange change = new BudgetChange(code, itemA.getName(), amountA, amountB, itemA.getType());
                differences.add(change);
            }
        }
        
        // Έλεγχος των αντικειμένων του budgetB για νέες προσθήκες
        for (BudgetItem itemB : budgetB.getItems()) {
            String code = itemB.getCode();
            
            // Αν δεν έχει επεξεργαστεί ήδη (άρα δεν υπήρχε στο A)
            if (!processedCodes.contains(code)) {
                long amountA = 0L;
                long amountB = itemB.getAmount();
                BudgetChange change = new BudgetChange(code, itemB.getName(), amountA, amountB, itemB.getType());
                differences.add(change);
            }
        }
        return differences;
    }

    /**
     * Αναλύει τον αντίκτυπο ενός σεναρίου δημιουργώντας μια πλήρη αναφορά.
     * <p>
     * Συγκρίνει τον βασικό με τον τροποποιημένο προϋπολογισμό, καταγράφει τις αλλαγές
     * σε ένα αντικείμενο {@link Scenario} και παράγει το τελικό κείμενο της αναφοράς.
     * </p>
     * * @param base Ο βασικός προϋπολογισμός.
     * @param modified Ο τροποποιημένος προϋπολογισμός.
     * @param scenarioName Το όνομα του σεναρίου.
     * @return Ένα String που περιέχει την αναλυτική αναφορά των αποτελεσμάτων.
     */
    public String analyzeImpact(Budget base, Budget modified, String scenarioName) {
        // Δημιουργία σεναρίου
        Scenario scenario = new Scenario(base, scenarioName);
        
        // Εύρεση διαφορών
        List<BudgetChange> changes = compareBudgets(base, modified);
        
        // Ανάθεση αλλαγών στο σενάριο
        scenario.setChanges(changes);
        scenario.generateSummary(); 
        
        // Επιστροφή αναφοράς μέσω του ReportGenerator
        return reportGenerator.generateSummary(scenario, changes);
    }
}