package ui;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationScreenTest {

    private RegistrationScreen registrationScreen;
    private AppController controller;

    @BeforeEach
    void setUp() {
        controller = new AppController();
        controller.setVisible(false);

        registrationScreen = new RegistrationScreen(controller);
    }

    @Test
    void testRegistrationScreenInitialization() {
        assertNotNull(registrationScreen);
        assertTrue(registrationScreen.getLayout() instanceof GridLayout);

        GridLayout layout = (GridLayout) registrationScreen.getLayout();
        assertEquals(4, layout.getRows());
        assertEquals(1, layout.getColumns());
    }

    @Test
    void testTextFieldsExist() {
        JTextField nameField = findTextFieldByText(registrationScreen, "Name");
        JTextField surnameField = findTextFieldByText(registrationScreen, "Surname");
        JTextField emailField = findTextFieldByText(registrationScreen, "Email");

        assertNotNull(nameField);
        assertNotNull(surnameField);
        assertNotNull(emailField);
    }

    @Test
    void testRegisterButtonExists() {
        JButton registerButton = findButtonByText(registrationScreen, "Register");
        assertNotNull(registerButton);
    }

    @Test
    void testRegisterButtonActionDoesNotThrow() {
        JButton registerButton = findButtonByText(registrationScreen, "Register");

        assertNotNull(registerButton);
        assertDoesNotThrow(registerButton::doClick);
    }


    private JTextField findTextFieldByText(Container root, String text) {
        for (Component c : root.getComponents()) {
            if (c instanceof JTextField) {
                JTextField tf = (JTextField) c;
                if (text.equals(tf.getText())) {
                    return tf;
                }
            }
            if (c instanceof Container) {
                JTextField found = findTextFieldByText((Container) c, text);
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
