package ConcertAdmissionSystem;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BaseFont; // <-- ADDED: Needed for direct canvas font setting

import java.awt.*;
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
    /**
     * Helper method to draw the rotated seat row, seat number, and ticket ID.
     * @param canvas The PdfContentByte object for drawing.
     * @param seatRow The seat row string.
     * @param seatNum The seat number string.
     * @param securityHash The ticket ID string.
     * @throws DocumentException If font creation fails.
     * @throws IOException If font creation fails.
     */

    private void drawRotatedTicketData(PdfContentByte canvas, String seatRow, String seatNum, String securityHash)
            throws DocumentException, IOException {

        // --- 1. Rotated Seat Row ---
        // Declare BaseFont inside the method or as a static final field if performance is critical
        BaseFont bfSeatRow = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        canvas.setFontAndSize(bfSeatRow, 19);
        canvas.setColorFill(BaseColor.BLACK);

        float RcenterX = 588.24f;
        float RcenterY = 100.515f;

        canvas.beginText();
        canvas.showTextAligned(
                Element.ALIGN_CENTER,
                seatRow,
                RcenterX,
                RcenterY,
                90f
        );
        canvas.endText();


        // --- 2. Rotated Seat Number ---
        BaseFont bfSeatNum = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        canvas.setFontAndSize(bfSeatNum, 19);
        canvas.setColorFill(BaseColor.BLACK);

        float centerX = 588.24f;
        float centerY = 163.515f;

        canvas.beginText();
        canvas.showTextAligned(
                Element.ALIGN_CENTER,
                seatNum,
                centerX,
                centerY,
                90f
        );
        canvas.endText();

        // --- 3. Rotated TicketID ---
        BaseFont bfSeatID = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
        canvas.setFontAndSize(bfSeatID, 5);
        canvas.setColorFill(BaseColor.BLACK);

        float TcenterX = 541.50f;
        float TcenterY = 87f;

        canvas.beginText();
        canvas.showTextAligned(
                Element.ALIGN_CENTER,
                securityHash,
                TcenterX,
                TcenterY,
                90f
        );
        canvas.endText();
    }

    public String generateTicket(Ticket ticket) {
        // Data

        String seatRow = ticket.getSeat().getRow();
        String seatNum = ticket.getSeat().getSeatNumber();
        String securityHash = ticket.getTicketID();
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

            // Set the PageEvent
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

            // --- 2. Pub Concert Name (Static Header) ---
            ct = new ColumnText(canvas);
            Font Pub = new Font(Font.FontFamily.HELVETICA, 40f, Font.UNDEFINED, BaseColor.WHITE);
            Paragraph PubHeader = new Paragraph("Pub", WildcatsPub);
            ct.setSimpleColumn(PubHeader, 20.16f, 21.76f, 300f, 90f, 10f, Element.ALIGN_LEFT);
            ct.go();


            // --- 3. Rotated Seat Row, Num, Ticket ID ---

            drawRotatedTicketData(canvas, seatRow, seatNum, securityHash);

            // second page for QR

            document.newPage();
            canvas = writer.getDirectContent();

            PdfContentByte underCanvas = writer.getDirectContentUnder();

            underCanvas.saveState();
            underCanvas.setColorFill(BaseColor.WHITE);

            underCanvas.rectangle(
                    0f, 0f,
                    document.getPageSize().getWidth(),
                    document.getPageSize().getHeight()
            );
            underCanvas.fill();
            underCanvas.restoreState();


            // --- NEW: ADD QR CODE TO TICKET ---
            if (qrCodePath != null) {
                try {
                    Image qrImage = Image.getInstance(qrCodePath);

                    // Position QR code on the left side of your ticket
                    float qrX = 280f;  // X position (from left)
                    float qrY = 22f;   // Y position (from bottom)
                    float qrSize = 150f; // QR code size

                    qrImage.setAbsolutePosition(qrX, qrY);
                    qrImage.scaleAbsolute(qrSize, qrSize);

                    document.add(qrImage);

                    System.out.println("✅ QR code added to PDF at position (" + qrX + ", " + qrY + ")");

                } catch (Exception e) {
                    System.err.println("⚠️ Failed to add QR code to PDF: " + e.getMessage());
                }
            }

            drawRotatedTicketData(canvas,seatRow, seatNum, securityHash);


        } catch (DocumentException | IOException e) {
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