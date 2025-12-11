package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.*;

public class UIStyler {

    public static void styleSectionPanel(JPanel panel, String title, Font font, Color color) {
        if (panel == null) return;
        javax.swing.border.TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitleFont(font);
        titledBorder.setTitleColor(color);
        javax.swing.border.Border compoundBorder = BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
        panel.setBorder(compoundBorder);
    }

    public static void styleCTAButton(JButton button) {
        if (button == null) return;
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(new Color(40, 167, 69));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 167, 69), 1),
                BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
    }

    public static void styleScanButton(JButton button) {
        if (button == null) return;
        Color adminBlue = new Color(0, 123, 255);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(adminBlue);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(adminBlue, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    public static void styleDropdown(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        Color brandBlue = new Color(51, 153, 255);
        Color darkBackground = new Color(45, 48, 50);

        comboBox.setBackground(darkBackground);
        comboBox.setForeground(Color.WHITE);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(brandBlue, 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                if (isSelected) {
                    setBackground(brandBlue);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(new Color(60, 63, 65));
                    setForeground(Color.WHITE);
                }
                return this;
            }
        });
    }

    public static void styleProgressBar(JProgressBar progressBar) {
        if (progressBar == null) return;
        progressBar.setPreferredSize(new Dimension(200, 30));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(51, 153, 255));
        progressBar.setBackground(new Color(45, 48, 50));
    }

    public static void styleReadOnlyFields(JTextField... fields) {
        Color fieldBackground = new Color(60, 63, 65);
        Color fieldBorderColor = new Color(80, 80, 80);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        for (JTextField field : fields) {
            if (field == null) continue;
            field.setOpaque(true);
            field.setBackground(fieldBackground);
            field.setForeground(Color.WHITE);
            field.setFont(fieldFont);
            field.setMargin(new Insets(5, 12, 5, 5));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorderColor, 1),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));
            field.setEditable(false);
            field.setFocusable(false);
        }
    }

    public static void adjustFonts(JLabel[] bigLabels, JLabel[] regularLabels, JLabel priceLabel, JTextField[] inputs) {
        Font labelFontBig = new Font("Segoe UI", Font.BOLD, 16);
        Font labelFontReg = new Font("Segoe UI", Font.BOLD, 14);
        Font priceFont = new Font("Segoe UI", Font.BOLD, 20);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        Color labelColor = new Color(200, 200, 200);
        Color priceColor = new Color(255, 85, 85);

        for (JLabel lbl : bigLabels) {
            if (lbl != null) {
                lbl.setFont(labelFontBig);
                lbl.setForeground(labelColor);
            }
        }
        for (JLabel lbl : regularLabels) {
            if (lbl != null) {
                lbl.setFont(labelFontReg);
                lbl.setForeground(Color.WHITE);
            }
        }
        if (priceLabel != null) {
            priceLabel.setFont(priceFont);
            priceLabel.setForeground(priceColor);
        }
        for (JTextField field : inputs) {
            if (field != null) {
                field.setFont(inputFont);
                if (field.isEditable()) {
                    field.setMargin(new Insets(4, 6, 4, 6));
                } else {
                    field.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                            BorderFactory.createEmptyBorder(5, 8, 5, 8)
                    ));
                }
            }
        }
    }
}