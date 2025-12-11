package ConcertAdmissionSystem;

import com.formdev.flatlaf.extras.FlatSVGIcon; // <--- NEW IMPORT
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
    private JTextField details_venue; // Venue
    private JLabel seatTierLabel;
    private JLabel venueLbl;
    private JLabel concertDateLbl;
    private JLabel artistLbl;
    private JButton btnScanQRCode;
    private JLabel nameLbl;
    private JLabel emailLbl;
    private JLabel ageLbl;
    private JLabel selectedSeatLbl;
    private JLabel seatTierlbl;
    private JLabel perksLbl;
    private JLabel priceLbl;
    private JLabel selectConcertLabel;
    private JLabel filterTierLbl;
    private JLabel capacityStatusLabel;
    private JPanel buttonsPanel;

    // Create Lists to Group Buttons
    private final List<JButton> vvip_buttons = new ArrayList<>();
    private final List<JButton> vip_buttons = new ArrayList<>();
    private final List<JButton> generalAdmission_buttons = new ArrayList<>();

    private ConcertManager concertManager;
    private List<Concert> availableConcerts;

    private String currentConcertName;

    // Local Variables
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
        styleDropdown();
        styleReadOnlyFields();
        adjustFonts();
        styleProgressBar();

        // --- MAIN LAYOUT ---
        JPanel mainWrapper = new JPanel(new BorderLayout());

// --- A. HEADER (Logo + Gradient Text) ---
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px gap between Logo and Text
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

// 1. THE LOGO (Icon Only)
        JLabel logoLabel = new JLabel();
        String logoPath = "src/assets/logos/logo4.svg";
        File svgFile = new File(logoPath);

        if (svgFile.exists()) {
            try {
                FlatSVGIcon svgIcon = new FlatSVGIcon(svgFile);
                // Scale to 80px height (Slightly smaller to match 36px text better)
                if (svgIcon.getIconHeight() > 0) {
                    float scale = 80f / svgIcon.getIconHeight();
                    int newWidth = Math.round(svgIcon.getIconWidth() * scale);
                    logoLabel.setIcon(svgIcon.derive(newWidth, 80));
                } else {
                    logoLabel.setIcon(svgIcon);
                }
            } catch (Exception e) {
                System.out.println("Error loading SVG: " + e.getMessage());
            }
        }

        JLabel titleLabel = new JLabel("Wildcats Pub Concerts") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                // Enable anti-aliasing for smooth text
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // GRADIENT DEFINITION
                // Start: Brand Blue (51, 153, 255)
                // End:   Vibrant Violet (147, 51, 234)
                // The middle automatically blends into a nice Purple.
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(51, 153, 255),
                        getPreferredSize().width, 0, new Color(147, 51, 234)
                );

                g2.setPaint(gradient);

                // Draw the text manually to apply the gradient
                FontMetrics fm = g2.getFontMetrics();
                int x = 0;
                // Center vertically based on font height
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                g2.drawString(getText(), x, y);
            }
        };

// 3. APPLY FONT (Size 36)
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
// We need to set a preferred size so the layout manager knows how much space the custom text needs
        titleLabel.setPreferredSize(new Dimension(450, 80));

// Add them separately
        topBar.add(logoLabel);
        topBar.add(titleLabel);

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

        concertManager = new ConcertManager();

// Set the detail fields to read-only
        details_concertDate.setEditable(false);
        details_artist.setEditable(false);
        details_venue.setEditable(false);

        // 2. FORCE them to look like normal (editable) text fields
        //    We grab the colors used by the "Look and Feel" (FlatLaf) for standard fields.
        Color standardBg = UIManager.getColor("TextField.background");
        Color standardFg = UIManager.getColor("TextField.foreground");
        javax.swing.border.Border standardBorder = UIManager.getBorder("TextField.border");

        // 3. Apply those standard colors/borders to your read-only fields
        details_concertDate.setBackground(standardBg);
        details_concertDate.setForeground(standardFg);
        details_concertDate.setBorder(standardBorder);

        details_artist.setBackground(standardBg);
        details_artist.setForeground(standardFg);
        details_artist.setBorder(standardBorder);

        details_venue.setBackground(standardBg);
        details_venue.setForeground(standardFg);
        details_venue.setBorder(standardBorder);

        // HOME notes 1 : Initialize the groups.
        groupButtons();
        populateComboBox();

        cmbox_selectConcert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) cmbox_selectConcert.getSelectedItem();

                // Find the corresponding Concert object in your list
                if (selectedName != null) {
                    Concert concert = concertManager.getConcertByTitle(selectedName);
                    if (concert != null) {
                        loadConcertDetails(concert);
                    }
                }
            }
        });

        // HOME notes 2 : COLOR INITIALIZATION & LISTENERS
        initalizeSeatButtons();

        // HOME notes 3 : Configure Progress Bar (Fixed at 70)
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

        if (btnScanQRCode == null) {
            btnScanQRCode = new JButton("Scan QR (Admin)");
        }


        styleScanButton();

// Add the Listener to open the Scanner
        btnScanQRCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        TicketScanner scanner = new TicketScanner();

                        // --- FIX STARTS HERE ---
                        // 1. Force the layout to calculate size immediately
                        scanner.pack();

                        // 2. Center it on the screen (null = center of monitor)
                        scanner.setLocationRelativeTo(null);

                        // OPTIONAL: If you want it centered over your APP specifically, use this instead:
                        // scanner.setLocationRelativeTo(ConcertAdmissionSystemUI.this);
                        // -----------------------

                        scanner.setVisible(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ConcertAdmissionSystemUI.this,
                                "Error launching scanner: " + ex.getMessage());
                    }
                });
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

        List<List<JButton>> allTiers = Arrays.asList(vvip_buttons, vip_buttons, generalAdmission_buttons);
        Color[] tierColors = {COLOR_VVIP, COLOR_VIP, COLOR_GEN};

        for (int i = 0; i < allTiers.size(); i++) {
            List<JButton> buttons = allTiers.get(i);
            Color color = tierColors[i];

            for (JButton btn : buttons) {
                // Set the initial seat identifier as the button's text.
                // This relies on the button having its `name` property set in the GUI designer
                // (e.g., VVA1Button) and then using that name minus "Button".
                String buttonName = btn.getName();
                if (buttonName != null && buttonName.endsWith("Button")) {
                    btn.setText(buttonName.substring(0, buttonName.length() - 6));
                } else {
                    // Fallback for buttons without a name property (less ideal)
                    btn.setText("Seat");
                }

                btn.setBackground(color);
                btn.addActionListener(seatListener);
                btn.setForeground(Color.BLACK); // Default text color
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(true);
            }
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

    // --- START OF NEW METHODS TO ADD/REPLACE IN ConcertAdmissionSystemUI.java ---

    /**
     * Gets the currently selected Concert object from the list.
     */
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

    /**
     * Generates a unique ticket ID.
     */
    private String generateUniqueTicketID() {
        // Generates a simple ID based on current time
        return "WC" + System.currentTimeMillis() % 1000000;
    }

    /**
     * Attempts to parse the seat details (Tier Name, Price, and Perks) from the clicked button.
     * @param button The clicked seat button (e.g., VVA1)
     * @return A temporary array/object containing [Tier Name (String), Price (Double), Perks (String)].
     */
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

    /**
     * Orchestrates the full purchase process, including validation, ticket object creation,
     * PDF generation (via EmailSender), email sending, and final UI update.
     */
    // Inside ConcertAdmissionSystemUI.java, replace the old confirmPurchase method:
    public void confirmPurchase() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1-6. Validation, gathering details, and creating Ticket object (NO CHANGE)
        Customer customer = createCustomerFromInput();
        if (customer == null) { return; }
        Concert selectedConcert = getSelectedConcert();
        if (selectedConcert == null) {
            JOptionPane.showMessageDialog(this, "No concert is selected or loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String actualSeatIdentifier = selectedSeat.getText();
        Object[] seatDetails = getSeatDetailsFromButton(selectedSeat);
        String tierName = (String) seatDetails[0];
        double price = (Double) seatDetails[1];
        String perks = (String) seatDetails[2];
        String row = String.valueOf(actualSeatIdentifier.charAt(0));
        SeatingTier tierObject = new SeatingTier(tierName, price, perks);
        Seat seat = new Seat(actualSeatIdentifier, row, tierObject);
        seat.markAsTaken();
        Ticket finalTicket = new Ticket(customer, price, selectedConcert, tierObject, seat);
        String ticketID = finalTicket.getTicketID();

        // 7. Confirmation Dialogue
        int response = JOptionPane.showConfirmDialog(this,
                String.format("Confirm purchase:\nName: %s\nSeat: %s (%s)\nPrice: PHP %.2f\nPerks: %s\nTicket ID: %s",
                        customer.getName(), actualSeatIdentifier, tierName, price, perks, ticketID),
                "Confirm Ticket Purchase", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {

            // 8. Generate PDF File and Get Path (SIMPLIFIED!)
            PDFGenerator generator = new PDFGenerator();
            String pdfPath;

            try {
                // Directly calls PDFGenerator and gets the file path (String)
                pdfPath = generator.generateTicket(finalTicket);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Critical Error: Failed to generate or save PDF ticket.", "PDF Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop the purchase process
            }

            // 9. Call EmailSender with the file path
            boolean emailSent = EmailSender.sendTicketEmail(
                    customer.getEmail(),
                    pdfPath, // String: path to the saved PDF file
                    customer.getName(),
                    ticketID
            );

            // 10. Finalize Transaction, UI Update, and Cleanup
            if (emailSent) {
                TicketManager.saveTicket(finalTicket,currentConcertName);

                // Update UI visually (Existing logic)
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
// --- END OF NEW METHODS ---

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
    public void groupButtons() { // groupButtons note 1 : add each individual button to its corresponding list.
        // VVIP
        VVA1Button.setName("VVA1"); vvip_buttons.add(VVA1Button);
        VVA2Button.setName("VVA2"); vvip_buttons.add(VVA2Button);
        VVA3Button.setName("VVA3"); vvip_buttons.add(VVA3Button);
        VVA4Button.setName("VVA4"); vvip_buttons.add(VVA4Button);
        VVA5Button.setName("VVA5"); vvip_buttons.add(VVA5Button);
        VVA6Button.setName("VVA6"); vvip_buttons.add(VVA6Button);
        VVA7Button.setName("VVA7"); vvip_buttons.add(VVA7Button);
        VVA8Button.setName("VVA8"); vvip_buttons.add(VVA8Button);
        VVA9Button.setName("VVA9"); vvip_buttons.add(VVA9Button);
        VVA10Button.setName("VVA10"); vvip_buttons.add(VVA10Button);

        // VIP
        vB1Button.setName("VB1"); vip_buttons.add(vB1Button);
        vB2Button.setName("VB2"); vip_buttons.add(vB2Button);
        vB3Button.setName("VB3"); vip_buttons.add(vB3Button);
        vB4Button.setName("VB4"); vip_buttons.add(vB4Button);
        vB5Button.setName("VB5"); vip_buttons.add(vB5Button);
        vB6Button.setName("VB6"); vip_buttons.add(vB6Button);
        vB7Button.setName("VB7"); vip_buttons.add(vB7Button);
        vB8Button.setName("VB8"); vip_buttons.add(vB8Button);
        vB9Button.setName("VB9"); vip_buttons.add(vB9Button);
        vB10Button.setName("VB10"); vip_buttons.add(vB10Button);

        vC1Button.setName("VC1"); vip_buttons.add(vC1Button);
        vC2Button.setName("VC2"); vip_buttons.add(vC2Button);
        vC3Button.setName("VC3"); vip_buttons.add(vC3Button);
        vC4Button.setName("VC4"); vip_buttons.add(vC4Button);
        vC5Button.setName("VC5"); vip_buttons.add(vC5Button);
        vC6Button.setName("VC6"); vip_buttons.add(vC6Button);
        vC7Button.setName("VC7"); vip_buttons.add(vC7Button);
        vC8Button.setName("VC8"); vip_buttons.add(vC8Button);
        vC9Button.setName("VC9"); vip_buttons.add(vC9Button);
        vC10Button.setName("VC10"); vip_buttons.add(vC10Button);

        // GENERAL ADMISSION
        gD1Button.setName("GD1"); generalAdmission_buttons.add(gD1Button);
        gD2Button.setName("GD2"); generalAdmission_buttons.add(gD2Button);
        gD3Button.setName("GD3"); generalAdmission_buttons.add(gD3Button);
        gD4Button.setName("GD4"); generalAdmission_buttons.add(gD4Button);
        gD5Button.setName("GD5"); generalAdmission_buttons.add(gD5Button);
        gD6Button.setName("GD6"); generalAdmission_buttons.add(gD6Button);
        gD7Button.setName("GD7"); generalAdmission_buttons.add(gD7Button);
        gD8Button.setName("GD8"); generalAdmission_buttons.add(gD8Button);
        gD9Button.setName("GD9"); generalAdmission_buttons.add(gD9Button);
        gD10Button.setName("GD10"); generalAdmission_buttons.add(gD10Button);


        gE1Button.setName("GE1"); generalAdmission_buttons.add(gE1Button);
        gE2Button.setName("GE2"); generalAdmission_buttons.add(gE2Button);
        gE3Button.setName("GE3"); generalAdmission_buttons.add(gE3Button);
        gE4Button.setName("GE4"); generalAdmission_buttons.add(gE4Button);
        gE5Button.setName("GE5"); generalAdmission_buttons.add(gE5Button);
        gE6Button.setName("GE6"); generalAdmission_buttons.add(gE6Button);
        gE7Button.setName("GE7"); generalAdmission_buttons.add(gE7Button);
        gE8Button.setName("GE8"); generalAdmission_buttons.add(gE8Button);
        gE9Button.setName("GE9"); generalAdmission_buttons.add(gE9Button);
        gE10Button.setName("GE10"); generalAdmission_buttons.add(gE10Button);


        gF1Button.setName("GF1"); generalAdmission_buttons.add(gF1Button);
        gF2Button.setName("GF2"); generalAdmission_buttons.add(gF2Button);
        gF3Button.setName("GF3"); generalAdmission_buttons.add(gF3Button);
        gF4Button.setName("GF4"); generalAdmission_buttons.add(gF4Button);
        gF5Button.setName("GF5"); generalAdmission_buttons.add(gF5Button);
        gF6Button.setName("GF6"); generalAdmission_buttons.add(gF6Button);
        gF7Button.setName("GF7"); generalAdmission_buttons.add(gF7Button);
        gF8Button.setName("GF8"); generalAdmission_buttons.add(gF8Button);
        gF9Button.setName("GF9"); generalAdmission_buttons.add(gF9Button);
        gF10Button.setName("GF10"); generalAdmission_buttons.add(gF10Button);
    }

    public void populateComboBox() {
        availableConcerts = concertManager.getAvailableConcerts();
        // --- Box 1: Concert Selection ---
        // Clear it first to be safe (in case the GUI designer added example items)
        cmbox_selectConcert.removeAllItems();

//        availableConcerts = new ArrayList<>();
//        LocalDateTime concertDate = LocalDateTime.of(2025, 12,25,19,0);
//        Concert concert1 = new Concert("WildCats Pub Concert for a Cause", concertDate, "Zild", "CIT-U Gymnasium");
//        availableConcerts.add(concert1);

        // 2. Box 1: Concert Selection
//        cmbox_selectConcert.removeAllItems();
        for (Concert concert : availableConcerts) {
            // We add the name to the JComboBox
            cmbox_selectConcert.addItem(concert.getConcertName());
        }

        // 3. Set the initial details
        if (!availableConcerts.isEmpty()) {
            // Load details for the first concert by passing the object
            loadConcertDetails(availableConcerts.get(0));
        }

//        cmbox_selectConcert.removeAllItems();
//        cmbox_selectConcert.addItem(concert1.getConcertName());
        // --- Box 2: Filter Selection ---
        // 1. Clear existing items (removes the stuff you typed in the GUI Designer)
        cmbox_seatingTierFilter.removeAllItems();

        // 2. Add the items directly
        cmbox_seatingTierFilter.addItem("All");
        cmbox_seatingTierFilter.addItem("VVIP (PHP 600)");
        cmbox_seatingTierFilter.addItem("VIP (PHP 450)");
        cmbox_seatingTierFilter.addItem("General Admission (PHP 200)");
    }

    public void loadConcertDetails(Concert concert) { // Now accepts the concert object
        if (concert == null) return; // Safety check

        currentConcertName = concert.getConcertName();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

        String formattedDateTime = concert.getConcertDate().format(dtf); // Fixed: Use the passed 'concert' object

        details_artist.setText(concert.getArtist());
        details_concertDate.setText(formattedDateTime);
        details_venue.setText(concert.getVenue());
        perksLabel.setText("No specific perks listed. Check the official concert website.");

        // >>> START OF NEW LOGIC <<<
        // 1. Reset all seats to available state for the new concert
        resetAllSeats();

        // 2. Load the sold seats for the newly selected concert
        loadInfo(); // This will mark the sold seats as "TAKEN"
        // >>> END OF NEW LOGIC <<<
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
        if(currentConcertName == null || currentConcertName.isEmpty()){
            System.out.println("Concert name is empty");
            return;
        }
        // 1. Get the list of taken seats from the CSV
        List<String> takenSeats = TicketManager.loadSoldSeats(currentConcertName);

        // Debugging: Print to console so you can see what is happening
        System.out.println("DEBUG: Loaded " + takenSeats.size() + " seats from CSV for concert: "  + currentConcertName);

        // 2. Combine all buttons
        List<JButton> allButtons = new ArrayList<>();
        allButtons.addAll(vvip_buttons);
        allButtons.addAll(vip_buttons);
        allButtons.addAll(generalAdmission_buttons);

        // 3. Reset counter
        soldSeats = 0;

        // 4. Iterate and Update
        for (JButton btn : allButtons) {
            // Check if the button's internal name (the original seat ID) is in the list of taken seats
            if (takenSeats.contains(btn.getName())) { // CRITICAL: Check the button's Name, not its current Text
                // The Text might be "TAKEN" from a previous run, or the original seat ID

                System.out.println("DEBUG: Marking " + btn.getName() + " as TAKEN.");

                // VISUAL UPDATES
                btn.setText("TAKEN"); // <-- Set the text to TAKEN
                btn.setBackground(COLOR_SOLD);
                btn.setForeground(Color.WHITE);

                // Fix for specific OS (Mac/Windows) painting issues
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
                btn.setBorderPainted(false);

                soldSeats++;
            } else {
                // FIX: If it is not taken, make sure it is reset to its available state.
                // This is the safety net if resetAllSeats() was called but the button
                // wasn't fully cleaned up or if the ticket manager failed to find a record.

                // Check its name again and use the correct background color
                String originalSeatID = btn.getName();
                if (originalSeatID != null) {
                    btn.setText(originalSeatID);
                    if (vvip_buttons.contains(btn)) {
                        btn.setBackground(COLOR_VVIP);
                    } else if (vip_buttons.contains(btn)) {
                        btn.setBackground(COLOR_VIP);
                    } else {
                        btn.setBackground(COLOR_GEN);
                    }
                    btn.setForeground(Color.BLACK);
                    btn.setBorderPainted(true);
                }
            }
        }

        // 5. Update Progress Bar
        progressBar1.setValue(soldSeats);
        progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");

        // 6. FORCE REFRESH (Crucial Step)
        // This tells Java to redraw the panel immediately
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
        if(!name.matches("[\\p{L} '\\-]+")){
            return false;
        }

        return true;
    }

    private Customer createCustomerFromInput() {
        String name = enterFullNameTextField.getText().trim();
        String email = enterEmailAddressTextField.getText().trim();
        String ageText = enterAgeTextField.getText().trim();
        int age;

        //Name validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.");
            return null;
        }

        if (!checkName(name)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            return null;
        }

        // Email validation
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Email Address.");
            return null;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return null;
        }

        //Age validation

        if (ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your age", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            age = Integer.parseInt(ageText);
            if (age < 1) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return null;
            } else if (age > 90) {
                JOptionPane.showMessageDialog(this, "Individuals of this age are advised not to attend the concert", "Warning!", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        // All validations passed → create object
        return new Customer(name, email, age);
    }

    private void customizeVisuals() {
        // INCREASED SIZE: From 18 to 24 for the main headers
        Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
        Color headerColor = new Color(51, 153, 255); // Brand Blue

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
    private void styleScanButton() {
        if (btnScanQRCode == null) return;

        // 1. Smaller Font than the Confirm button (14 vs 18)
        btnScanQRCode.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 2. "Admin Blue" color to differentiate from "Success Green"
        Color adminBlue = new Color(0, 123, 255);
        btnScanQRCode.setBackground(adminBlue);
        btnScanQRCode.setForeground(Color.WHITE);

        // 3. UX touches
        btnScanQRCode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnScanQRCode.setFocusPainted(false);

        // 4. Smaller Padding (Compact look)
        btnScanQRCode.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(adminBlue, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20) // Smaller padding than confirm btn
                )
        );
    }

    private void styleDropdown() {
        if (cmbox_selectConcert == null) return;

        Color brandBlue = new Color(51, 153, 255);
        Color darkBackground = new Color(45, 48, 50); // Distinct dark background for the box

        // 1. Style the Main Box
        cmbox_selectConcert.setBackground(darkBackground);
        cmbox_selectConcert.setForeground(Color.WHITE);
        cmbox_selectConcert.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbox_selectConcert.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 2. Add the "Contrasting Stroke" (Border)
        cmbox_selectConcert.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(brandBlue, 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // External padding
        ));

        // 3. CUSTOM RENDERER (Controls the Dropdown List Appearance)
        cmbox_selectConcert.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Let the superclass handle the basic text setup
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // A. ALIGNMENT: Add 5px padding inside the dropdown items so text aligns
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                // B. COLORS
                if (isSelected) {
                    // When hovered/selected: Brand Blue
                    setBackground(brandBlue);
                    setForeground(Color.WHITE);
                } else {
                    // Unselected Items: Slightly lighter/different grey than the main background
                    // This makes the expanded menu visually distinct
                    setBackground(new Color(60, 63, 65));
                    setForeground(Color.WHITE);
                }
                return this;
            }
        });
    }

    private void styleProgressBar() {
        if (progressBar1 == null) return;

        // 1. MAKE IT THICKER (Height)
        // We set height to 30px.
        // The width (200) is just a placeholder; your Layout Manager will still stretch it to fill the row.
        progressBar1.setPreferredSize(new Dimension(200, 30));

        // 2. MAKE TEXT CLEARER
        // Bold font makes it stand out against the colored bar
        progressBar1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        progressBar1.setStringPainted(true); // Ensures text is shown

        // 3. ENHANCE COLORS
        // Fill Color: Your Brand Blue
        progressBar1.setForeground(new Color(51, 153, 255));
        // Background (Empty part): Darker grey for high contrast
        progressBar1.setBackground(new Color(45, 48, 50));
    }

    private void styleReadOnlyFields() {
        JTextField[] fields = {details_venue, details_concertDate, details_artist};

        // Style constants
        Color fieldBackground = new Color(60, 63, 65);
        Color fieldBorderColor = new Color(80, 80, 80);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        for (JTextField field : fields) {
            if (field == null) continue;

            field.setOpaque(true);
            field.setBackground(fieldBackground);
            field.setForeground(Color.WHITE);
            field.setFont(fieldFont);

            // --- ALIGNMENT FIX ---
            // We set the margin (padding) explicitly.
            // 12px on the left matches the Dropdown's text position.
            field.setMargin(new Insets(5, 12, 5, 5));

            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(fieldBorderColor, 1),
                    // We keep the empty border small because setMargin handles the text push
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)
            ));

            field.setEditable(false);
            field.setFocusable(false);
        }
    }

    private void adjustFonts() {
        // 1. DEFINE FONTS & COLORS
        // Labels are now 16px (Distinctly smaller than the 24px Headers)
        Font labelFontBig   = new Font("Segoe UI", Font.BOLD, 16);

        // Receipt details remain standard
        Font labelFontReg   = new Font("Segoe UI", Font.BOLD, 14);

        // Price remains the "Pop" element
        Font priceFont      = new Font("Segoe UI", Font.BOLD, 20);

        Color labelColor    = new Color(200, 200, 200); // Light Grey
        Color priceColor    = new Color(255, 85, 85);   // Bright Red

        // 2. GROUP 1: The Main Field Labels
        // These will be styled to 16px Bold
        JLabel[] bigLabels = {
                venueLbl, concertDateLbl, artistLbl,
                nameLbl, emailLbl, ageLbl,
                // Make sure these variables exist in your class:
                selectConcertLabel, capacityStatusLabel, filterTierLbl
        };

        for (JLabel lbl : bigLabels) {
            if (lbl != null) {
                lbl.setFont(labelFontBig);
                lbl.setForeground(labelColor);
            }
        }

        // 3. GROUP 2: Receipt Details (Smaller/Standard)
        JLabel[] regularBoldLabels = {
                perksLabel, selectedSeatLabel, seatTierLabel
        };

        for (JLabel lbl : regularBoldLabels) {
            if (lbl != null) {
                lbl.setFont(labelFontReg);
                lbl.setForeground(Color.WHITE);
            }
        }

        // 4. GROUP 3: Price Label
        if (priceLabel != null) {
            priceLabel.setFont(priceFont);
            priceLabel.setForeground(priceColor);
        }

        // 5. INPUT FIELDS
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        JTextField[] inputFields = {
                enterFullNameTextField, enterEmailAddressTextField, enterAgeTextField,
                details_venue, details_concertDate, details_artist
        };

        for (JTextField field : inputFields) {
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

        // 6. DROPDOWNS
        if (cmbox_selectConcert != null) cmbox_selectConcert.setFont(inputFont);
        if (cmbox_seatingTierFilter != null) cmbox_seatingTierFilter.setFont(inputFont);
    }

    public void resetAllSeats() {
        List<JButton> allButtons = new ArrayList<>();
        allButtons.addAll(vvip_buttons);
        allButtons.addAll(vip_buttons);
        allButtons.addAll(generalAdmission_buttons);

        // Reset the currently selected seat, if any
        if (selectedSeat != null) {
            resetSeatColor(selectedSeat); // Resets the color from the temporary COLOR_SELECTED
            selectedSeat = null; // Unselect the seat
        }
        // Clear the selection labels
        selectedSeatLabel.setText("~none");
        priceLabel.setText("PHP 0.00");
        seatTierLabel.setText("~none");
        perksLabel.setText("~none");


        // Reset all buttons to their default state
        for (JButton btn : allButtons) {
        // Use the button's internal Name property which we set in groupButtons()
            String originalSeatID = btn.getName();

        // Safety check: if name is not set, use a generic fallback.
        if (originalSeatID == null) {
            originalSeatID = "Seat"; // Keep this fallback just in case
        }

        btn.setText(originalSeatID); // <-- FIX: Sets the correct seat ID (e.g., VVA1)

        // Re-initialize the button based on its group
        if (vvip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VVIP);
        } else if (vip_buttons.contains(btn)) {
            btn.setBackground(COLOR_VIP);
        } else { // generalAdmission_buttons
            btn.setBackground(COLOR_GEN);
        }

        // Reset text color and styling
            btn.setForeground(Color.BLACK); // Set text color back to default
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
        }

        // Reset sold seat count and progress bar display
            soldSeats = 0;
            progressBar1.setValue(0);
            progressBar1.setString("0 / " + maximumCapacity + " Sold");

        // Recalculate and repaint the panel
            panelInsideScroll.revalidate();
            panelInsideScroll.repaint();
        }

}
