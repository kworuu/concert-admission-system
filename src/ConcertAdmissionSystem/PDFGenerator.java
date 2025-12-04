package ConcertAdmissionSystem;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import java.io.File;           // ADDED: For file path
import java.io.FileOutputStream; // ADDED: For writing to disk
import java.io.FileNotFoundException;
// Removed: java.io.ByteArrayOutputStream (no longer needed)

public class PDFGenerator {

    /**
     * Generates the concert ticket PDF from a Ticket object, writes it to disk,
     * and returns the absolute file path.
     *
     * @param ticket The complete Ticket object to extract data from.
     * @return The String (absolute path) of the generated PDF file.
     * @throws RuntimeException if the PDF generation or writing fails.
     */
    public String generateTicket(Ticket ticket) { // <--- RETURN TYPE IS NOW String

        // 1. Extract necessary data from the Ticket object
        String name = ticket.getCustomer().getName();
        // Use getSeatInfo() which provides a good summary
        String seatInfo = ticket.getSeat().getSeatInfo();
        String concertName = ticket.getConcert().getConcertName();
        String securityHash = ticket.getTicketID();

        // 2. Define the output file path/name
        // Use the system's temporary directory for a reliable save location
        String tempDir = System.getProperty("java.io.tmpdir");
        String filename = "Ticket_" + securityHash + ".pdf";
        File file = new File(tempDir, filename);
        String absolutePath = file.getAbsolutePath(); // This is the path we must return

        Document document = new Document(PageSize.A4);

        try {
            // 3. Write PDF directly to the file on disk using the absolute path
            PdfWriter.getInstance(document, new FileOutputStream(absolutePath));
            document.open();

            // --- Add Content to the PDF using extracted data ---

            Paragraph title = new Paragraph("🎫 CONCERT ADMISSION TICKET 🎫");
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            document.add(new Paragraph("Concert: " + concertName));
            document.add(new Paragraph("Ticket Holder: " + name));
            document.add(new Paragraph("Seat Details: " + seatInfo));
            // Ensure the price formatting matches your system's currency (PHP)
            document.add(new Paragraph("Price: PHP " + String.format("%.2f", ticket.getPrice())));

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Ticket ID: " + securityHash));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Perks: " + ticket.getSeating().getPerks()));


        } catch (FileNotFoundException | DocumentException e) {
            System.err.println("CRITICAL ERROR: Failed to initialize or write to PDF document.");
            e.printStackTrace();
            // Wrap in RuntimeException to match the previous error handling style
            throw new RuntimeException("Failed to generate PDF ticket due to document error.", e);

        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }

        // CRITICAL: Return the absolute path of the file that was created
        return absolutePath;
    }
}