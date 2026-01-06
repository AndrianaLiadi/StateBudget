package ui;

import static org.junit.jupiter.api.Assertions.*;

import model.Budget;
import model.BudgetItem;
import model.Scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class ScenarioScreenTest {

    private AppController controller;
    private Budget baseBudget;
    private ScenarioScreen scenarioScreen;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);


        BudgetItem item1 = new BudgetItem("A1", "Item A1", 1000);
        BudgetItem item2 = new BudgetItem("B1", "Item B1", 2000);
        baseBudget = new Budget(2025, List.of(item1, item2));

        scenarioScreen = new ScenarioScreen(controller, baseBudget);
    }

    @Test
    void testScenarioScreenInitialization() {
        assertNotNull(scenarioScreen);
        assertTrue(scenarioScreen.getLayout() instanceof BorderLayout);
    }

    @Test
    void testComponentsExist() {
        JComboBox<?> comboBox = findComponent(scenarioScreen, JComboBox.class);
        JTextField textField = findComponent(scenarioScreen, JTextField.class);
        JButton addButton = findButtonByText(scenarioScreen, "Προσθήκη αλλαγής");
        JButton runButton = findButtonByText(scenarioScreen, "Εκτέλεση σεναρίου");
        JButton backButton = findButtonByText(scenarioScreen, "Back");
        JList<?> changesList = findComponent(scenarioScreen, JList.class);

        assertNotNull(comboBox);
        assertNotNull(textField);
        assertNotNull(addButton);
        assertNotNull(runButton);
        assertNotNull(backButton);
        assertNotNull(changesList);
    }

    @Test
    void testAddChangeActionDoesNotThrow() {
        JComboBox<BudgetItem> comboBox = findComponent(scenarioScreen, JComboBox.class);
        JTextField textField = findComponent(scenarioScreen, JTextField.class);
        JButton addButton = findButtonByText(scenarioScreen, "Προσθήκη αλλαγής");
        JList<?> changesList = findComponent(scenarioScreen, JList.class);

        assertNotNull(comboBox);
        assertNotNull(textField);
        assertNotNull(addButton);
        assertNotNull(changesList);


        comboBox.setSelectedIndex(0);
        textField.setText("1500");


        assertDoesNotThrow(addButton::doClick);


        assertEquals("", textField.getText());


        DefaultListModel<?> model = (DefaultListModel<?>) changesList.getModel();
        assertEquals(1, model.getSize());
        assertTrue(model.getElementAt(0).toString().contains("A1"));
    }

    @Test
    void testRunScenarioAndBackActionsDoNotThrow() {
        JButton runButton = findButtonByText(scenarioScreen, "Εκτέλεση σεναρίου");
        JButton backButton = findButtonByText(scenarioScreen, "Back");

        assertNotNull(runButton);
        assertNotNull(backButton);

        assertDoesNotThrow(runButton::doClick);
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
