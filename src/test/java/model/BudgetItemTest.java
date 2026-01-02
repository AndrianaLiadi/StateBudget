package test.java.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BudgetItemTest {

    private BudgetItem parent;
    private BudgetItem child1;
    private BudgetItem child2;

    @BeforeEach
    void setUp() {
        parent = new BudgetItem("P01", "Parent", "EXPENDITURE", 1000);
        child1 = new BudgetItem("C01", "Child 1", "EXPENDITURE", 500);
        child2 = new BudgetItem("C02", "Child 2", "EXPENDITURE", 300);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("P01", parent.getCode());
        assertEquals("Parent", parent.getName());
        assertEquals("EXPENDITURE", parent.getType());
        assertEquals(1000, parent.getAmount());
        assertTrue(parent.getSubItems().isEmpty());
    }

    @Test
    void testAddSubItem() {
        parent.addSubItem(child1);
        assertEquals(1, parent.getSubItems().size());
        assertEquals(parent, child1.getParentCategory());
    }

    @Test
    void testUpdateAmount() {
        parent.updateAmount(2000);
        assertEquals(2000, parent.getAmount());
    }

    @Test
    void testGetTotal() {
        parent.addSubItem(child1);
        parent.addSubItem(child2);
        assertEquals(1800.0, parent.getTotal());
    }

    @Test
    void testGetTotalNoChildren() {
        assertEquals(1000.0, parent.getTotal());
    }

    @Test
    void testClone() {
        parent.addSubItem(child1);
        BudgetItem cloned = parent.clone();

        assertNotNull(cloned);
        assertNotSame(parent, cloned);
        assertNotSame(parent.getSubItems(), cloned.getSubItems());
        assertEquals(parent.getAmount(), cloned.getAmount());
        assertEquals(parent.getSubItems().size(), cloned.getSubItems().size());
        
        BudgetItem clonedChild = cloned.getSubItems().get(0);
        assertNotSame(child1, clonedChild);
        assertEquals(cloned, clonedChild.getParentCategory());
    }
}