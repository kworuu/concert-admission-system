package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SeatGridManager {
    // Button Lists
    private final List<JButton> vvipButtons = new ArrayList<>();
    private final List<JButton> vipButtons = new ArrayList<>();
    private final List<JButton> genButtons = new ArrayList<>();

    // Logic State
    private JButton selectedSeat = null;
    private int soldSeats = 0;
    private final int maximumCapacity = 60;

    // References to UI Labels to update them automatically
    private JLabel selectedSeatLabel;
    private JLabel priceLabel;
    private JLabel perksLabel;
    private JLabel seatTierLabel;

    // Colors
    private final Color COLOR_VVIP = new Color(255, 215, 0);
    private final Color COLOR_VIP = new Color(173, 216, 230);
    private final Color COLOR_GEN = new Color(220, 220, 220);
    private final Color COLOR_SELECTED = new Color(50, 205, 50);
    private final Color COLOR_SOLD = new Color(220, 53, 69);

    public SeatGridManager(JLabel selectedSeatLabel, JLabel priceLabel, JLabel perksLabel, JLabel seatTierLabel) {
        this.selectedSeatLabel = selectedSeatLabel;
        this.priceLabel = priceLabel;
        this.perksLabel = perksLabel;
        this.seatTierLabel = seatTierLabel;
    }

    public void addVVIP(JButton btn) { vvipButtons.add(btn); configureButtonName(btn); }
    public void addVIP(JButton btn) { vipButtons.add(btn); configureButtonName(btn); }
    public void addGen(JButton btn) { genButtons.add(btn); configureButtonName(btn); }

    private void configureButtonName(JButton btn) {
        // Sets the text of the button based on its Name property (e.g. VVA1Button -> VVA1)
        String name = btn.getName();
        if (name != null && name.endsWith("Button")) {
            // Save the original ID as the name property for logic
            String seatID = name.substring(0, name.length() - 6);
            btn.setName(seatID);
            btn.setText(seatID);
        } else {
            btn.setText("Seat");
        }
    }

    public void initializeButtons(ActionListener listener) {
        setupTier(vvipButtons, COLOR_VVIP, listener);
        setupTier(vipButtons, COLOR_VIP, listener);
        setupTier(genButtons, COLOR_GEN, listener);
    }

    private void setupTier(List<JButton> buttons, Color color, ActionListener listener) {
        for (JButton btn : buttons) {
            btn.setBackground(color);
            btn.setForeground(Color.BLACK);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.addActionListener(listener);
        }
    }

    public void handleSeatSelection(JButton clickedButton, Component parentFrame) {
        if (clickedButton.getText().equals("TAKEN")) {
            JOptionPane.showMessageDialog(parentFrame, "This seat is already taken!");
            return;
        }

        // Deselect if clicking the same seat
        if (selectedSeat == clickedButton) {
            resetSeatColor(clickedButton);
            selectedSeat = null;
            resetLabels();
            return;
        }

        // Reset previous selection
        if (selectedSeat != null) {
            resetSeatColor(selectedSeat);
        }

        // Select new seat
        selectedSeat = clickedButton;
        selectedSeat.setBackground(COLOR_SELECTED);
        selectedSeat.setForeground(Color.BLACK);

        updateLabelsForSeat(clickedButton);
    }

    private void updateLabelsForSeat(JButton btn) {
        selectedSeatLabel.setText(btn.getText());

        if (vvipButtons.contains(btn)) {
            priceLabel.setText("PHP 600.00");
            perksLabel.setText("Backstage Pass");
            seatTierLabel.setText("VVIP");
        } else if (vipButtons.contains(btn)) {
            priceLabel.setText("PHP 450.00");
            perksLabel.setText("Free Table");
            seatTierLabel.setText("VIP");
        } else {
            priceLabel.setText("PHP 200.00");
            perksLabel.setText("~none");
            seatTierLabel.setText("General Admission");
        }
    }

    private void resetLabels() {
        selectedSeatLabel.setText("~none");
        priceLabel.setText("PHP 0.00");
        perksLabel.setText("-");
        seatTierLabel.setText("-");
    }

    public void resetAllSeats(JProgressBar progressBar) {
        if (selectedSeat != null) {
            resetSeatColor(selectedSeat);
            selectedSeat = null;
        }
        resetLabels();

        resetTierVisuals(vvipButtons, COLOR_VVIP);
        resetTierVisuals(vipButtons, COLOR_VIP);
        resetTierVisuals(genButtons, COLOR_GEN);

        soldSeats = 0;
        updateProgressBar(progressBar);
    }

    private void resetTierVisuals(List<JButton> buttons, Color color) {
        for (JButton btn : buttons) {
            // Restore original name
            String originalID = btn.getName();
            btn.setText(originalID);
            btn.setBackground(color);
            btn.setForeground(Color.BLACK);
            btn.setBorderPainted(true);
        }
    }

    private void resetSeatColor(JButton btn) {
        if (vvipButtons.contains(btn)) btn.setBackground(COLOR_VVIP);
        else if (vipButtons.contains(btn)) btn.setBackground(COLOR_VIP);
        else btn.setBackground(COLOR_GEN);
        btn.setForeground(Color.BLACK);
    }

    public void loadSoldInfo(String concertName, JProgressBar progressBar, JPanel refreshPanel) {
        if (concertName == null || concertName.isEmpty()) return;

        List<String> takenSeats = TicketManager.loadSoldSeats(concertName);
        List<JButton> allButtons = new ArrayList<>();
        allButtons.addAll(vvipButtons);
        allButtons.addAll(vipButtons);
        allButtons.addAll(genButtons);

        soldSeats = 0;

        for (JButton btn : allButtons) {
            // Check based on the ID we stored in btn.getName()
            if (takenSeats.contains(btn.getName())) {
                btn.setText("TAKEN");
                btn.setBackground(COLOR_SOLD);
                btn.setForeground(Color.WHITE);
                btn.setBorderPainted(false);
                soldSeats++;
            }
        }
        updateProgressBar(progressBar);

        if (refreshPanel != null) {
            refreshPanel.revalidate();
            refreshPanel.repaint();
        }
    }

    private void updateProgressBar(JProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setValue(soldSeats);
            progressBar.setString(soldSeats + " / " + maximumCapacity + " Sold");
        }
    }

    public void filterSeats(String selectedTier, JPanel refreshPanel) {
        if (selectedTier == null) return;
        boolean showAll = selectedTier.equals("All");
        boolean showVVIP = selectedTier.contains("VVIP");
        boolean showVIP = selectedTier.contains("VIP") && !selectedTier.contains("VVIP");
        boolean showGen = selectedTier.contains("General");

        for (JButton btn : vvipButtons) btn.setVisible(showAll || showVVIP);
        for (JButton btn : vipButtons) btn.setVisible(showAll || showVIP);
        for (JButton btn : genButtons) btn.setVisible(showAll || showGen);

        if(refreshPanel != null) {
            refreshPanel.revalidate();
            refreshPanel.repaint();
        }
    }

    // Getters for Purchase Logic
    public JButton getSelectedSeat() { return selectedSeat; }

    public Object[] getSelectedSeatDetails() {
        if(selectedSeat == null) return null;

        if (vvipButtons.contains(selectedSeat)) return new Object[]{"VVIP", 600.00, "Backstage Pass"};
        if (vipButtons.contains(selectedSeat)) return new Object[]{"VIP", 450.00, "Free Table"};
        return new Object[]{"General Admission", 200.00, "~none"};
    }

    public void markSelectedAsSold() {
        if (selectedSeat != null) {
            selectedSeat.setText("TAKEN");
            selectedSeat.setBackground(COLOR_SOLD);
            selectedSeat.setForeground(Color.WHITE);
            selectedSeat.setBorderPainted(false);
            soldSeats++;
            selectedSeat = null; // Clear selection
        }
    }

    public int getSoldSeats() { return soldSeats; }
}