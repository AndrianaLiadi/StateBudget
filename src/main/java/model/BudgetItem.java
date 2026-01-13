package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση BudgetItem αναπαριστά ένα μεμονωμένο κονδύλιο (εγγραφή) του κρατικού προϋπολογισμού.
 * <p>
 * Κάθε αντικείμενο περιέχει βασικές πληροφορίες όπως κωδικό, όνομα και ποσό, καθώς και τον τύπο του
 * (Έσοδο/Έξοδο). Η κλάση υποστηρίζει δενδρική (ιεραρχική) δομή, επιτρέποντας σε ένα κονδύλιο
 * να έχει υποκατηγορίες (subItems) και γονική κατηγορία (parentCategory).
 * </p>
 * <p>
 * Υλοποιεί τη διεπαφή {@link Cloneable} για να επιτρέπει τη δημιουργία αντιγράφων (deep copies),
 * κάτι που είναι απαραίτητο για τη δημιουργία ανεξάρτητων σεναρίων.
 * </p>
 */
public class BudgetItem implements Cloneable {

    private String code;
    private String name;
    private String type;
    private long amount;
    private BudgetItem parentCategory;
    private List<BudgetItem> subItems;

    /**
     * Κατασκευαστής για τη δημιουργία ενός νέου κονδυλίου.
     *
     * @param code   Ο μοναδικός κωδικός του κονδυλίου (π.χ. "1000").
     * @param name   Η ονομασία/περιγραφή του κονδυλίου.
     * @param type   Ο τύπος του κονδυλίου ("REVENUE" ή "EXPENDITURE").
     * @param amount Το χρηματικό ποσό.
     */
    public BudgetItem(String code, String name, String type, long amount) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.subItems = new ArrayList<>();
    }

    /**
     * Επιστρέφει τον κωδικό του κονδυλίου.
     * @return Ο κωδικός ως String.
     */
    public String getCode() {
        return code;
    }

    /**
     * Επιστρέφει το όνομα του κονδυλίου.
     * @return Το όνομα ως String.
     */
    public String getName() {
        return name;
    }

    /**
     * Επιστρέφει τον τύπο του κονδυλίου.
     * @return Ο τύπος ("REVENUE" ή "EXPENDITURE").
     */
    public String getType() {
        return type;
    }

    /**
     * Επιστρέφει το ποσό του κονδυλίου.
     * @return Το ποσό ως long.
     */
    public long getAmount() {  
        return amount;
    }

    /**
     * Επιστρέφει το γονικό κονδύλιο (αν υπάρχει).
     * @return Το αντικείμενο γονέας ή {@code null} αν είναι ριζικό στοιχείο.
     */
     public BudgetItem getParentCategory() {
        return parentCategory;
    }

    /**
     * Επιστρέφει τη λίστα με τις υποκατηγορίες (υπο-κονδύλια).
     * @return Μια λίστα με αντικείμενα {@link BudgetItem}.
     */
    public List<BudgetItem> getSubItems() {
        return subItems;
    }

    /**
     * Ενημερώνει το ποσό του κονδυλίου.
     * @param newValue Η νέα τιμή του ποσού.
     */
    public void updateAmount(long newValue) {
        this.amount = newValue;
    }

    /**
     * Προσθέτει ένα υπο-κονδύλιο στη λίστα των subItems και ορίζει το τρέχον αντικείμενο ως γονέα.
     * @param newItem Το νέο κονδύλιο που θα προστεθεί ως παιδί.
     */
    public void addSubItem(BudgetItem newItem) {
        newItem.parentCategory = this;
        subItems.add(newItem);
    }

    /**
     * Υπολογίζει το συνολικό άθροισμα του κονδυλίου και όλων των υποκατηγοριών του αναδρομικά.
     * @return Το συνολικό ποσό (τρέχον ποσό + ποσά υποκατηγοριών).
     */
    public double getTotal() {
        double total = this.amount;
        for (BudgetItem i: subItems){
            total = total + i.getTotal();
        }
        return total;
    }

    /**
     * Δημιουργεί ένα βαθύ αντίγραφο (Deep Copy) του κονδυλίου.
     * <p>
     * Κλωνοποιεί το ίδιο το αντικείμενο αλλά και αναδρομικά όλη τη λίστα των subItems.
     * Αυτό διασφαλίζει ότι οι αλλαγές στο αντίγραφο δεν επηρεάζουν το πρωτότυπο.
     * </p>
     *
     * @return Ένα νέο, ανεξάρτητο αντικείμενο {@link BudgetItem}.
     */
    @Override
    public BudgetItem clone(){
        try {
            BudgetItem cloned = (BudgetItem) super.clone();
            cloned.subItems = new ArrayList<>();
            for (BudgetItem sub:this.subItems) {
                BudgetItem subClone = sub.clone();
                subClone.parentCategory = cloned;
                cloned.subItems.add(subClone);
            }
            return cloned;
        }  catch (CloneNotSupportedException e){
            System.out.println("Clone Error");
            return null;
        }
    }

    /**
     * Επιστρέφει μια αναπαράσταση του κονδυλίου σε μορφή κειμένου.
     * <p>
     * Συνήθως επιστρέφει τη μορφή "Όνομα (Κωδικός)".
     * </p>
     * @return Το String αναπαράστασης.
     */
    @Override
    public String toString() {
        if (name != null && !name.isBlank()) {
            return name + " (" + code + ")";
        }
        return code != null ? code : super.toString();
    }
}