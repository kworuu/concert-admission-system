package ConcertAdmissionSystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private JTextField details_concertDate;
    private JTextField details_artist;
    private JTextField textField1;
    private JLabel seatTierLabel;

    // Create Lists to Group Buttons
    private final List<JButton> vvip_buttons = new ArrayList<>();
    private final List<JButton> vip_buttons = new ArrayList<>();
    private final List<JButton> generalAdmission_buttons = new ArrayList<>();

    private List<Concert> availableConcerts;


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

        // Set the detail fields to read-only
        details_concertDate.setEditable(false);
        details_artist.setEditable(false);
        textField1.setEditable(false); // Assuming textField1 is the Venue field

        // You may also want to change the background to distinguish them
        // from editable fields, although setEditable(false) usually handles appearance.
        Color readOnlyBg = new Color(240, 240, 240); // A light gray color
        details_concertDate.setBackground(readOnlyBg);
        details_artist.setBackground(readOnlyBg);
        textField1.setBackground(readOnlyBg);
        // HOME notes 1 : Initialize the groups.
        groupButtons();
        populateComboBox();

        cmbox_selectConcert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) cmbox_selectConcert.getSelectedItem();

                // Find the corresponding Concert object in your list
                if (selectedName != null) {
                    for (Concert concert : availableConcerts) {
                        if (concert.getConcertName().equals(selectedName)) {
                            loadConcertDetails(concert); // Update the details panel
                            // loadInfo(); // You would call loadInfo() here to refresh sold seats
                            break;
                        }
                    }
                }
            }
        });

        // HOME notes 2 : COLOR INITIALIZATION & LISTENERES
        initalizeSeatButtons();

        // HOME notes 3 : Configure Progress Bar (Fixed at 70)
        progressBar1.setMinimum(0);
        progressBar1.setMaximum(maximumCapacity);
        progressBar1.setValue(1);
        progressBar1.setStringPainted(true);
        progressBar1.setString("0 / " + maximumCapacity + " Sold");

        // Load Info : DRI
        loadInfo();

        // HOME notes 4 : Add filtering functionality
        cmbox_seatingTierFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSeats();
            }
        });


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
            priceLabel.setText("PHP 600.00");
        } else if (vip_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 450.00");
        } else if (generalAdmission_buttons.contains(clickedButton)) {
            priceLabel.setText("PHP 200.00");
        }

        // update perks label
        if (vvip_buttons.contains(clickedButton)) {
            perksLabel.setText("Backstage Pass");
        } else if (vip_buttons.contains(clickedButton)) {
            perksLabel.setText("Free Table");
        } else if (generalAdmission_buttons.contains(clickedButton)) {
            perksLabel.setText("~none");
        }

        // update seatTier Label
        if (vvip_buttons.contains(clickedButton)) {
            seatTierLabel.setText("VVIP");
        } else if (vip_buttons.contains(clickedButton)) {
            seatTierLabel.setText("VIP");
        } else if (generalAdmission_buttons.contains(clickedButton)) {
            seatTierLabel.setText("General Admission");
        }
    }

    public void confirmPurchase() {
        if (selectedSeat == null) {
            JOptionPane.showMessageDialog(this, "Please select a seat first.", "Missing Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer customer = createCustomerFromInput();
        if (customer == null) {
            return; // validation failed, stop
        }

        int response = JOptionPane.showConfirmDialog(this, "Confirm purchase for seat?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // GET SEAT NUMBER BEFORE CHANGING TEXT
            String actualSeatNumber = selectedSeat.getText();

            // DETERMINE TIER AND PRICE
            String tierName = "";
            double price = 0.0;
            SeatingTier seatingTier = null;

            if (vvip_buttons.contains(selectedSeat)) {
                tierName = "VVIP";
                price = 2000.0;
                seatingTier = new SeatingTier(tierName, price, "VIP lounge access, meet & greet", 10);
            } else if (vip_buttons.contains(selectedSeat)) {
                tierName = "VIP";
                price = 1000.0;
                seatingTier = new SeatingTier(tierName, price, "Priority seating, backstage pass", 20);
            } else if (generalAdmission_buttons.contains(selectedSeat)) {
                tierName = "General Admission";
                price = 600.0;
                seatingTier = new SeatingTier(tierName, price, "Standard entry", 40);
            }


            // Extract row from seat number (e.g., "VVA1" -> row "A")
            String row = actualSeatNumber.substring(0, actualSeatNumber.length() - 1);
            Seat seat = new Seat(actualSeatNumber, row, seatingTier);

            // Get the selected concert
            String selectedConcertName = (String) cmbox_selectConcert.getSelectedItem();
            Concert selectedConcert = null;
            for (Concert c : availableConcerts) {
                if (c.getConcertName().equals(selectedConcertName)) {
                    selectedConcert = c;
                    break;
                }
            }

            // CREATE THE TICKET OBJECT
            Ticket ticket = new Ticket(customer, price, selectedConcert, seatingTier, seat);

            // CUSTOMER BUYS THE TICKET (SAVES TO CSV)
            customer.buyTicket(ticket);

            // UPDATE UI
            selectedSeat.setText("TAKEN");
            selectedSeat.setBackground(COLOR_SOLD);
            selectedSeat.setForeground(Color.WHITE);
            selectedSeat.setOpaque(true);
            selectedSeat.setBorderPainted(false);

            // UPDATE PROGRESS BAR
            soldSeats++;
            progressBar1.setValue(soldSeats);
            progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");

            // CLEAR FORM
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

        availableConcerts = new ArrayList<>();
        LocalDateTime concertDate = LocalDateTime.of(2025, 12,25,19,0);
        Concert concert1 = new Concert("WildCats Pub Concert for a Cause", concertDate, "Zild", "CIT-U Gymnasium");
        availableConcerts.add(concert1);

        // 2. Box 1: Concert Selection
        cmbox_selectConcert.removeAllItems();
        for (Concert concert : availableConcerts) {
            // We add the name to the JComboBox
            cmbox_selectConcert.addItem(concert.getConcertName());
        }

        // 3. Set the initial details
        if (!availableConcerts.isEmpty()) {
            // Load details for the first concert by passing the object
            loadConcertDetails(availableConcerts.get(0));
        }

        cmbox_selectConcert.removeAllItems();
        cmbox_selectConcert.addItem(concert1.getConcertName());
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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

        String formattedDateTime = concert.getConcertDate().format(dtf); // Fixed: Use the passed 'concert' object

        details_artist.setText(concert.getArtist());
        details_concertDate.setText(formattedDateTime);
        textField1.setText(concert.getVenue());
        perksLabel.setText("No specific perks listed. Check the official concert website.");

        // IMPORTANT: When a new concert is loaded, you must also reset the seat map
        // and soldSeats count, and then call loadInfo() to load the sold seats for
        // this specific concert (assuming TicketManager is updated to handle it).
    }
    public void filterSeats() {
        String selectedTier = (String) cmbox_seatingTierFilter.getSelectedItem();

        if(selectedTier == null) return;

        // filterSeats notes 1 : Helper method to make code cleaner
        boolean showAll = selectedTier.equals("All");
        boolean showVVIP = selectedTier.equals("VVIP (PHP 600)");
        boolean showVIP = selectedTier.equals("VIP (PHP 450)");
        boolean showGeneralAdmission = selectedTier.equals("General Admission (PHP 200)");

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

    // NEW METHOD: Reads the CSV and updates the UI
    public void loadInfo() {
        soldSeats = TicketManager.getSoldTicketCount();

        // 2. Get the list of taken seats from the CSV
        List<String> takenSeats = TicketManager.loadSoldSeats();

        // 3. Combine all buttons into one list to search them easily
        List<JButton> allButtons = new ArrayList<>();
        allButtons.addAll(vvip_buttons);
        allButtons.addAll(vip_buttons);
        allButtons.addAll(generalAdmission_buttons);

        // 4. Loop through every button and mark as taken
        for (JButton btn : allButtons) {
            if (takenSeats.contains(btn.getText())) {
                btn.setText("TAKEN");
                btn.setBackground(COLOR_SOLD);
                btn.setForeground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBorderPainted(false);
            }
        }

        // 5. Update the progress bar with the correct count from CSV
        progressBar1.setValue(soldSeats);
        progressBar1.setString(soldSeats + " / " + maximumCapacity + " Sold");
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

    private Customer createCustomerFromInput(){
        String name = enterFullNameTextField.getText().trim();
        String email = enterEmailAddressTextField.getText().trim();
        String ageText = enterAgeTextField.getText().trim();
        int age;

        //Name validation
        if(name.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter your Full Name.");
            return null;
        }

        if(!checkName(name)){
            JOptionPane.showMessageDialog(this, "Please enter a valid name.");
            return null;
        }

        // Email validation
        if(email.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter your Email Address.");
            return null;
        }

        if(!isValidEmail(email)){
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return null;
        }

        //Age validation

        if(ageText.isEmpty()){
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
}