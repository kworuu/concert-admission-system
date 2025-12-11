package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.*;

public class InputValidator {

    public static Customer validateAndCreateCustomer(Component parent, String name, String email, String ageText) {
        // Name validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter your Full Name.");
            return null;
        }
        if (!name.matches("[\\p{L} . '\\-]+")) {
            JOptionPane.showMessageDialog(parent, "Please enter a valid name.");
            return null;
        }

        // Email validation
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter your Email Address.");
            return null;
        }
        String emailRegex = "^[\\p{L}0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(parent, "Please enter a valid email address.");
            return null;
        }

        // Age validation
        if (ageText.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Please enter your age", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            int age = Integer.parseInt(ageText);
            if (age < 1) {
                JOptionPane.showMessageDialog(parent, "Please enter a valid age.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return null;
            } else if (age > 90) {
                JOptionPane.showMessageDialog(parent, "Individuals of this age are advised not to attend the concert", "Warning!", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            // Success
            return new Customer(name, email, age);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Age must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}