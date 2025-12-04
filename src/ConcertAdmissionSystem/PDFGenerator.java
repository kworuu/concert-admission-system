package ConcertAdmissionSystem;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import java.io.ByteArrayOutputStream;

public class PDFGenerator {

    /**
     * Generates the concert ticket PDF from a Ticket object.
     *
     * @param ticket The complete Ticket object to extract data from.
     * @return A byte array representing the generated PDF file.
     * @throws RuntimeException if the PDF generation fails.
     */
    public byte[] generateTicket(Ticket ticket) { // <--- MODIFIED SIGNATURE

        // 1. Extract necessary data from the Ticket object
        String name = ticket.getCustomer().getName(); // Assumes Customer has getName()
        String seatIdentifier = ticket.getSeat().getSeatInfo(); // Assumes Seat has getSeatIdentifier()
        String concertName = ticket.getConcert().getConcertName(); // Assumes Concert has getName()
        String securityHash = ticket.getTicketID();

        // 2. Declare variables for PDF generation
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // --- Add Content to the PDF using extracted data ---

            Paragraph title = new Paragraph("🎫 CONCERT ADMISSION TICKET 🎫");
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            document.add(new Paragraph("Concert: " + concertName));
            document.add(new Paragraph("Ticket Holder: " + name));
            document.add(new Paragraph("Seat: " + seatIdentifier));
            document.add(new Paragraph("Price: $" + ticket.getPrice())); // Optional: add more fields

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Security Hash: " + securityHash));

            return outputStream.toByteArray();

        } catch (DocumentException e) {
            System.err.println("CRITICAL ERROR: Failed to initialize or write to PDF document.");
            e.printStackTrace();
            throw new RuntimeException("Failed to generate PDF ticket due to document error.", e);

        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
    }
}