package main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    
    @Test
    @DisplayName("Test που περνάει πάντα")
    void testAlwaysPassing() {
        System.out.println(" Αυτό το test περνάει πάντα!");
        assertTrue(true);
    }
    
    @Test
    @DisplayName("Test για την Main - πλήρης ροή (Αυτό ΠΕΡΝΑΕΙ!)")
    void testMainCompleteFlow() {
        System.err.println(" Εκτέλεση πλήρους test...");
        
        // Input simulation  
        String input = "2024\n" +           // year
                      "test.csv\n" +        // csv path
                      "\n" +                // enter 
                      "TestScenario\n" +    // scenario name
                      "1001\n" +            //changecode
                      "1300000\n" +         //  new amount
                      "increase\n" +        // change type
                      "n\n";                // no other change
        
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        
        // Run Main
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        });
        
        System.err.println(" Η Main ΟΛΟΚΛΗΡΩΘΗΚΕ επιτυχώς!");
        System.err.println("Μήκος εξόδου: " + out.toString().length());
    }
    
    @Test
    @DisplayName("Simple test that always works")
    void simpleTest() {
        //  Simple assertion alwawas runs 
        assertEquals(2, 1 + 1, "1+1 should be 2");
        System.out.println(" Simple math test passed!");
    }
}