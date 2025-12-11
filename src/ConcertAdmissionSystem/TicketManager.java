package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages the storage and retrieval of sold seat data.
 * Each concert's sold seats are stored in a unique CSV file named
 * "[ConcertName]TicketsSold.csv".
 */
public class TicketManager {

    /**
     * Helper method to generate the file path based on the concert name.
     * @param concertName The name of the concert.
     * @return The Path object for the specific concert's ticket file.
     */
    private static Path getConcertFilePath(String concertName) {
        // Sanitize the name to be file-system friendly (replace non-alphanumeric/hyphen/dot characters with an underscore)
        String sanitizedName = concertName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = sanitizedName + "TicketsSold.csv";
        return Paths.get(fileName);
    }

    /**
     * Saves a newly purchased ticket record to the CSV file corresponding to the specified concert.
     * @param ticket The ticket object containing all purchase information.
     * @param concertName The name of the concert to save data for.
     */
    public static void saveTicket(Ticket ticket, String concertName) {
        Path filePath = getConcertFilePath(concertName);
        File file = filePath.toFile();

        try {
            // Check if the file exists and is empty to determine if the header is needed
            boolean isNewFile = !Files.exists(filePath) || Files.size(filePath) == 0;

            // Use FileWriter with true for append mode
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            if (isNewFile) {
                // Header must be written only once
                writer.write("SeatNumber,CustomerName,Email,Age,Price,Tier,TicketID");
                writer.newLine();
            }

            // Extract data directly from the Ticket object hierarchy
            // Note: Assuming getSeatNumber() and getSeating() methods exist in Ticket/Seat classes.
            String seat  = ticket.getSeat().getSeatNumber();
            String name  = ticket.getCustomer().getName();
            String email = ticket.getCustomer().getEmail();
            String age   = String.valueOf(ticket.getCustomer().getAge());
            String price = String.format("%.2f", ticket.getPrice());
            String tier  = ticket.getSeating().getTierName();
            String id    = ticket.getTicketID();

            // Create CSV record
            String record = String.join(",", seat, name, email, age, price, tier, id);

            writer.write(record);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.err.println("Error saving ticket: " + e.getMessage());
        }
    }

    /**
     * Loads the list of sold seat identifiers from the CSV file corresponding to the concert.
     * @param concertName The name of the concert to load data for.
     * @return A list of seat identifiers (e.g., ["VVA1", "VVB2"]).
     */
    public static List<String> loadSoldSeats(String concertName) {
        List<String> soldSeats = new ArrayList<>();
        Path filePath = getConcertFilePath(concertName);
        File file = filePath.toFile();

        if (!Files.exists(filePath)) {
            // File does not exist for this concert, return empty list
            return soldSeats;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] data = line.split(",");
                // The Seat Number (identifier) is at index 0
                if (data.length > 0) {
                    soldSeats.add(data[0].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading tickets: " + e.getMessage());
        }
        return soldSeats;
    }
}