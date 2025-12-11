package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // 1. Setup the Modern Look and Feel (Dark Theme for Concert Vibe)
        try {
            // We use reflection here so the code compiles even if you haven't added the Jar yet.
            // Ideally, you should import com.formdev.flatlaf.FlatDarkLaf and use: FlatDarkLaf.setup();
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");

            // OPTIONAL: Customize global defaults to look more "App-like"
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

        // 2. Launch the Application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ConcertAdmissionSystemUI concertAdmissionSystem = new ConcertAdmissionSystemUI();
                concertAdmissionSystem.setVisible(true);
            }
        });
    }
}