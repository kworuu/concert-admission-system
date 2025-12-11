package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TicketManager {

    private static final String CSV_PATH =
            System.getProperty("user.dir") + File.separator + "ticketSold.csv";

    private static final String FILE_PATH =
            System.getProperty("user.dir") + File.separator + "TicketsSold.csv";

    private static Path getConcertFilePath(String concertName) {
        // Sanitize the name to be file-system friendly (replace non-alphanumeric/hyphen/dot characters with an underscore)
        String sanitizedName = concertName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String fileName = sanitizedName + "TicketsSold.csv";
        return Paths.get(fileName);
    }

    // Check if ticket was already used
    public static boolean isTicketUsed(String ticketID) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(ticketID)) {
                    return parts[4].equalsIgnoreCase("USED");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verify ticket exists + matching hash
    public static boolean verifyTicket(String ticketID, String verificationHash) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[0].equals(ticketID)) {
                    return parts[1].equalsIgnoreCase(verificationHash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mark ticket as used
    public static void markTicketAsUsed(String ticketID) {
        try {
            File file = new File(CSV_PATH);
            List<String> newLines = new ArrayList<>();

            // Rewrite CSV with updated "USED"
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts[0].equals(ticketID)) {
                        parts[4] = "USED";
                        System.out.println("Ticket marked as used: " + ticketID);
                    }

                    newLines.add(String.join(",", parts));
                }
            }

            // Write new CSV file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String s : newLines) {
                    bw.write(s);
                    bw.newLine();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

