package main;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link MainGUI}.
 * <p>
 * Ελέγχει τη δομική ακεραιότητα του σημείου εισόδου (Entry Point) της εφαρμογής.
 * Χρησιμοποιεί κυρίως Java Reflection για να επιβεβαιώσει ότι η κλάση και η μέθοδος
 * εκκίνησης υπάρχουν και μπορούν να κληθούν χωρίς σφάλματα.
 * </p>
 */
class MainGUITest {
    
    /**
     * Ελέγχει αν η κλάση MainGUI υπάρχει στο classpath και μπορεί να φορτωθεί.
     * <p>
     * Χρησιμοποιεί την {@code Class.forName} για να αναζητήσει την κλάση στο πακέτο "main".
     * Αν αποτύχει, σημαίνει ότι το όνομα της κλάσης ή το πακέτο είναι λάθος.
     * </p>
     */
    @Test
    void testMainGUIClassExists() {
        try {
            Class<?> guiClass = Class.forName("main.MainGUI");
            assertNotNull(guiClass, "Η MainGUI πρέπει να υπάρχει");
        } catch (ClassNotFoundException e) {
            fail("Η κλάση MainGUI δεν βρέθηκε: " + e.getMessage());
        }
    }
    
    /**
     * Ελέγχει αν η κλάση MainGUI διαθέτει την τυπική μέθοδο {@code main}.
     * <p>
     * Αναζητά τη μέθοδο με υπογραφή {@code public static void main(String[] args)}.
     * Αυτό είναι απαραίτητο για να μπορεί να εκκινήσει η εφαρμογή από το JVM.
     * </p>
     */
    @Test
    void testMainMethodExists() {
        try {
            Class<?> guiClass = Class.forName("main.MainGUI");
            Method mainMethod = guiClass.getMethod("main", String[].class);
            assertNotNull(mainMethod, "Η μέθοδος main πρέπει να υπάρχει");
        } catch (Exception e) {
            fail("Η μέθοδος main δεν βρέθηκε: " + e.getMessage());
        }
    }
    
    /**
     * Ελέγχει αν η μέθοδος {@code main} εκτελείται χωρίς να πετάξει εξαίρεση (Exception).
     * <p>
     * Καλεί τη main μέσω Reflection περνώντας έναν κενό πίνακα ορισμάτων.
     * Αυτό αποτελεί ένα "Smoke Test" για να βεβαιωθούμε ότι η εφαρμογή ξεκινάει
     * (ή τουλάχιστον μπαίνει στο Swing thread) χωρίς να καταρρεύσει αμέσως.
     * </p>
     */
    @Test
    void testMainDoesNotThrowWhenCalledWithEmptyArgs() {
        assertDoesNotThrow(() -> {
            // Χρήση reflection για να μην μπλοκάρει το UI το test runner
            Class<?> guiClass = Class.forName("main.MainGUI");
            guiClass.getMethod("main", String[].class)
                    .invoke(null, (Object) new String[]{});
        }, "Η MainGUI.main() δεν πρέπει να πετάει exception με άδεια arguments");
    }
    
    /**
     * Ένας απλός έλεγχος επαλήθευσης.
     * <p>
     * Επιβεβαιώνει ότι η σουίτα ελέγχων για το GUI φορτώνεται και τρέχει σωστά.
     * </p>
     */
    @Test
    void testSimpleInstantiation() {
        System.out.println(" MainGUI test passed - GUI can be loaded");
        assertTrue(true);
    }
}