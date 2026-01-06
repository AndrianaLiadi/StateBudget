package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HomeScreenTest {

    private HomeScreen homeScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        homeScreen = new HomeScreen(controller);
    }

    @Test
    void testHomeScreenInitialization() {
        assertNotNull(homeScreen);
        assertTrue(homeScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) homeScreen.getLayout();
        assertEquals(3, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    @Test
    void testContainsTitleLabel() {
        JLabel label = findComponent(homeScreen, JLabel.class);

        assertNotNull(label);
        assertEquals("Καλώς ήρθες στο Home", label.getText());
        assertEquals(SwingConstants.CENTER, label.getHorizontalAlignment());
    }

    @Test
    void testBudgetButtonExists() {
        JButton budgetButton = findButtonByText(homeScreen, "Δες Προϋπολογισμό");
        assertNotNull(budgetButton);
    }

    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText(homeScreen, "Back");
        assertNotNull(backButton);
    }

    @Test
    void testBudgetButtonActionDoesNotThrow() {
        JButton budgetButton = findButtonByText(homeScreen, "Δες Προϋπολογισμό");

        assertNotNull(budgetButton);
        assertDoesNotThrow(budgetButton::doClick);
    }

    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText(homeScreen, "Back");

        assertNotNull(backButton);
        assertDoesNotThrow(backButton::doClick);
    }



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
