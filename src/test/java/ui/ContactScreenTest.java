package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactScreenTest {

    private ContactScreen contactScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        contactScreen = new ContactScreen(controller);
    }

    @Test
    void testContactScreenInitialization() {
        assertNotNull(contactScreen);
        assertTrue(contactScreen.getLayout() instanceof java.awt.BorderLayout);
    }

    @Test
    void testContainsTextAreaInsideScrollPane() {
        boolean foundScrollPane = false;

        for (var component : contactScreen.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                assertTrue(scroll.getViewport().getView() instanceof JTextArea);

                JTextArea textArea = (JTextArea) scroll.getViewport().getView();
                assertFalse(textArea.isEditable());
                assertTrue(textArea.getText().contains("Email"));

                foundScrollPane = true;
            }
        }

        assertTrue(foundScrollPane, "Δεν βρέθηκε JScrollPane με JTextArea");
    }

    @Test
    void testBackButtonExists() {
        JButton backButton = findButtonByText(contactScreen, "Back");
        assertNotNull(backButton);
    }

    @Test
    void testBackButtonActionDoesNotThrow() {
        JButton backButton = findButtonByText(contactScreen, "Back");

        assertNotNull(backButton);
        assertDoesNotThrow(backButton::doClick);
    }



    private JButton findButtonByText(Container root, String text) {
        for (var component : root.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if (text.equals(button.getText())) {
                    return button;
                }
            }
            if (component instanceof Container) {
                JButton found = findButtonByText((Container) component, text);
                if (found != null) return found;
            }
        }
        return null;
    }
}
