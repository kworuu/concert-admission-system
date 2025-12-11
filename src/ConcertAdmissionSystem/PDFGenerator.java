package ConcertAdmissionSystem;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont; // <-- ADDED: Needed for direct canvas font setting
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException; // <-- ADDED: Needed for BaseFont.createFont

public class PDFGenerator {

    public String generateTicket(Ticket ticket) {
        // 1. Extract necessary data
        String name = ticket.getCustomer().getName();
        String seatRow = ticket.getSeat().getRow();
        String seatNum = ticket.getSeat().getSeatNumber();
        String securityHash = ticket.getTicketID();
        String concertName = ticket.getConcert().getConcertName();
        String ticketType = ticket.getSeating().getTierName();

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
        File file = new File(ticketsDir, filename);
        String absolutePath = file.getAbsolutePath();

        // 5. Create PDF with custom size
        Rectangle customSize = new Rectangle(612f, 198f);
        Document document = new Document(customSize);
        PdfWriter writer = null;

        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(absolutePath));

            // Set background event
            BackgroundEvent backgroundEvent = new BackgroundEvent(ticketType);
            writer.setPageEvent(backgroundEvent);

            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            ColumnText ct;

            // --- EXISTING CONTENT (Your styled text) ---

            // 1. WildCats Concert Name
            ct = new ColumnText(canvas);
            Font WildcatsPub = new Font(Font.FontFamily.HELVETICA, 40f, Font.UNDEFINED, BaseColor.WHITE);
            Paragraph concertHeader = new Paragraph("WildCats", WildcatsPub);
            ct.setSimpleColumn(concertHeader, 20.16f, 21.76f, 300f, 135f, 10f, Element.ALIGN_LEFT);
            ct.go();

            // 2. "Pub" text
            ct = new ColumnText(canvas);
            Font Pub = new Font(Font.FontFamily.HELVETICA, 40f, Font.UNDEFINED, BaseColor.WHITE);
            Paragraph PubHeader = new Paragraph("Pub", WildcatsPub);
            ct.setSimpleColumn(PubHeader, 20.16f, 21.76f, 300f, 90f, 10f, Element.ALIGN_LEFT);
            ct.go();

            // 3. Rotated Seat Row
            BaseFont bfSeatRow = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatRow, 19);
            canvas.setColorFill(BaseColor.BLACK);

            float Rllx = 574.56f;
            float Rlly = 100.8f;
            float Rurx = 601.92f;
            float Rury = 100.23f;

            float RcenterX = (Rllx + Rurx) / 2f;
            float RcenterY = (Rlly + Rury) / 2f;

            canvas.beginText();
            canvas.showTextAligned(Element.ALIGN_CENTER, seatRow, RcenterX, RcenterY, 90f);
            canvas.endText();

            // 4. Rotated Seat Number
            BaseFont bfSeatNum = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatNum, 19);
            canvas.setColorFill(BaseColor.BLACK);

            float llx = 574.56f;
            float lly = 163.8f;
            float urx = 601.92f;
            float ury = 163.23f;

            float centerX = (llx + urx) / 2f;
            float centerY = (lly + ury) / 2f;

            canvas.beginText();
            canvas.showTextAligned(Element.ALIGN_CENTER, seatNum, centerX, centerY, 90f);
            canvas.endText();

            // 5. Rotated Ticket ID
            BaseFont bfSeatID = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(bfSeatID, 5);
            canvas.setColorFill(BaseColor.BLACK);

            float TcenterX = 541.50f;
            float TcenterY = 87f;

            canvas.beginText();
            canvas.showTextAligned(Element.ALIGN_CENTER, securityHash, TcenterX, TcenterY, 90f);
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
            System.err.println("CRITICAL ERROR: Failed to generate PDF ticket.");
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF ticket", e);

        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        System.out.println("✅ PDF ticket generated: " + absolutePath);
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