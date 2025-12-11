package ConcertAdmissionSystem;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private JTextField details_venue;
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
    private JScrollBar scrollBar1;

    // Logic Managers
    private ConcertManager concertManager;
    private SeatGridManager seatManager;
    private List<Concert> availableConcerts;
    private String currentConcertName;

    public ConcertAdmissionSystemUI() {
        setTitle("Concert Admission System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. Initialize Helpers
        seatManager = new SeatGridManager(selectedSeatLabel, priceLabel, perksLabel, seatTierLabel);
        concertManager = ConcertManager.getInstance();

        // 2. Apply Styles
        customizeAllVisuals();

        // 3. Setup Layout
        setupMainLayout();

        // 4. Initialize Data & Events
        initializeGroups(); // Adds buttons to manager
        populateComboBox();
        initializeListeners();

        // 5. Finalize Frame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        setLocationRelativeTo(null);
    }

    private void customizeAllVisuals() {
        // Delegate to UIStyler
        UIStyler.styleCTAButton(btnConfirmTicketDetails);
        UIStyler.styleScanButton(btnScanQRCode);
        UIStyler.styleDropdown(cmbox_selectConcert);
        UIStyler.styleDropdown(cmbox_seatingTierFilter);
        UIStyler.styleProgressBar(progressBar1);
        UIStyler.styleReadOnlyFields(details_venue, details_concertDate, details_artist);

        // Fonts
        JLabel[] bigLabels = {venueLbl, concertDateLbl, artistLbl, nameLbl, emailLbl, ageLbl, selectConcertLabel, capacityStatusLabel, filterTierLbl};
        JLabel[] regLabels = {perksLabel, selectedSeatLabel, seatTierLabel};
        JTextField[] inputs = {enterFullNameTextField, enterEmailAddressTextField, enterAgeTextField, details_venue, details_concertDate, details_artist};
        UIStyler.adjustFonts(bigLabels, regLabels, priceLabel, inputs);

        // Panels
        Font headerFont = new Font("Segoe UI", Font.BOLD, 24);
        Color headerColor = new Color(51, 153, 255);
        UIStyler.styleSectionPanel(concertDetailsPanel, "1. Concert Details", headerFont, headerColor);
        UIStyler.styleSectionPanel(customerInformationPanel, "2. Customer Information", headerFont, headerColor);
        UIStyler.styleSectionPanel(seatSelectionPanel, "3. Seat Selection", headerFont, headerColor);

        // Scroll fix
        if (scrollPanel != null) {
            scrollPanel.setPreferredSize(new Dimension(scrollPanel.getPreferredSize().width, 100));
            scrollPanel.setMinimumSize(new Dimension(0, 100));
        }
    }

    private void setupMainLayout() {
        JPanel mainWrapper = new JPanel(new BorderLayout());

        // Header
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));

        // Logo
        JLabel logoLabel = new JLabel();
        File svgFile = new File("src/assets/logos/logo4.svg");
        if (svgFile.exists()) {
            try {
                FlatSVGIcon svgIcon = new FlatSVGIcon(svgFile);
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

        // Title with Gradient
        JLabel titleLabel = new JLabel("Wildcats Pub Concerts") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(51, 153, 255), getPreferredSize().width, 0, new Color(147, 51, 234));
                g2.setPaint(gradient);
                FontMetrics fm = g2.getFontMetrics();
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), 0, y);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setPreferredSize(new Dimension(450, 80));

        topBar.add(logoLabel);
        topBar.add(titleLabel);
        mainWrapper.add(topBar, BorderLayout.NORTH);

        // Body
        JPanel bodyWrapper = new JPanel(new BorderLayout());
        bodyWrapper.add(contentpane, BorderLayout.CENTER);
        bodyWrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 100, 20));

        JScrollPane centerScrollPane = new JScrollPane(bodyWrapper);
        centerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        centerScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        centerScrollPane.setBorder(null);

        mainWrapper.add(centerScrollPane, BorderLayout.CENTER);
        setContentPane(mainWrapper);
    }

    private void initializeGroups() {
        // VVIP
        VVA1Button.setName("VVA1Button"); seatManager.addVVIP(VVA1Button);
        VVA2Button.setName("VVA2Button"); seatManager.addVVIP(VVA2Button);
        VVA3Button.setName("VVA3Button"); seatManager.addVVIP(VVA3Button);
        VVA4Button.setName("VVA4Button"); seatManager.addVVIP(VVA4Button);
        VVA5Button.setName("VVA5Button"); seatManager.addVVIP(VVA5Button);
        VVA6Button.setName("VVA6Button"); seatManager.addVVIP(VVA6Button);
        VVA7Button.setName("VVA7Button"); seatManager.addVVIP(VVA7Button);
        VVA8Button.setName("VVA8Button"); seatManager.addVVIP(VVA8Button);
        VVA9Button.setName("VVA9Button"); seatManager.addVVIP(VVA9Button);
        VVA10Button.setName("VVA10Button"); seatManager.addVVIP(VVA10Button);

        // VIP
        vB1Button.setName("VB1Button"); seatManager.addVIP(vB1Button);
        vB2Button.setName("VB2Button"); seatManager.addVIP(vB2Button);
        vB3Button.setName("VB3Button"); seatManager.addVIP(vB3Button);
        vB4Button.setName("VB4Button"); seatManager.addVIP(vB4Button);
        vB5Button.setName("VB5Button"); seatManager.addVIP(vB5Button);
        vB6Button.setName("VB6Button"); seatManager.addVIP(vB6Button);
        vB7Button.setName("VB7Button"); seatManager.addVIP(vB7Button);
        vB8Button.setName("VB8Button"); seatManager.addVIP(vB8Button);
        vB9Button.setName("VB9Button"); seatManager.addVIP(vB9Button);
        vB10Button.setName("VB10Button"); seatManager.addVIP(vB10Button);

        vC1Button.setName("VC1Button"); seatManager.addVIP(vC1Button);
        vC2Button.setName("VC2Button"); seatManager.addVIP(vC2Button);
        vC3Button.setName("VC3Button"); seatManager.addVIP(vC3Button);
        vC4Button.setName("VC4Button"); seatManager.addVIP(vC4Button);
        vC5Button.setName("VC5Button"); seatManager.addVIP(vC5Button);
        vC6Button.setName("VC6Button"); seatManager.addVIP(vC6Button);
        vC7Button.setName("VC7Button"); seatManager.addVIP(vC7Button);
        vC8Button.setName("VC8Button"); seatManager.addVIP(vC8Button);
        vC9Button.setName("VC9Button"); seatManager.addVIP(vC9Button);
        vC10Button.setName("VC10Button"); seatManager.addVIP(vC10Button);

        // GEN AD
        gD1Button.setName("GD1Button"); seatManager.addGen(gD1Button);
        gD2Button.setName("GD2Button"); seatManager.addGen(gD2Button);
        gD3Button.setName("GD3Button"); seatManager.addGen(gD3Button);
        gD4Button.setName("GD4Button"); seatManager.addGen(gD4Button);
        gD5Button.setName("GD5Button"); seatManager.addGen(gD5Button);
        gD6Button.setName("GD6Button"); seatManager.addGen(gD6Button);
        gD7Button.setName("GD7Button"); seatManager.addGen(gD7Button);
        gD8Button.setName("GD8Button"); seatManager.addGen(gD8Button);
        gD9Button.setName("GD9Button"); seatManager.addGen(gD9Button);
        gD10Button.setName("GD10Button"); seatManager.addGen(gD10Button);

        gE1Button.setName("GE1Button"); seatManager.addGen(gE1Button);
        gE2Button.setName("GE2Button"); seatManager.addGen(gE2Button);
        gE3Button.setName("GE3Button"); seatManager.addGen(gE3Button);
        gE4Button.setName("GE4Button"); seatManager.addGen(gE4Button);
        gE5Button.setName("GE5Button"); seatManager.addGen(gE5Button);
        gE6Button.setName("GE6Button"); seatManager.addGen(gE6Button);
        gE7Button.setName("GE7Button"); seatManager.addGen(gE7Button);
        gE8Button.setName("GE8Button"); seatManager.addGen(gE8Button);
        gE9Button.setName("GE9Button"); seatManager.addGen(gE9Button);
        gE10Button.setName("GE10Button"); seatManager.addGen(gE10Button);

        gF1Button.setName("GF1Button"); seatManager.addGen(gF1Button);
        gF2Button.setName("GF2Button"); seatManager.addGen(gF2Button);
        gF3Button.setName("GF3Button"); seatManager.addGen(gF3Button);
        gF4Button.setName("GF4Button"); seatManager.addGen(gF4Button);
        gF5Button.setName("GF5Button"); seatManager.addGen(gF5Button);
        gF6Button.setName("GF6Button"); seatManager.addGen(gF6Button);
        gF7Button.setName("GF7Button"); seatManager.addGen(gF7Button);
        gF8Button.setName("GF8Button"); seatManager.addGen(gF8Button);
        gF9Button.setName("GF9Button"); seatManager.addGen(gF9Button);
        gF10Button.setName("GF10Button"); seatManager.addGen(gF10Button);

        // Pass listener to manager
        seatManager.initializeButtons(e -> seatManager.handleSeatSelection((JButton) e.getSource(), this));
    }

    private void initializeListeners() {
        cmbox_selectConcert.addActionListener(e -> {
            String selectedName = (String) cmbox_selectConcert.getSelectedItem();
            if (selectedName != null) {
                Concert concert = concertManager.getConcertByTitle(selectedName);
                if (concert != null) loadConcertDetails(concert);
            }
        });

        cmbox_seatingTierFilter.addActionListener(e ->
                seatManager.filterSeats((String) cmbox_seatingTierFilter.getSelectedItem(), panelInsideScroll)
        );

        btnConfirmTicketDetails.addActionListener(e -> confirmPurchase());

        if (btnScanQRCode == null) btnScanQRCode = new JButton("Scan QR (Admin)");
        btnScanQRCode.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {
                TicketScanner scanner = new TicketScanner();
                scanner.pack();
                scanner.setLocationRelativeTo(null);
                scanner.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error launching scanner: " + ex.getMessage());
            }
        }));
    }

    private void populateComboBox() {
        availableConcerts = concertManager.getAvailableConcerts();
        cmbox_selectConcert.removeAllItems();
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
        currentConcertName = concert.getConcertName();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

        details_artist.setText(concert.getArtist());
        details_concertDate.setText(concert.getConcertDate().format(dtf));
        details_venue.setText(concert.getVenue());

        // Use Manager to reset and reload
        seatManager.resetAllSeats(progressBar1);
        seatManager.loadSoldInfo(currentConcertName, progressBar1, panelInsideScroll);
    }

    public void confirmPurchase() {
        JButton selected = seatManager.getSelectedSeat();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate Input using Validator
        Customer customer = InputValidator.validateAndCreateCustomer(this,
                enterFullNameTextField.getText().trim(),
                enterEmailAddressTextField.getText().trim(),
                enterAgeTextField.getText().trim());

        if (customer == null) return; // Validation failed

        Concert selectedConcert = concertManager.getConcertByTitle((String) cmbox_selectConcert.getSelectedItem());
        if (selectedConcert == null) return;

        // Gather Ticket Details
        Object[] details = seatManager.getSelectedSeatDetails();
        String tierName = (String) details[0];
        double price = (Double) details[1];
        String perks = (String) details[2];

        String actualSeatIdentifier = selected.getText();
        String row = String.valueOf(actualSeatIdentifier.charAt(0));

        // Create Objects
        SeatingTier tierObject = new SeatingTier(tierName, price, perks);
        Seat seat = new Seat(actualSeatIdentifier, row, tierObject);
        seat.markAsTaken();
        Ticket finalTicket = new Ticket(customer, price, selectedConcert, tierObject, seat);
        String ticketID = finalTicket.getTicketID();

        // Confirmation
        int response = JOptionPane.showConfirmDialog(this,
                String.format("Confirm purchase:\nName: %s\nSeat: %s (%s)\nPrice: PHP %.2f\nPerks: %s\nTicket ID: %s",
                        customer.getName(), actualSeatIdentifier, tierName, price, perks, ticketID),
                "Confirm Ticket Purchase", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            processTransaction(customer, finalTicket, ticketID);
        }
    }

    private void processTransaction(Customer customer, Ticket finalTicket, String ticketID) {
        PDFGenerator generator = new PDFGenerator();
        String pdfPath;
        try {
            pdfPath = generator.generateTicket(finalTicket);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Critical Error: Failed to generate PDF.", "PDF Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean emailSent = EmailSender.sendTicketEmail(customer.getEmail(), pdfPath, customer.getName(), ticketID);

        if (emailSent) {
            TicketManager.saveTicket(finalTicket, currentConcertName);
            seatManager.markSelectedAsSold();
            seatManager.resetAllSeats(null); // Just deselect visual
            seatManager.loadSoldInfo(currentConcertName, progressBar1, panelInsideScroll); // Refresh logic

            // Clear inputs
            enterFullNameTextField.setText("");
            enterEmailAddressTextField.setText("");
            enterAgeTextField.setText("");

            JOptionPane.showMessageDialog(this, "Ticket Confirmed! Check your email.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send email. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (pdfPath != null) {
            try { new File(pdfPath).delete(); } catch (Exception ignored) {}
        }
    }
}