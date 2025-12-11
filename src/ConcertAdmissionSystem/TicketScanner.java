package ConcertAdmissionSystem;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TicketScanner extends JFrame implements Runnable, ThreadFactory {

    private Webcam webcam = null;
    private WebcamPanel panel = null;
    private JPanel resultPanel;
    private JLabel statusLabel;
    private JLabel ticketInfoLabel;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    private boolean scanning = true;
    private String lastScannedTicket = ""; // Prevent re-scanning same ticket

    public TicketScanner() {
        super();

        setTitle("🎫 WildCats Pub - Ticket Scanner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        java.util.List<Webcam> cams = Webcam.getWebcams();
        if (cams.size() > 1) {
            webcam = cams.get(1); // Set to Azurewave
        } else {
            webcam = Webcam.getDefault(); // Fallback if Azurewave moves to index 0
        }

        if (webcam == null) {
            JOptionPane.showMessageDialog(this,
                    "No webcam detected! Fallback to file selection mode.",
                    "No Webcam", JOptionPane.WARNING_MESSAGE);
            setupFileMode();
            return;
        }

        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        // Camera panel (top)
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(new Dimension(640, 480));
        panel.setFPSDisplayed(false);
        panel.setMirrored(true);
        add(panel, BorderLayout.CENTER);

        // Result panel (bottom)
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setPreferredSize(new Dimension(640, 150));
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        statusLabel = new JLabel("Scanning for QR codes...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ticketInfoLabel = new JLabel(" ", SwingConstants.CENTER);
        ticketInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ticketInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultPanel.add(statusLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(ticketInfoLabel);

        add(resultPanel, BorderLayout.SOUTH);

        // Instructions panel (top)
        JLabel instructions = new JLabel(
                "<html><div style='text-align:center; padding:10px;'>" +
                        "📱 Hold QR code in front of camera<br>" +
                        "System will automatically scan and verify" +
                        "</div></html>",
                SwingConstants.CENTER
        );
        instructions.setFont(new Font("Arial", Font.PLAIN, 12));
        instructions.setBackground(new Color(70, 130, 180));
        instructions.setForeground(Color.WHITE);
        instructions.setOpaque(true);
        add(instructions, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Start scanning thread
        executor.execute(this);
    }

    @Override
    public void run() {
        while (scanning) {
            try {
                Thread.sleep(100); // Scan every 100ms

                Result result = null;
                BufferedImage image = null;

                if (webcam != null && webcam.isOpen()) {
                    image = webcam.getImage();

                    if (image == null) {
                        continue;
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException e) {
                        // No QR code in frame - continue scanning
                        continue;
                    }
                }

                if (result != null) {
                    String qrData = result.getText();

                    // Prevent re-scanning same ticket immediately
                    if (qrData.equals(lastScannedTicket)) {
                        continue;
                    }

                    lastScannedTicket = qrData;
                    System.out.println("📱 QR Detected: " + qrData);

                    // Process ticket
                    SwingUtilities.invokeLater(() -> verifyTicket(qrData));

                    // Wait 3 seconds before scanning next ticket
                    Thread.sleep(3000);
                    lastScannedTicket = ""; // Reset after delay
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void verifyTicket(String qrData) {
        try {
            // Parse QR data: TICKETID|HASH|SEAT|EMAIL
            String[] parts = qrData.split("\\|");
            if (parts.length != 4) {
                showInvalidTicket("Invalid QR code format", "");
                return;
            }

            String ticketID = parts[0];
            String hash = parts[1];
            String seat = parts[2];
            String email = parts[3];

            System.out.println("🎫 Verifying Ticket ID: " + ticketID);

            // Check if already used
            if (TicketManager.isTicketUsed(ticketID)) {
                showInvalidTicket("TICKET ALREADY USED", ticketID);
                return;
            }

            // Verify ticket
            boolean isValid = TicketManager.verifyTicket(ticketID, hash);

            if (isValid) {
                // Mark as used
                TicketManager.markTicketAsUsed(ticketID);
                showValidTicket(ticketID, seat, email);
            } else {
                showInvalidTicket("Ticket not found or invalid", ticketID);
            }

        } catch (Exception e) {
            showInvalidTicket("Error: " + e.getMessage(), "");
            e.printStackTrace();
        }
    }

    private void showValidTicket(String ticketID, String seat, String email) {
        resultPanel.setBackground(new Color(76, 175, 80)); // Green
        statusLabel.setText("✅ VALID TICKET");
        statusLabel.setForeground(Color.WHITE);

        String info = String.format(
                "<html><div style='text-align: center; color: white;'>" +
                        "<p style='font-size: 16px;'><b>Ticket ID:</b> %s</p>" +
                        "<p style='font-size: 16px;'><b>Seat:</b> %s</p>" +
                        "<p style='font-size: 14px;'>%s</p>" +
                        "<p style='font-size: 20px; margin-top: 10px;'><b>✓ ADMIT GUEST</b></p>" +
                        "</div></html>",
                ticketID, seat, email
        );
        ticketInfoLabel.setText(info);
        ticketInfoLabel.setForeground(Color.WHITE);

        Toolkit.getDefaultToolkit().beep(); // Success beep

        // Reset after 3 seconds
        Timer timer = new Timer(3000, e -> resetDisplay());
        timer.setRepeats(false);
        timer.start();
    }

    private void showInvalidTicket(String reason, String ticketID) {
        resultPanel.setBackground(new Color(244, 67, 54)); // Red
        statusLabel.setText("❌ INVALID TICKET");
        statusLabel.setForeground(Color.WHITE);

        String info = String.format(
                "<html><div style='text-align: center; color: white;'>" +
                        "<p style='font-size: 14px;'>%s</p>" +
                        "%s" +
                        "<p style='font-size: 20px; margin-top: 10px;'><b>⛔ DO NOT ADMIT</b></p>" +
                        "</div></html>",
                reason,
                ticketID.isEmpty() ? "" : "<p style='font-size: 12px;'>ID: " + ticketID + "</p>"
        );
        ticketInfoLabel.setText(info);
        ticketInfoLabel.setForeground(Color.WHITE);

        // Reset after 3 seconds
        Timer timer = new Timer(3000, e -> resetDisplay());
        timer.setRepeats(false);
        timer.start();
    }

    private void resetDisplay() {
        resultPanel.setBackground(Color.WHITE);
        statusLabel.setText("🔍 Scanning for QR codes...");
        statusLabel.setForeground(Color.BLACK);
        ticketInfoLabel.setText(" ");
        ticketInfoLabel.setForeground(Color.BLACK);
    }

    private void setupFileMode() {
        // Fallback to file selection if no webcam
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("No webcam detected. Click to select QR image.",
                SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        JButton button = new JButton("Select QR Code Image");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("tickets/qr");
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                // Read and verify from file (similar to previous version)
            }
        });

        panel.add(label, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "Scanner-Thread");
        t.setDaemon(true);
        return t;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicketScanner());
    }
}