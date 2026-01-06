package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetScreenTest {

    private BudgetScreen budgetScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        budgetScreen = new BudgetScreen(controller);
    }

    @Test
    void testBudgetScreenInitialization() {
        assertNotNull(budgetScreen);
        assertTrue(budgetScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    @Test
    void testYearComboBoxExistsAndHasYears() {
        JComboBox<?> combo = findComponent(budgetScreen, JComboBox.class);

        assertNotNull(combo);
        assertEquals(7, combo.getItemCount());
        assertEquals(2025, combo.getSelectedItem());
    }

    @Test
    void testTextAreaExistsAndIsNotEditable() {
        JTextArea area = findComponent(budgetScreen, JTextArea.class);

        assertNotNull(area);
        assertFalse(area.isEditable());
    }

    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText(budgetScreen, "Back");

        assertNotNull(backButton);
    }

    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText(budgetScreen, "Back");

        assertNotNull(backButton);
        assertDoesNotThrow(backButton::doClick);
    }

    @Test
    void testChangingYearDoesNotThrow() {
        JComboBox<Integer> combo = findComponent(budgetScreen, JComboBox.class);

        assertNotNull(combo);

        assertDoesNotThrow(() -> {
            combo.setSelectedItem(2019);
            combo.setSelectedItem(2023);
            combo.setSelectedItem(2025);
        });
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
                JButton b = (JButton) c;
                if (text.equals(b.getText())) {
                    return b;
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
