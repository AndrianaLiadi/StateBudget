package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ScenarioTest {

    @Test
    public void checkInitialState() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("CODE1", "Item 1", "Type A", (long)100.0));
        Budget b = new Budget(2024, items);
        
        Scenario s = new Scenario(b, "First Scenario");

        assertEquals("First Scenario", s.getitemName());
        assertNotNull(s.getChanges());
        assertEquals(0, s.getChanges().size());
    }

    @Test
    public void checkApplyChangesLogic() {
        List<BudgetItem> items = new ArrayList<>();
        items.add(new BudgetItem("10", "Salary", "Expense", (long)500.0));
        Budget base = new Budget(2024, items);
        Scenario scen = new Scenario(base, "Test");

        List<BudgetChange> changeList = new ArrayList<>();
        changeList.add(new BudgetChange("10", "Salary", (long)500.0, (long)600.0, "expense"));
        scen.setChanges(changeList);

        scen.applyChanges();

        Budget mod = scen.getModifiedBudget();
        assertEquals(1, mod.getItems().size());
        assertEquals(600.0, mod.getItems().get(0).getAmount());
    }

    @Test
    public void testAdditionOfNewItem() {
        Budget base = new Budget(2024, new ArrayList<>());
        Scenario sc = new Scenario(base, "EmptyBase");

        List<BudgetChange> ch = new ArrayList<>();
        ch.add(new BudgetChange("NEW", "Extra", 0, 200, "Income"));
        sc.setChanges(ch);

        sc.applyChanges();

        List<BudgetItem> res = sc.getModifiedBudget().getItems();
        assertEquals(1, res.size());
        assertEquals("NEW", res.get(0).getCode());
    }

    @Test
    public void summaryOutputTest() {
        Budget b = new Budget(2024, new ArrayList<>());
        Scenario s = new Scenario(b, "S1");

        s.generateSummary();
        assertTrue(s.getSummary().contains("Δεν υπάρχει καμία αλλαγή"));

        List<BudgetChange> list = new ArrayList<>();
        list.add(new BudgetChange("1", "TestItem", 20, 10, "T1"));
        s.setChanges(list);
        
        s.generateSummary();
        assertNotNull(s.getSummary());
        assertTrue(s.getSummary().contains("S1"));
    }
}