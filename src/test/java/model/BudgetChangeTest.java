package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BudgetChangeTest {

    @Test
    public void testBasicFields() {
        BudgetChange bc = new BudgetChange("A1", "Test Item", 100, 150, "Expense");
        
        assertEquals("A1", bc.getItemCode());
        assertEquals("Test Item", bc.getItemName());
        assertEquals("Expense", bc.getType());
        assertEquals(100, bc.getOldValue());
        assertEquals(150, bc.getNewValue());
    }

    @Test
    public void testCalcDifference() {
        BudgetChange bc1 = new BudgetChange("C1", "Item", 200, 300, "Type");
        assertEquals(100, bc1.getDifference());

        BudgetChange bc2 = new BudgetChange("C2", "Item", 500, 200, "Type");
        assertEquals(-300, bc2.getDifference());
    }

    @Test
    public void testPercents() {
        BudgetChange bc = new BudgetChange("P1", "Item", 100, 150, "T");
        assertEquals(50.0, bc.getPercentageChange());

        BudgetChange bc2 = new BudgetChange("P2", "Item", 100, 50, "T");
        assertEquals(-50.0, bc2.getPercentageChange());
    }

    @Test
    public void testZeroValueCases() {
        BudgetChange zeroOld = new BudgetChange("Z1", "Item", 0, 50, "T");
        assertEquals(100.0, zeroOld.getPercentageChange());

        BudgetChange bothZero = new BudgetChange("Z2", "Item", 0, 0, "T");
        assertEquals(0.0, bothZero.getPercentageChange());
    }

    @Test
    public void testUpdateValue() {
        BudgetChange bc = new BudgetChange("U1", "Item", 100, 150, "T");
        bc.setNewValue(200);
        
        assertEquals(200, bc.getNewValue());
        assertEquals(100, bc.getDifference());
        assertEquals(100.0, bc.getPercentageChange());
    }
}