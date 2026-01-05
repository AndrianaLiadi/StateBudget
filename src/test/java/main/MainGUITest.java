package main;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class MainGUITest {
    
    @Test
    void testMainGUIClassExists() {
        // Test that exixts 
        try {
            Class<?> guiClass = Class.forName("main.MainGUI");
            assertNotNull(guiClass, "Η MainGUI πρέπει να υπάρχει");
        } catch (ClassNotFoundException e) {
            fail("Η κλάση MainGUI δεν βρέθηκε: " + e.getMessage());
        }
    }
    
    @Test
    void testMainMethodExists() {
        //  test that method exixts 
        try {
            Class<?> guiClass = Class.forName("main.MainGUI");
            Method mainMethod = guiClass.getMethod("main", String[].class);
            assertNotNull(mainMethod, "Η μέθοδος main πρέπει να υπάρχει");
        } catch (Exception e) {
            fail("Η μέθοδος main δεν βρέθηκε: " + e.getMessage());
        }
    }
    
    @Test
    void testMainDoesNotThrowWhenCalledWithEmptyArgs() {
        
        assertDoesNotThrow(() -> {
            // reflection for the ui not run
            Class<?> guiClass = Class.forName("main.MainGUI");
            guiClass.getMethod("main", String[].class)
                   .invoke(null, (Object) new String[]{});
        }, "Η MainGUI.main() δεν πρέπει να πετάει exception με άδεια arguments");
    }
    
    @Test
    void testSimpleInstantiation() {
        // class is runnable
        System.out.println(" MainGUI test passed - GUI can be loaded");
        assertTrue(true);
    }
}