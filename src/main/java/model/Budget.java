package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση Budget αναπαριστά τον κρατικό προϋπολογισμό για ένα συγκεκριμένο οικονομικό έτος.
 * <p>
 * Περιέχει τη λίστα όλων των κονδυλίων (Εσόδων και Εξόδων) και παρέχει μεθόδους
 * για τον υπολογισμό συνόλων, την εύρεση πλεονάσματος/ελλείμματος και τη διαχείριση
 * των δεδομένων. Υλοποιεί το {@link Cloneable} για τη δημιουργία αντιγράφων (Deep Copy),
 * ώστε να μπορούν να δημιουργηθούν σενάρια χωρίς να αλλοιώνεται ο αρχικός προϋπολογισμός.
 * </p>
 */
public class Budget implements Cloneable {

    /** Η λίστα με τα κονδύλια (αντικείμενα {@link BudgetItem}) του προϋπολογισμού. */
    private List<BudgetItem> items;
    
    /** Το οικονομικό έτος του προϋπολογισμού. */
    private int year;

    /**
     * Κατασκευαστής της κλάσης Budget.
     *
     * @param year  Το οικονομικό έτος.
     * @param items Η αρχική λίστα των κονδυλίων.
     */
    public Budget(int year, List<BudgetItem> items){
        this.items = items;
        this.year = year;
    }

    /**
     * Επιστρέφει τη λίστα με όλα τα κονδύλια του προϋπολογισμού.
     *
     * @return Η λίστα των {@link BudgetItem}.
     */
    public List<BudgetItem> getItems() {
        return this.items;
    }

    /**
     * Βοηθητική μέθοδος που επιστρέφει τη λίστα των κονδυλίων (όμοια με την getItems).
     *
     * @return Η λίστα των {@link BudgetItem}.
     */
    public List<BudgetItem> listMaker() {
        return this.getItems();
    }

    /**
     * Επιστρέφει το οικονομικό έτος του προϋπολογισμού.
     *
     * @return Το έτος ως ακέραιος.
     */
    public int getYear() {
        return year; 
    }

    /**
     * Υπολογίζει το συνολικό ποσό των Εσόδων (REVENUE).
     * <p>
     * Διατρέχει όλα τα κονδύλια και αθροίζει τα ποσά όσων έχουν τύπο "REVENUE".
     * </p>
     *
     * @return Το συνολικό ποσό των εσόδων.
     */
    public long totalRevenue(){
        long trevenue = 0;
        // Η λίστα revenue γεμίζει αλλά δεν επιστρέφεται, χρησιμοποιείται μόνο για τον υπολογισμό
        List<BudgetItem> revenue = new ArrayList<>();
        for (BudgetItem item : this.items) {
            if (item.getType().equals("REVENUE")) {
                revenue.add(item);
                trevenue = trevenue + item.getAmount();
            }
        }
        return trevenue;
    }

    /**
     * Υπολογίζει το συνολικό ποσό των Εξόδων (EXPENDITURE).
     * <p>
     * Διατρέχει όλα τα κονδύλια και αθροίζει τα ποσά όσων έχουν τύπο "EXPENDITURE".
     * </p>
     *
     * @return Το συνολικό ποσό των εξόδων.
     */
    public double totalExpenditure(){
        long texpenditure = 0;
        List<BudgetItem> expenditure = new ArrayList<>();
        for (BudgetItem item : this.items){
            if (item.getType().equals("EXPENDITURE")){
                expenditure.add(item);
                texpenditure = texpenditure + item.getAmount();
            }
        }
        return texpenditure;
    }

    /**
     * Υπολογίζει και εκτυπώνει το Πλεόνασμα ή το Έλλειμμα.
     * <p>
     * Συγκρίνει τα συνολικά έσοδα με τα συνολικά έξοδα.
     * </p>
     *
     * @param trevenue     Το σύνολο των εσόδων.
     * @param texpenditure Το σύνολο των εξόδων.
     * @return Η απόλυτη τιμή της διαφοράς (το ποσό του πλεονάσματος ή του ελλείμματος).
     */
    public double surplusdeficitFinder(double trevenue, double texpenditure){
        if (trevenue > texpenditure){
            double surplus = trevenue - texpenditure;
            System.out.println("The surplus is" + surplus);
            return surplus;
        } else {
            double deficit = texpenditure - trevenue;
            System.out.println("The deficit is " + deficit);
            return deficit;
        }
    }

    /**
     * Επιστρέφει μια λίστα κονδυλίων φιλτραρισμένη με βάση τον τύπο τους.
     *
     * @param type Ο τύπος προς αναζήτηση (π.χ. "REVENUE", "EXPENDITURE").
     * @return Μια νέα λίστα που περιέχει μόνο τα κονδύλια του συγκεκριμένου τύπου.
     */
    public List<BudgetItem> getItemsByType(String type) {
        List<BudgetItem> filteredItems = new ArrayList<>();
        for (BudgetItem item : items) {
            if (item.getType() != null && item.getType().equals(type)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    /**
     * Αναζητά ένα συγκεκριμένο κονδύλιο με βάση τον μοναδικό κωδικό του.
     *
     * @param code Ο κωδικός του κονδυλίου (π.χ. "1110103").
     * @return Το αντικείμενο {@link BudgetItem} αν βρεθεί, διαφορετικά {@code null}.
     */
    public BudgetItem getItemByCode(String code) {
        for (BudgetItem item : this.items) {
            if (item.getCode().equals(code)) {
            return item;
            }
        }
        return null;
    }

    /**
     * Δημιουργεί ένα βαθύ αντίγραφο (Deep Copy) του προϋπολογισμού.
     * <p>
     * Αυτή η μέθοδος είναι κρίσιμη για τη δημιουργία σεναρίων. Διασφαλίζει ότι
     * όταν τροποποιούμε το σενάριο, δεν αλλάζουν τα δεδομένα στον αρχικό (Base) προϋπολογισμό.
     * Αντιγράφεται τόσο το αντικείμενο Budget όσο και η λίστα των αντικειμένων μέσα σε αυτό.
     * </p>
     *
     * @return Ένα νέο, ανεξάρτητο αντικείμενο Budget με τα ίδια δεδομένα.
     */
    @Override
    public Budget clone() {
        try {
            Budget cloned = (Budget) super.clone();

            // Δημιουργία νέας λίστας και κλωνοποίηση κάθε αντικειμένου ξεχωριστά
            cloned.items = new ArrayList<>();
            for (BudgetItem item : this.items) {
                cloned.items.add(item.clone());
            }

            return cloned;

        } catch (CloneNotSupportedException e) {
            System.out.println("Clone Error");
            return null;
        }
    }
}