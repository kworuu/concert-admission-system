package ConcertAdmissionSystem;

import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter; // <-- CRITICAL: ADD THIS IMPORT
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.IOException;

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
            String imagePath = determineImagePath(this.ticketType);

            // 3. Load the Image instance from the file path
            // Note: If you have issues loading from a path, ensure the path is an absolute file path,
            // not just a directory name.
            Image background = Image.getInstance(imagePath);

            // 4. Scale and Position the Image to fit the page exactly

            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();

            // Set the bottom-left corner of the image to the bottom-left corner of the page (0, 0)
            background.setAbsolutePosition(0, 0);

            // Scale the image to cover the entire page area
            background.scaleAbsolute(documentWidth, documentHeight);

            // 5. Add the image to the background canvas
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
    private String determineImagePath(String type) {
        // Normalize the type string for reliable comparison
        String normalizedType = type.toUpperCase().trim();

        // **!!! CRITICAL: FILE PATH CORRECTION !!!**
        // A path like "C:\\Users\\allea\\Downloads" is a directory, not a file.
        // You MUST append the actual file name (e.g., GEN-ADD.jpg) to the directory.
        String baseDir = "C:\\Users\\allea\\Downloads\\TicketTier\\"; // Ensure this directory exists and images are here

        switch (normalizedType) {

            case "VIP":
                return baseDir + "VIP.png";
            case "VVIP":
                return baseDir + "VVIP.png";
            default:
                // for GenADD BG
                return baseDir + "GEN-ADD.png";
        }
    }
}