package ConcertAdmissionSystem;

import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter; // <-- CRITICAL: ADD THIS IMPORT
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URL;

public class BackgroundEvent extends PdfPageEventHelper {

    private String ticketType; // e.g., "GEN ADD", "VIP", "VVIP"

    // Constructor to receive the ticket type
    public BackgroundEvent(String type) {
        this.ticketType = type;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        try {
            // 1. Get the 'under' content layer
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();

            // 2. Determine the path for the correct image
            String filename = determineImageName(this.ticketType);

            // 3. Load the Image instance from the file path
            Image background = loadImageFromResources(filename);

            // 4. Size & position
            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();

            // Set the bottom-left corner of the image to the bottom-left corner of the page (0, 0)
            background.setAbsolutePosition(0, 0);
            background.scaleAbsolute(documentWidth, documentHeight);

            canvas.addImage(background);
            canvas.restoreState();

        } catch (IOException | DocumentException e) {
            // Handle error: Failed to load image or add to document
            System.err.println("Failed to load or add background image for ticket type: " + this.ticketType);
            e.printStackTrace();
        }
    }

    /**
     * Maps the ticket type string to the local file path of the corresponding image.
     */
    private String determineImageName(String type) {
        // Normalize the type string for reliable comparison
        String normalizedType = type.toUpperCase().trim();

        switch (normalizedType) {
            case "VIP":
                return "VIP.png";
            case "VVIP":
                return "VVIP.png";
            default:
                // for GenADD BG
                return "GEN-ADD.png";
        }
    }

    private Image loadImageFromResources(String filename) throws IOException, DocumentException {
        String resourcePath = "/assets/ticket-background/" + filename;
        URL resourceUrl = getClass().getResource(resourcePath);

        if(resourceUrl == null) {
            throw new IOException("Image not found in resources: " + resourcePath);
        }

        return Image.getInstance(resourceUrl);
    }
}