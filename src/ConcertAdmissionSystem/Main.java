package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");

            UIManager.put("Button.arc", 15); // Rounder buttons
            UIManager.put("Component.arc", 10); // Rounder text fields
            UIManager.put("ProgressBar.arc", 10); // Rounder progress bar
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12)); // Cleaner font
        } catch (Exception e) {
            // Fallback: If FlatLaf jar is missing, use Nimbus (Built-in Java modern look)
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                // If all else fails, keep default
            }
        }

        // Launch the Application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConcertAdmissionSystemUI concertAdmissionSystem = new ConcertAdmissionSystemUI();
                concertAdmissionSystem.setVisible(true);
            }
        });
    }
}