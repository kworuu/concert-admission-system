package ConcertAdmissionSystem;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont; // <-- ADDED: Needed for direct canvas font setting
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException; // <-- ADDED: Needed for BaseFont.createFont
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class PDFGenerator {

    public String generateTicket(Ticket ticket) {
        // 1. Extract necessary data (including the ticket type)
        String name = ticket.getCustomer().getName();
        String seatRow = ticket.getSeat().getRow();
        String seatNum = ticket.getSeat().getSeatNumber();
        String securityHash = ticket.getTicketID();
        String concertName = ticket.getConcert().getConcertName(); // Added to match original extract list
        String ticketType = ticket.getSeating().getTierName();

        // 2. Define the output file path/name
        String tempDir = System.getProperty("java.io.tmpdir");
        // 2. Generate verification hash
        String verificationHash = generateVerificationHash(ticket);

        // 3. Generate QR code and get its path
        String qrCodePath = generateQRCode(ticket, verificationHash);

        if (qrCodePath == null) {
            System.err.println("⚠️ Failed to generate QR code, continuing without it");
        }

        // 4. Define the output file path
        String ticketsDir = "tickets";
        new File(ticketsDir).mkdirs();

        String filename = "Ticket_" + securityHash + ".pdf";
        File file = new File(tempDir, filename);
        String absolutePath = file.getAbsolutePath();

        // Customize size for pdf
        Rectangle customSize = new Rectangle(612f, 198f);
        Document document = new Document(customSize);
        PdfWriter writer = null;

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(absolutePath));

            // Set the PageEvent (BackgroundEvent class is assumed to exist)
            BackgroundEvent backgroundEvent = new BackgroundEvent(ticketType);
            writer.setPageEvent(backgroundEvent);

            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText ct;

            // --- 1. WildCats Concert Name (Static Header) ---
            ct = new ColumnText(canvas);
            Font WildcatsPub = new Font(Font.FontFamily.HELVETICA, 40f, Font.UNDEFINED, BaseColor.WHITE);
            Paragraph concertHeader = new Paragraph("WildCats", WildcatsPub);
            ct.setSimpleColumn(concertHeader, 20.16f, 21.76f, 300f, 135f, 10f, Element.ALIGN_LEFT);
            ct.go();

            // --- 2. WildCats Concert Name (Static Header) ---
            ct = new ColumnText(canvas);
            Font Pub = new Font(Font.FontFamily.HELVETICA, 40f, Font.UNDEFINED, BaseColor.WHITE);
            Paragraph PubHeader = new Paragraph("Pub", WildcatsPub);
            ct.setSimpleColumn(PubHeader, 20.16f, 21.76f, 300f, 90f, 10f, Element.ALIGN_LEFT);
            ct.go();


            // --- 3. Rotated Seat Row (SEAT NUM STYLED FIX) ---

            // Define the font for the rotated seat number (REQUIRED FIX)
            BaseFont bfSeatRow = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatRow, 19); // Set a clear font size
            canvas.setColorFill(BaseColor.BLACK); // Ensure color is set

            // Coordinates for the lower position (from Y=0.65in)
            float Rllx = 574.56f;
            float Rlly = 100.8f;
            float Rurx = 601.92f;
            float Rury = 100.23f; // Corrected from 69.23f (was a slight calculation error)

            float RcenterX = ((Rllx + Rurx) / 2f) ; // Midpoint X
            float RcenterY = ((Rlly + Rury) / 2f) ; // Midpoint Y

            // Draw the text directly onto the canvas with rotation
            canvas.beginText();
            canvas.showTextAligned(
                    Element.ALIGN_CENTER,
                    seatRow,
                    RcenterX,
                    RcenterY,
                    90f
            );
            canvas.endText();


            // --- 4. Rotated Seat Number (SEAT NUM STYLED FIX) ---

            // Define the font for the rotated seat number (REQUIRED FIX)
            BaseFont bfSeatNum = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatNum, 19); // Set a clear font size
            canvas.setColorFill(BaseColor.BLACK); // Ensure color is set

            // Coordinates for the lower position (from Y=0.65in)
            float llx = 574.56f;
            float lly = 163.8f;
            float urx = 601.92f;
            float ury = 163.23f; // Corrected from 69.23f (was a slight calculation error)

            float centerX = (llx + urx) / 2f; // Midpoint X
            float centerY = (lly + ury) / 2f; // Midpoint Y

            // Draw the text directly onto the canvas with rotation
            canvas.beginText();
            canvas.showTextAligned(
                    Element.ALIGN_CENTER,
                    seatNum,
                    centerX,
                    centerY,
                    90f
            );
            canvas.endText();

            // --- 5. Rotated TicketID (SEAT NUM STYLED FIX) ---

            // Define the font for the rotated seat number (REQUIRED FIX)
            BaseFont bfSeatID = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatID, 5); // Set a clear font size
            canvas.setColorFill(BaseColor.BLACK); // Ensure color is set

            // Coordinates for the lower position (from Y=0.65in)


            float TcenterX = 541.50f; // Midpoint X
            float TcenterY = 87f; // Midpoint Y

            // Draw the text directly onto the canvas with rotation
            canvas.beginText();
            canvas.showTextAligned(
                    Element.ALIGN_CENTER,
                    securityHash,
                    TcenterX,
                    TcenterY,
                    90f
            );
            canvas.endText();

            // --- NEW: ADD QR CODE TO TICKET ---
            if (qrCodePath != null) {
                try {
                    Image qrImage = Image.getInstance(qrCodePath);

                    // Position QR code on the left side of your ticket
                    // Adjust these coordinates based on your design
                    float qrX = 350f;  // X position (from left)
                    float qrY = 24f;   // Y position (from bottom)
                    float qrSize = 150f; // QR code size

                    qrImage.setAbsolutePosition(qrX, qrY);
                    qrImage.scaleAbsolute(qrSize, qrSize);

                    document.add(qrImage);

                    System.out.println("✅ QR code added to PDF at position (" + qrX + ", " + qrY + ")");

                } catch (Exception e) {
                    System.err.println("⚠️ Failed to add QR code to PDF: " + e.getMessage());
                }
            }

        } catch (DocumentException | IOException e) {
            // Added IOException for BaseFont.createFont
            System.err.println("CRITICAL ERROR: Failed to initialize or write to PDF document.");
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF ticket due to document error.", e);

        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        return absolutePath;
    }

    private String generateVerificationHash(Ticket ticket) {
        try {
            String secret = "WILDCATS_SECRET_2025";
            String data = ticket.getTicketID() +
                    ticket.getCustomer().getEmail() +
                    ticket.getSeat().getSeatNumber() +
                    secret;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString().substring(0, 16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String generateQRCode(Ticket ticket, String verificationHash) {
        String qrData = String.format("%s|%s|%s|%s",
                ticket.getTicketID(),
                verificationHash,
                ticket.getSeat().getSeatNumber(),
                ticket.getCustomer().getEmail()
        );

        String folderPath = "tickets" + File.separator + "qr";
        String filename = "QR_" + ticket.getTicketID() + ".png";
        String fullPath = folderPath + File.separator + filename;

        // Create folder if doesn't exist
        new File(folderPath).mkdirs();

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            // Generate QR code with size 150x150 (good for your ticket)
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 150, 150, hints);
            Path path = FileSystems.getDefault().getPath(fullPath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("✅ QR Code generated: " + fullPath);
            return fullPath;

        } catch (WriterException | IOException e) {
            System.err.println("❌ Error generating QR code: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}