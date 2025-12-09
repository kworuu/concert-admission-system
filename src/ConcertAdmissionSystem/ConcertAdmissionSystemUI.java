package ConcertAdmissionSystem;

import com.formdev.flatlaf.extras.FlatSVGIcon; // <--- NEW IMPORT
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.BorderLayout;

public class ConcertAdmissionSystemUI extends JFrame {
    private JPanel contentpane;
    private JComboBox<String> cmbox_selectConcert;
    private JProgressBar progressBar1;
    private JTextField enterFullNameTextField;
    private JTextField enterEmailAddressTextField;
    private JTextField enterAgeTextField;
    private JComboBox<String> cmbox_seatingTierFilter;

    // VVIP Buttons
    private JButton VVA1Button, VVA10Button, VVA2Button, VVA3Button, VVA4Button;
    private JButton VVA5Button, VVA6Button, VVA9Button, VVA8Button, VVA7Button;

    private JScrollBar scrollBar1;

    // VIP Buttons
    private JButton vB1Button, vB2Button, vB3Button, vB4Button, vB5Button;
    private JButton vB6Button, vB8Button, vB9Button, vB10Button, vB7Button;
    private JButton vC1Button, vC2Button, vC3Button, vC4Button, vC5Button;
    private JButton vC6Button, vC7Button, vC8Button, vC9Button, vC10Button;

    // Gen Ad Buttons
    private JButton gD1Button, gD2Button, gD3Button, gD4Button, gD5Button;
    private JButton gD6Button, gD7Button, gD8Button, gD9Button, gD10Button;
    private JButton gE1Button, gE2Button, gE3Button, gE4Button, gE5Button;
    private JButton gE6Button, gE7Button, gE8Button, gE9Button, gE10Button;
    private JButton gF1Button, gF2Button, gF3Button, gF4Button, gF5Button;
    private JButton gF6Button, gF7Button, gF8Button, gF9Button, gF10Button;

    private JButton btnConfirmTicketDetails;
    private JPanel concertDetailsPanel;
    private JPanel customerInformationPanel;
    private JPanel seatSelectionPanel;
    private JScrollPane scrollPanel;
    private JLabel selectedSeatLabel;
    private JLabel priceLabel;
    private JPanel panelInsideScroll;
    private JLabel perksLabel;
    private JTextField details_concertDate;
    private JTextField details_artist;
    private JTextField textField1; // Venue
    private JLabel seatTierLabel;

    private final List<JButton> vvip_buttons = new ArrayList<>();
    private final List<JButton> vip_buttons = new ArrayList<>();
    private final List<JButton> generalAdmission_buttons = new ArrayList<>();

    private List<Concert> availableConcerts;
    private JButton selectedSeat = null;

    // Colors
    private final Color COLOR_VVIP = new Color(255, 215, 0);       // Gold
    private final Color COLOR_VIP = new Color(173, 216, 230);      // Light Blue
    private final Color COLOR_GEN = new Color(220, 220, 220);      // Light Gray
    private final Color COLOR_SELECTED = new Color(50, 205, 50);   // Lime Green
    private final Color COLOR_SOLD = new Color(220, 53, 69);       // Modern Red

    private final int maximumCapacity = 60;
    private int soldSeats = 0;

    public ConcertAdmissionSystemUI() {
        setTitle("Concert Admission System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        customizeVisuals();
        styleCTAButton();

        // --- MAIN LAYOUT ---
        JPanel mainWrapper = new JPanel(new BorderLayout());

        // --- A. HEADER (SVG Logo + Text) ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // SVG FILE PATH
        String logoPath = "src/assets/logos/logo4.svg";

        JLabel headerLabel = new JLabel("Wildcats Pub Concerts");

        // --- NEW SVG LOADING LOGIC ---
        File svgFile = new File(logoPath);
        if (svgFile.exists()) {
            try {
                // 1. Load the SVG
                FlatSVGIcon svgIcon = new FlatSVGIcon(svgFile);

                // 2. Scale it to 100px height (Calculate width to keep aspect ratio)
                //    (We check if height is > 0 to avoid divide-by-zero errors)
                if (svgIcon.getIconHeight() > 0) {
                    float scale = 100f / svgIcon.getIconHeight();
                    int newWidth = Math.round(svgIcon.getIconWidth() * scale);

                    // 3. Derive a new icon with the exact size
                    headerLabel.setIcon(svgIcon.derive(newWidth, 100));
                } else {
                    headerLabel.setIcon(svgIcon); // Fallback if size unknown
                }
            } catch (Exception e) {
                System.out.println("Error loading SVG: " + e.getMessage());
            }
        }
        // -----------------------------

        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(51, 153, 255));
        headerLabel.setIconTextGap(20);
        headerLabel.setHorizontalTextPosition(JLabel.RIGHT);
        headerLabel.setVerticalTextPosition(JLabel.CENTER);

        topBar.add(headerLabel);
        mainWrapper.add(topBar, BorderLayout.NORTH);


        // --- B. SCROLLABLE CONTENT ---
        JPanel bodyWrapper = new JPanel(new BorderLayout());
        bodyWrapper.add(contentpane, BorderLayout.CENTER);
        // Bottom cushion (100px)
        bodyWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 100, 20));

        JScrollPane centerScrollPane = new JScrollPane(bodyWrapper);
        centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        centerScrollPane.setBorder(null);

        mainWrapper.add(centerScrollPane, BorderLayout.CENTER);
        setContentPane(mainWrapper);


        // --- FIX FOR SEAT LIST HEIGHT ---
        if (scrollPanel != null) {
            scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, 100));
            scrollPanel.setMinimumSize(new Dimension(0, 100));
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setLocationRelativeTo(null);

        // --- LOGIC INITIALIZATION ---
        if (details_concertDate != null) details_concertDate.setEditable(false);
        if (details_artist != null) details_artist.setEditable(false);
        if (textField1 != null) textField1.setEditable(false);

        groupButtons();
        populateComboBox();

        cmbox_selectConcert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) cmbox_selectConcert.getSelectedItem();
                if (selectedName != null && availableConcerts != null) {
                    for (Concert concert : availableConcerts) {
                        if (concert.getConcertName().equals(selectedName)) {
                            loadConcertDetails(concert);
                            break;
                        }
                    }
                }
            }
        });

        initalizeSeatButtons();
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(maximumCapacity);
        progressBar1.setValue(0);
        progressBar1.setStringPainted(true);
        progressBar1.setString("0 / " + maximumCapacity + " Sold");
        loadInfo();

        cmbox_seatingTierFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSeats();
            }
        });

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
            btn.setForeground(Color.BLACK);
            btn.addActionListener(seatListener);
        }
        for (JButton btn : vip_buttons) {
            btn.setBackground(COLOR_VIP);
            btn.setForeground(Color.BLACK);
            btn.addActionListener(seatListener);
        }
        for (JButton btn : generalAdmission_buttons) {
            btn.setBackground(COLOR_GEN);
            btn.setForeground(Color.BLACK);
            btn.addActionListener(seatListener);
        }
    }

    private void handleSeatSelection(JButton clickedButton) {
        if (clickedButton.getText().equals("TAKEN")) {
            JOptionPane.showMessageDialog(this, "This seat is already taken!");
            return;
        }
        if (selectedSeat != null) {
            resetSeatColor(selectedSeat);
        }
        if (selectedSeat == clickedButton) {
            selectedSeat = null;
            selectedSeatLabel.setText("~none");
            priceLabel.setText("PHP 0.00");
            perksLabel.setText("-");
            seatTierLabel.setText("-");
            return;
        }

        selectedSeat = clickedButton;
        selectedSeat.setBackground(COLOR_SELECTED);
        selectedSeat.setForeground(Color.BLACK);
        selectedSeatLabel.setText(selectedSeat.getText());

        if (vvip_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 600.00");
            perksLabel.setText("Backstage Pass");
            seatTierLabel.setText("VVIP");
        } else if (vip_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 450.00");
            perksLabel.setText("Free Table");
            seatTierLabel.setText("VIP");
        } else if (generalAdmission_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 200.00");
            perksLabel.setText("~none");
            seatTierLabel.setText("General Admission");
        }
    }

    private Concert getSelectedConcert() {
        String selectedName = (String) cmbox_selectConcert.getSelectedItem();
        if (selectedName != null && availableConcerts != null) {
            for (Concert concert : availableConcerts) {
                if (concert.getConcertName().equals(selectedName)) {
                    return concert;
                }
            }
        }
        return null;
    }

    private Object[] getSeatDetailsFromButton(JButton button) {
        String tierName = "";
        double price = 0.0;
        String perks = "";

        if (vvip_buttons.contains(button)) {
            tierName = "VVIP";
            price = 600.00;
            perks = "Backstage Pass";
        } else if (vip_buttons.contains(button)) {
            tierName = "VIP";
            price = 450.00;
            perks = "Free Table";
        } else if (generalAdmission_buttons.contains(button)) {
            tierName = "General Admission";
            price = 200.00;
            perks = "~none";
        }
        return new Object[]{tierName, price, perks};
    }

    public void confirmPurchase() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = createCustomerFromInput();
        if (customer == null) return;

        Concert selectedConcert = getSelectedConcert();
        if (selectedConcert == null) return;

        String actualSeatIdentifier = selectedSeat.getText();
        Object[] seatDetails = getSeatDetailsFromButton(selectedSeat);
        String tierName = (String) seatDetails[0];
        double price = (Double) seatDetails[1];
        String perks = (String) seatDetails[2];

        SeatingTier tierObject = new SeatingTier(tierName, price, perks);
        Seat seat = new Seat(actualSeatIdentifier, String.valueOf(actualSeatIdentifier.charAt(0)), tierObject);
        seat.markAsTaken();
        Ticket finalTicket = new Ticket(customer, price, selectedConcert, tierObject, seat);
        String ticketID = finalTicket.getTicketID();

        int response = JOptionPane.showConfirmDialog(this,
                String.format("Confirm purchase:\nName: %s\nSeat: %s (%s)\nPrice: PHP %.2f",
                        customer.getName(), actualSeatIdentifier, tierName, price),
                "Confirm Purchase", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            PDFGenerator generator = new PDFGenerator();
            String pdfPath = null;
            try {
                pdfPath = generator.generateTicket(finalTicket);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error generating ticket: " + e.getMessage());
                return;
            }

            boolean emailSent = EmailSender.sendTicketEmail(customer.getEmail(), pdfPath, customer.getName(), ticketID);

            if (emailSent) {
                finalTicket.saveToFile();
                selectedSeat.setText("TAKEN");
                selectedSeat.setBackground(COLOR_SOLD);
                selectedSeat.setForeground(Color.WHITE);
                selectedSeat.setOpaque(true);
                selectedSeat.setBorderPainted(false);

                soldSeats++;
                progressBar1.setValue(soldSeats);
                progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");

                resetSelectionUI();
                JOptionPane.showMessageDialog(this, "Ticket Confirmed! Check your email.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to send email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (pdfPath != null) {
                try { new File(pdfPath).delete(); } catch (Exception ignored) {}
            }
        }
    }

    private void resetSelectionUI() {
        selectedSeat = null;
        selectedSeatLabel.setText("~none");
        priceLabel.setText("PHP 0.00");
        enterFullNameTextField.setText("");
        enterEmailAddressTextField.setText("");
        enterAgeTextField.setText("");
    }

    public void resetSeatColor(JButton btn) {
        if (vvip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VVIP);
        } else if (vip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VIP);
        } else {
            btn.setBackground(COLOR_GEN);
        }
        btn.setForeground(Color.BLACK);
    }

    public void groupButtons() {
        vvip_buttons.add(VVA1Button); vvip_buttons.add(VVA2Button); vvip_buttons.add(VVA3Button);
        vvip_buttons.add(VVA4Button); vvip_buttons.add(VVA5Button); vvip_buttons.add(VVA6Button);
        vvip_buttons.add(VVA7Button); vvip_buttons.add(VVA8Button); vvip_buttons.add(VVA9Button); vvip_buttons.add(VVA10Button);
        vip_buttons.add(vB1Button); vip_buttons.add(vB2Button); vip_buttons.add(vB3Button);
        vip_buttons.add(vB4Button); vip_buttons.add(vB5Button); vip_buttons.add(vB6Button);
        vip_buttons.add(vB7Button); vip_buttons.add(vB8Button); vip_buttons.add(vB9Button); vip_buttons.add(vB10Button);
        vip_buttons.add(vC1Button); vip_buttons.add(vC2Button); vip_buttons.add(vC3Button);
        vip_buttons.add(vC4Button); vip_buttons.add(vC5Button); vip_buttons.add(vC6Button);
        vip_buttons.add(vC7Button); vip_buttons.add(vC8Button); vip_buttons.add(vC9Button); vip_buttons.add(vC10Button);
        generalAdmission_buttons.add(gD1Button); generalAdmission_buttons.add(gD2Button); generalAdmission_buttons.add(gD3Button);
        generalAdmission_buttons.add(gD4Button); generalAdmission_buttons.add(gD5Button); generalAdmission_buttons.add(gD6Button);
        generalAdmission_buttons.add(gD7Button); generalAdmission_buttons.add(gD8Button); generalAdmission_buttons.add(gD9Button); generalAdmission_buttons.add(gD10Button);
        generalAdmission_buttons.add(gE1Button); generalAdmission_buttons.add(gE2Button); generalAdmission_buttons.add(gE3Button);
        generalAdmission_buttons.add(gE4Button); generalAdmission_buttons.add(gE5Button); generalAdmission_buttons.add(gE6Button);
        generalAdmission_buttons.add(gE7Button); generalAdmission_buttons.add(gE8Button); generalAdmission_buttons.add(gE9Button); generalAdmission_buttons.add(gE10Button);
        generalAdmission_buttons.add(gF1Button); generalAdmission_buttons.add(gF2Button); generalAdmission_buttons.add(gF3Button);
        generalAdmission_buttons.add(gF4Button); generalAdmission_buttons.add(gF5Button); generalAdmission_buttons.add(gF6Button);
        generalAdmission_buttons.add(gF7Button); generalAdmission_buttons.add(gF8Button); generalAdmission_buttons.add(gF9Button); generalAdmission_buttons.add(gF10Button);
    }

    public void populateComboBox() {
        cmbox_selectConcert.removeAllItems();
        availableConcerts = new ArrayList<>();
        LocalDateTime concertDate = LocalDateTime.of(2025, 12, 25, 19, 0);
        Concert concert1 = new Concert("WildCats Pub Concert for a Cause", concertDate, "Zild", "CIT-U Gymnasium");
        availableConcerts.add(concert1);
        for (Concert concert : availableConcerts) {
            cmbox_selectConcert.addItem(concert.getConcertName());
        }
        if (!availableConcerts.isEmpty()) {
            loadConcertDetails(availableConcerts.get(0));
        }
        cmbox_seatingTierFilter.removeAllItems();
        cmbox_seatingTierFilter.addItem("All");
        cmbox_seatingTierFilter.addItem("VVIP (PHP 600)");
        cmbox_seatingTierFilter.addItem("VIP (PHP 450)");
        cmbox_seatingTierFilter.addItem("General Admission (PHP 200)");
    }

    public void loadConcertDetails(Concert concert) {
        if (concert == null) return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
        details_artist.setText(concert.getArtist());
        details_concertDate.setText(concert.getConcertDate().format(dtf));
        textField1.setText(concert.getVenue());
        perksLabel.setText("Select a seat to view perks.");
    }

    public void filterSeats() {
        String selectedTier = (String) cmbox_seatingTierFilter.getSelectedItem();
        if(selectedTier == null) return;
        boolean showAll = selectedTier.equals("All");
        boolean showVVIP = selectedTier.contains("VVIP");
        boolean showVIP = selectedTier.contains("VIP") && !selectedTier.contains("VVIP");
        boolean showGeneralAdmission = selectedTier.contains("General");
        for (JButton btn : vvip_buttons) btn.setVisible(showAll || showVVIP);
        for (JButton btn : vip_buttons) btn.setVisible(showAll || showVIP);
        for (JButton btn : generalAdmission_buttons) btn.setVisible(showAll || showGeneralAdmission);
        panelInsideScroll.revalidate();
        panelInsideScroll.repaint();
    }

    public void loadInfo() {
        List<String> takenSeats = TicketManager.loadSoldSeats();
        List<JButton> allButtons = new ArrayList<>();
        allButtons.addAll(vvip_buttons);
        allButtons.addAll(vip_buttons);
        allButtons.addAll(generalAdmission_buttons);
        soldSeats = 0;
        for (JButton btn : allButtons) {
            if (takenSeats.contains(btn.getText())) {
                btn.setText("TAKEN");
                btn.setBackground(COLOR_SOLD);
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(false);
                soldSeats++;
            }
        }
        progressBar1.setValue(soldSeats);
        progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");
        if (panelInsideScroll != null) {
            panelInsideScroll.revalidate();
            panelInsideScroll.repaint();
        }
    }

    public static boolean isValidEmail(String email){
        String regex = "^[\\p{L}0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(regex);
    }

    public static boolean checkName(String name){
        return name.matches("[\\p{L} '\\-]+");
    }

    private Customer createCustomerFromInput(){
        String name = enterFullNameTextField.getText().trim();
        String email = enterEmailAddressTextField.getText().trim();
        String ageText = enterAgeTextField.getText().trim();
        int age;

        if(name.isEmpty() || !checkName(name)){
            JOptionPane.showMessageDialog(this, "Please enter a valid Full Name.");
            return null;
        }
        if(email.isEmpty() || !isValidEmail(email)){
            JOptionPane.showMessageDialog(this, "Please enter a valid Email Address.");
            return null;
        }
        try {
            age = Integer.parseInt(ageText);
            if (age < 1 || age > 90) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-90).", "Invalid Age", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new Customer(name, email, age);
    }

    private void customizeVisuals() {
        Font headerFont = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        Color headerColor = new Color(51, 153, 255);
        styleSectionPanel(concertDetailsPanel, "1. Concert Details", headerFont, headerColor);
        styleSectionPanel(customerInformationPanel, "2. Customer Information", headerFont, headerColor);
        styleSectionPanel(seatSelectionPanel, "3. Seat Selection", headerFont, headerColor);
    }

    private void styleSectionPanel(JPanel panel, String title, Font font, Color color) {
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

    private void styleCTAButton() {
        if (btnConfirmTicketDetails == null) return;
        btnConfirmTicketDetails.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnConfirmTicketDetails.setBackground(new Color(40, 167, 69));
        btnConfirmTicketDetails.setForeground(Color.WHITE);
        btnConfirmTicketDetails.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmTicketDetails.setFocusPainted(false);
        btnConfirmTicketDetails.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(40, 167, 69), 1),
                        BorderFactory.createEmptyBorder(15, 40, 15, 40)
                )
        );
    }
}