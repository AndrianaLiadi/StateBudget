package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BudgetTest {

    private Budget budget;
    private List<BudgetItem> items;

    @BeforeEach
    void setUp() {
        items = new ArrayList<>();
        items.add(new BudgetItem("R01", "REVENUE", 1000));
        items.add(new BudgetItem("R02", "REVENUE", 500));
        items.add(new BudgetItem("E01", "EXPENDITURE", 800));
        items.add(new BudgetItem("E02", "EXPENDITURE", 200));

        budget = new Budget(2023, items);
    }

    @Test
    void testTotalRevenue() {
        assertEquals(1500, budget.totalRevenue());
    }

    @Test
    void testTotalExpenditure() {
        assertEquals(1000, budget.totalExpenditure());
    }

    @Test
    void testSurplusdeficitFinder() {
        double rev = budget.totalRevenue();
        double exp = budget.totalExpenditure();
        
        assertEquals(500, budget.surplusdeficitFinder(rev, exp));
        assertEquals(500, budget.surplusdeficitFinder(1000, 1500));
    }

    @Test
    void testGetItemsByType() {
        List<BudgetItem> revenues = budget.getItemsByType("REVENUE");
        assertEquals(2, revenues.size());
        
        List<BudgetItem> none = budget.getItemsByType("NON_EXISTENT");
        assertTrue(none.isEmpty());
    }

    @Test
    void testGetItemByCode() {
        BudgetItem item = budget.getItemByCode("R01");
        assertNotNull(item);
        assertEquals("REVENUE", item.getType());
        assertNull(budget.getItemByCode("UNKNOWN"));
    }

    @Test
    void testClone() {
        Budget clonedBudget = budget.clone();

        assertNotNull(clonedBudget);
        assertNotSame(budget, clonedBudget);
        assertEquals(budget.getYear(), clonedBudget.getYear());
        assertEquals(budget.getItems().size(), clonedBudget.getItems().size());
        assertNotSame(budget.getItems().get(0), clonedBudget.getItems().get(0));
    }
}