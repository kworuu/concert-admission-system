package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ConcertAdmissionSystemUI extends JFrame {
    private JPanel contentpane;
    private JComboBox<String> cmbox_selectConcert;
    private JProgressBar progressBar1;
    private JTextField enterFullNameTextField;
    private JTextField enterEmailAddressTextField;
    private JTextField enterAgeTextField;
    private JComboBox<String> cmbox_seatingTierFilter;
    private JButton VVA1Button;
    private JButton VVA10Button;
    private JButton VVA2Button;
    private JButton VVA3Button;
    private JButton VVA4Button;
    private JButton VVA5Button;
    private JButton VVA6Button;
    private JButton VVA9Button;
    private JButton VVA8Button;
    private JButton VVA7Button;
    private JScrollBar scrollBar1;
    private JButton vB1Button;
    private JButton vB2Button;
    private JButton vB3Button;
    private JButton vB4Button;
    private JButton vB5Button;
    private JButton vB6Button;
    private JButton vB8Button;
    private JButton vB9Button;
    private JButton vB10Button;
    private JButton vB7Button;
    private JButton vC1Button;
    private JButton vC2Button;
    private JButton vC3Button;
    private JButton vC4Button;
    private JButton vC5Button;
    private JButton vC6Button;
    private JButton vC7Button;
    private JButton vC8Button;
    private JButton vC9Button;
    private JButton vC10Button;
    private JButton gD1Button;
    private JButton gD2Button;
    private JButton gD3Button;
    private JButton gD4Button;
    private JButton gD5Button;
    private JButton gD6Button;
    private JButton gD7Button;
    private JButton gD8Button;
    private JButton gD9Button;
    private JButton gD10Button;
    private JButton gE1Button;
    private JButton gE2Button;
    private JButton gE3Button;
    private JButton gE4Button;
    private JButton gE5Button;
    private JButton gE6Button;
    private JButton gE7Button;
    private JButton gE8Button;
    private JButton gE9Button;
    private JButton gE10Button;
    private JButton gF1Button;
    private JButton gF2Button;
    private JButton gF3Button;
    private JButton gF4Button;
    private JButton gF5Button;
    private JButton gF6Button;
    private JButton gF7Button;
    private JButton gF8Button;
    private JButton gF9Button;
    private JButton gF10Button;
    private JButton btnConfirmTicketDetails;
    private JPanel concertDetailsPanel;
    private JPanel customerInformationPanel;
    private JPanel seatSelectionPanel;
    private JScrollPane scrollPanel;
    private JLabel selectedSeatLabel;
    private JLabel priceLabel;
    private JPanel panelInsideScroll;
    private JLabel perksLabel;

    // Create Lists to Group Buttons
    private final List<JButton> vvip_buttons = new ArrayList<>();
    private final List<JButton> vip_buttons = new ArrayList<>();
    private final List<JButton> generalAdmission_buttons = new ArrayList<>();

    // Local Variables
    private JButton selectedSeat = null;

    // Colors
    private final Color COLOR_VVIP = new Color(255, 215, 0);
    private final Color COLOR_VIP = new Color(173, 216, 230);
    private final Color COLOR_GEN = new Color(211, 211, 211);
    private final Color COLOR_SELECTED = Color.YELLOW;
    private final Color COLOR_SOLD = Color.RED;

    // Capacity
    private final int maximumCapacity = 70;
    private int soldSeats = 0;

    public ConcertAdmissionSystemUI() {
        setTitle("Concert Admission System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(contentpane);
        pack();
        setLocationRelativeTo(null);

        // HOME notes 1 : Initialize the groups.
        groupButtons();
        populateComboBox();


        // HOME notes 2 : COLOR INITIALIZATION & LISTENERES
        initalizeSeatButtons();

        // Load Info : DRI

        // HOME notes 3 : Add filtering functionality
        cmbox_seatingTierFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSeats();
            }
        });

        // HOME notes 4 : Configure Progress Bar (Fixed at 70)
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(maximumCapacity);
        progressBar1.setValue(1);
        progressBar1.setStringPainted(true);
        progressBar1.setString("0 / " + maximumCapacity + " Sold");

        // HOME notes 5 : Confirm Button Listener
        btnConfirmTicketDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmPurchase();
            }
        });
    }

    public void initalizeSeatButtons() {
        ActionListener seatListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                handleSeatSelection(clickedButton);
            }
        };
            for (JButton btn : vvip_buttons) {
                btn.setBackground(COLOR_VVIP);
                btn.addActionListener(seatListener);
            }
            for (JButton btn : vip_buttons) {
                btn.setBackground(COLOR_VIP);
                btn.addActionListener(seatListener);
            }
            for (JButton btn : generalAdmission_buttons) {
                btn.setBackground(COLOR_GEN);
                btn.addActionListener(seatListener);
            }
    }

    private void handleSeatSelection(JButton clickedButton) {

        if (clickedButton.getText().equals("TAKEN")) {
            JOptionPane.showMessageDialog(this, "This seat is already taken!");
            return;
        }
        if (selectedSeat == clickedButton) {
            resetSeatColor(clickedButton);
            selectedSeat = null;
        }
        if (selectedSeat != null) {
            resetSeatColor(selectedSeat);
        }
        // set selected seat;
        selectedSeat = clickedButton;
        selectedSeat.setBackground(COLOR_SELECTED);
        selectedSeatLabel.setText(selectedSeat.getText());

        // update price label
        if (vvip_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 2000.00");
        } else if (vip_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 1000.00");
        } else if (generalAdmission_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 600.00");
        }
    }

    public void confirmPurchase() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (enterFullNameTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (enterEmailAddressTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Email Address.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ageText = enterAgeTextField.getText().trim();
        if(ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your age.", "Missing Input", JOptionPane.WARNING_MESSAGE);
        }

        // Validate that age is actually a number
        try {
            int age = Integer.parseInt(ageText);
            if (age < 1) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // SUCCESSFUL VALIDATION LOGIC
        int response = JOptionPane.showConfirmDialog(this, "Confirm purchase for seat?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            selectedSeat.setText("TAKEN");
            selectedSeat.setBackground(COLOR_SOLD);
            selectedSeat.setForeground(Color.WHITE);

            // Fix for Mac/Windows styling issues
            selectedSeat.setOpaque(true);
            selectedSeat.setBorderPainted(false);

            // Update progress bar counter
            soldSeats++;
            progressBar1.setValue(soldSeats);
            progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");

            // clear the selection variable for the next person
            selectedSeat = null;
            selectedSeatLabel.setText("~none");
            priceLabel.setText("PHP 0.00");
            enterFullNameTextField.setText("");
            enterEmailAddressTextField.setText("");
            enterAgeTextField.setText("");
            JOptionPane.showMessageDialog(this, "Ticket Confirmed!");
        }
    }

    public void resetSeatColor(JButton btn) {
        if (vvip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VVIP);
        } else if (vip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VIP);
        } else {
            btn.setBackground(COLOR_GEN);
        }
    }
    public void groupButtons() { // groupButtons note 1 : add each individual button to its corresponding list.
        // VVIP
        vvip_buttons.add(VVA1Button);
        vvip_buttons.add(VVA2Button);
        vvip_buttons.add(VVA3Button);
        vvip_buttons.add(VVA4Button);
        vvip_buttons.add(VVA5Button);
        vvip_buttons.add(VVA6Button);
        vvip_buttons.add(VVA7Button);
        vvip_buttons.add(VVA8Button);
        vvip_buttons.add(VVA9Button);
        vvip_buttons.add(VVA10Button);

        // VIP
        vip_buttons.add(vB1Button);
        vip_buttons.add(vB2Button);
        vip_buttons.add(vB3Button);
        vip_buttons.add(vB4Button);
        vip_buttons.add(vB5Button);
        vip_buttons.add(vB6Button);
        vip_buttons.add(vB7Button);
        vip_buttons.add(vB8Button);
        vip_buttons.add(vB9Button);
        vip_buttons.add(vB10Button);

        vip_buttons.add(vC1Button);
        vip_buttons.add(vC2Button);
        vip_buttons.add(vC3Button);
        vip_buttons.add(vC4Button);
        vip_buttons.add(vC5Button);
        vip_buttons.add(vC6Button);
        vip_buttons.add(vC7Button);
        vip_buttons.add(vC8Button);
        vip_buttons.add(vC9Button);
        vip_buttons.add(vC10Button);

        // GENERAL ADMISSION
        generalAdmission_buttons.add(gD1Button);
        generalAdmission_buttons.add(gD2Button);
        generalAdmission_buttons.add(gD3Button);
        generalAdmission_buttons.add(gD4Button);
        generalAdmission_buttons.add(gD5Button);
        generalAdmission_buttons.add(gD6Button);
        generalAdmission_buttons.add(gD7Button);
        generalAdmission_buttons.add(gD8Button);
        generalAdmission_buttons.add(gD9Button);
        generalAdmission_buttons.add(gD10Button);


        generalAdmission_buttons.add(gE1Button);
        generalAdmission_buttons.add(gE2Button);
        generalAdmission_buttons.add(gE3Button);
        generalAdmission_buttons.add(gE4Button);
        generalAdmission_buttons.add(gE5Button);
        generalAdmission_buttons.add(gE6Button);
        generalAdmission_buttons.add(gE7Button);
        generalAdmission_buttons.add(gE8Button);
        generalAdmission_buttons.add(gE9Button);
        generalAdmission_buttons.add(gE10Button);


        generalAdmission_buttons.add(gF1Button);
        generalAdmission_buttons.add(gF2Button);
        generalAdmission_buttons.add(gF3Button);
        generalAdmission_buttons.add(gF4Button);
        generalAdmission_buttons.add(gF5Button);
        generalAdmission_buttons.add(gF6Button);
        generalAdmission_buttons.add(gF7Button);
        generalAdmission_buttons.add(gF8Button);
        generalAdmission_buttons.add(gF9Button);
        generalAdmission_buttons.add(gF10Button);
    }

    public void populateComboBox() {
        // --- Box 1: Concert Selection ---
        // Clear it first to be safe (in case the GUI designer added example items)
        cmbox_selectConcert.removeAllItems();
        cmbox_selectConcert.addItem("The Eras Tour (December 24, 2025)");

        // --- Box 2: Filter Selection ---
        // 1. Clear existing items (removes the stuff you typed in the GUI Designer)
        cmbox_seatingTierFilter.removeAllItems();

        // 2. Add the items directly
        cmbox_seatingTierFilter.addItem("All");
        cmbox_seatingTierFilter.addItem("VVIP (PHP 2000)");
        cmbox_seatingTierFilter.addItem("VIP (PHP 1000)");
        cmbox_seatingTierFilter.addItem("General Admission (PHP 600)");
    }

    public void filterSeats() {
        String selectedTier = (String) cmbox_seatingTierFilter.getSelectedItem();

        if(selectedTier == null) return;

        // filterSeats notes 1 : Helper method to make code cleaner
        boolean showAll = selectedTier.equals("All");
        boolean showVVIP = selectedTier.equals("VVIP (PHP 2000)");
        boolean showVIP = selectedTier.equals("VIP (PHP 1000)");
        boolean showGeneralAdmission = selectedTier.equals("General Admission (PHP 600)");

        // filterSeats notes 2 : Loop through VVIP View buttons
        for (JButton btn : vvip_buttons) {
            btn.setVisible(showAll || showVVIP);
        }

        // filterSeats notes 3 : Loop through VIP View buttons
        for (JButton btn : vip_buttons) {
            btn.setVisible(showAll || showVIP);
        }

        // filterSeats notes 4 : Loop through General Admission View buttons
        for (JButton btn : generalAdmission_buttons) {
            btn.setVisible(showAll || showGeneralAdmission);
        }

        // filterSeats notes 5 : REFRESH PANEL INSIDE THE JSCROLLPANE
        // This tells the panel inside the scroll pane to recalculate its size and content
        // now that some buttons are hidden.
        panelInsideScroll.revalidate();
        panelInsideScroll.repaint();
    }
}
