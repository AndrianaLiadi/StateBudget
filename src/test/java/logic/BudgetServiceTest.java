package logic;

import model.Budget;
import model.BudgetChange;
import model.BudgetItem;
import ui.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BudgetServiceTest {
    private BudgetService budgetService;
    private ReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGenerator();
        budgetService = new BudgetService(null, reportGenerator);
    }
    
}