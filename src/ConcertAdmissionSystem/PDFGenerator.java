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
        // Data

        String seatRow = ticket.getSeat().getRow();
        String seatNum = ticket.getSeat().getSeatNumber();
        String securityHash = ticket.getTicketID();
        String ticketType = ticket.getSeating().getTierName();

        // 2. Define the output file path/name
        String tempDir = System.getProperty("java.io.tmpdir");
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


            // --- 3. Rotated Seat Row ---
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
}