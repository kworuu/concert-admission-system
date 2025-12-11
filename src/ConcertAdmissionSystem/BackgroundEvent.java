package ConcertAdmissionSystem;

import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URL;

public class BackgroundEvent extends PdfPageEventHelper {

    private String ticketType;
    private static final int SECOND_PAGE = 2; // Constant for the second page number

    // Constructor to receive the ticket type
    public BackgroundEvent(String type) {
        this.ticketType = type;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        int pageNumber = writer.getPageNumber();

        try {
            PdfContentByte canvas = writer.getDirectContentUnder();
            canvas.saveState();

            String filename;
            if (pageNumber == SECOND_PAGE) {
                filename = determineSecondPageImageName(this.ticketType);
            } else {
                filename = determineImageName(this.ticketType);
            }

            Image background = loadImageFromResources(filename);

            float documentWidth = document.getPageSize().getWidth();
            float documentHeight = document.getPageSize().getHeight();

            // Set the bottom-left corner of the image to the bottom-left corner of the page (0, 0)
            background.setAbsolutePosition(0, 0);
            background.scaleAbsolute(documentWidth, documentHeight);

            canvas.addImage(background);
            canvas.restoreState();

        } catch (IOException | DocumentException e) {
            // Handle error: Failed to load image or add to document
            System.err.println("Failed to load or add background image for ticket type: " + this.ticketType
                    + " on page " + pageNumber);
            e.printStackTrace();
        }
    }

    /**
     * Maps the ticket type string to the local file path of the *first page* image.
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

    /**
     * Maps the ticket type string to the local file path of the *second page* image.
     * This is the new method you requested.
     */
    private String determineSecondPageImageName(String type) {
        String normalizedType = type.toUpperCase().trim();

        switch (normalizedType) {
            case "VIP":
                return "p2_VIP.png";
            case "VVIP":
                return "p2_VVIP.png";
            default:
                // for GenADD BG
                return "p2_GenAdd.png";
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