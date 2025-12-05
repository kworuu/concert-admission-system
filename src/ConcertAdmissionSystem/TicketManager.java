package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    private static final String FILE_PATH = "TicketsSoldOut.csv";

    // UPDATED: Now accepts a Ticket object instead of separate strings
    public static void saveTicket(Ticket ticket) {
        try {
            File file = new File(FILE_PATH);
            boolean isNewFile = !file.exists();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            if (isNewFile) {
                writer.write("SeatNumber,CustomerName,Email,Age,Price,Tier,TicketID");
                writer.newLine();
            }

            // Extract data directly from the Ticket object hierarchy
            String seat  = ticket.getSeat().getSeatNumber(); // Critical: Must match button text
            String name  = ticket.getCustomer().getName();
            String email = ticket.getCustomer().getEmail();
            String age   = String.valueOf(ticket.getCustomer().getAge());
            String price = String.format("%.2f", ticket.getPrice()); // Formats to 2000.00
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

    // This method remains the same for loading
    public static List<String> loadSoldSeats() {
        List<String> soldSeats = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return soldSeats;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                // The Seat Number is index 0
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