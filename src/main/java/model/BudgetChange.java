package model;

/**
 * Η κλάση BudgetChange αναπαριστά μια μεμονωμένη τροποποίηση σε ένα κονδύλιο του προϋπολογισμού.
 * <p>
 * Χρησιμοποιείται για να αποθηκεύσει την κατάσταση "Πριν" (Base) και "Μετά" (Scenario)
 * μιας εγγραφής, επιτρέποντας τον εύκολο υπολογισμό της νομισματικής διαφοράς
 * και της ποσοστιαίας μεταβολής.
 * </p>
 */
public class BudgetChange {

    private String itemCode;
    private String itemName; 
    private String type;
    private long oldValue;
    private long newValue;

    /**
     * Κατασκευαστής για τη δημιουργία μιας νέας εγγραφής αλλαγής.
     *
     * @param itemCode Ο μοναδικός κωδικός του κονδυλίου (π.χ. "1110103").
     * @param itemName Η περιγραφή/όνομα του κονδυλίου.
     * @param oldValue Η αρχική τιμή στον βασικό προϋπολογισμό.
     * @param newValue Η νέα τιμή στο σενάριο.
     * @param type     Ο τύπος του κονδυλίου ("REVENUE" ή "EXPENDITURE").
     */
    public BudgetChange(String itemCode, String itemName, long oldValue, long newValue, String type) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    /**
     * Επιστρέφει τον κωδικό του κονδυλίου.
     * @return Ο κωδικός ως String.
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * Επιστρέφει το όνομα/περιγραφή του κονδυλίου.
     * @return Το όνομα ως String.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Επιστρέφει την παλιά τιμή (από τον βασικό προϋπολογισμό).
     * @return Το ποσό πριν την αλλαγή.
     */
    public long getOldValue() {
        return oldValue;
    }

    /**
     * Επιστρέφει τη νέα τιμή (του σεναρίου).
     * @return Το ποσό μετά την αλλαγή.
     */
    public long getNewValue() {
        return newValue;
    }

    /**
     * Υπολογίζει την απόλυτη νομισματική διαφορά.
     * <p>
     * Τύπος: {@code NewValue - OldValue}.
     * </p>
     *
     * @return Θετική τιμή για αύξηση, αρνητική για μείωση.
     */
    public long getDifference() {
        return newValue - oldValue;
    }
    
    /**
     * Επιστρέφει τον τύπο του κονδυλίου (Έσοδο ή Έξοδο).
     * @return Ο τύπος ως String.
     */
    public String getType() {
        return type;
    }

    /**
     * Υπολογίζει την ποσοστιαία μεταβολή της τιμής.
     * <p>
     * Διαχειρίζεται την περίπτωση όπου η αρχική τιμή είναι 0 για να αποφύγει
     * τη διαίρεση με το μηδέν (επιστρέφει 100% αν η νέα τιμή δεν είναι 0).
     * </p>
     *
     * @return Το ποσοστό μεταβολής (π.χ. 50.0 για αύξηση 50%).
     */
    public double getPercentageChange() {
        if (oldValue == 0) {
            return newValue != 0 ? 100.0 : 0.0;
        }
        return ((double)(newValue - oldValue) / oldValue) * 100.0;
    }

    /**
     * Ορίζει μια νέα τιμή για το κονδύλιο στο σενάριο.
     * @param newValue Το νέο ποσό.
     */
    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }
}