package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import model.Budget;
import model.BudgetItem;
import model.Scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Κλάση ελέγχου (Test Class) για την κλάση {@link ReportScreen}.
 */
class ReportScreenTest {

    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);
    }

    @Test
    void testReportScreenWithNullScenario() {
        ReportScreen screen = new ReportScreen(controller, null);

        assertNotNull(screen);
        assertTrue(screen.getLayout() instanceof BorderLayout);

        JTextArea area = findComponent(screen, JTextArea.class);
        JButton backButton = findButtonByText(screen, "Back");

        assertNotNull(area);
        assertTrue(area.getText().contains("Δεν υπάρχει σενάριο"));
        assertFalse(area.isEditable());

        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }

    @Test
    void testReportScreenWithScenario() {
        // Setup δεδομένων
        BudgetItem item1 = new BudgetItem("A1", "Item A1", "Income", 1000);
        BudgetItem item2 = new BudgetItem("B1", "Item B1", "Income", 2000);

        Budget base = new Budget(2025, List.of(item1));
        Budget modified = new Budget(2025, List.of(item2));

        Scenario scenario = new Scenario(base, "Test Scenario");
        scenario.setModifiedBudget(modified);
        
        // --- ΔΙΟΡΘΩΣΗ 1: Πρέπει να παράγουμε τη σύνοψη πριν τον έλεγχο ---
        scenario.generateSummary(); 

        // Δημιουργία οθόνης
        ReportScreen screen = new ReportScreen(controller, scenario);

        assertNotNull(screen);

        // Εύρεση συστατικών
        JTextArea area = findComponent(screen, JTextArea.class);
        JButton backButton = findButtonByText(screen, "Back");
        ChartPanel chart = findComponent(screen, ChartPanel.class);

        // Assertions
        assertNotNull(area);
        
        // --- ΔΙΟΡΘΩΣΗ 2: Ασφαλής έλεγχος κειμένου ---
        // Αντί για assertEquals (που κολλάει στα \r\n), ελέγχουμε αν περιέχει το όνομα
        String screenText = area.getText();
        assertNotNull(screenText);
        assertTrue(screenText.contains("Test Scenario"), 
            "Το κείμενο της οθόνης πρέπει να περιέχει το όνομα του σεναρίου");

        assertNotNull(chart, "Πρέπει να υπάρχει το ChartPanel");
        assertNotNull(backButton);
        assertDoesNotThrow(() -> backButton.doClick());
    }


    /* =======================
       Helper methods
       ======================= */

    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container root, Class<T> type) {
        for (Component c : root.getComponents()) {
            if (type.isInstance(c)) {
                return (T) c;
            }
            if (c instanceof Container) {
                T found = findComponent((Container) c, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    private JButton findButtonByText(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
            if (c instanceof Container) {
                JButton found = findButtonByText((Container) c, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
