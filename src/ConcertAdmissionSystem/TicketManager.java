package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    private static final String FILE_PATH = "TicketSold.csv";
    /**
     * Saves a ticket object to CSV
     * @param ticket The ticket object containing all information
     */
    public static void saveTicket(Ticket ticket) {
        try {
            File file = new File(FILE_PATH);
            boolean isNewFile = !file.exists();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            if (isNewFile) {
                writer.write("Ticket ID,Seat Number,Customer Name,Email,Age,Price,Tier,Concert Name,Purchase Date Time");
                writer.newLine();
            }

            Customer customer = ticket.getCustomer();
            Concert concert = ticket.getConcert();
            Seat seat = ticket.getSeat();
            SeatingTier tier = ticket.getSeating();

            String record = String.join(",",
                    ticket.getTicketID(),
                    seat.getSeatNumber(),
                    customer.getName(),
                    customer.getEmail(),
                    String.valueOf(customer.getAge()),
                    String.format("%.2f", ticket.getPrice()),
                    tier.getTierName(),
                    concert.getConcertName(),
                    ticket.getTimeBought().toString()
            );

            writer.write(record);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving ticket: " + e.getMessage());
        }
    }

    /**
     * Loads all sold seats from CSV
     * @return List of seat numbers that are sold
     */
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
                if (data.length > 1) {
                    soldSeats.add(data[1].trim()); // Index 1 is SeatNumber
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tickets: " + e.getMessage());
        }
        return soldSeats;
    }

    /**
     * Gets the total count of sold tickets for progress bar
     * @return Number of tickets sold
     */
    public static int getSoldTicketCount() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return 0;
        }

        int count = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                count++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error counting tickets: " + e.getMessage());
        }
        return count;
    }

    /**
     * Loads all tickets for a specific concert
     * @param concertName The name of the concert
     * @return List of sold seat numbers for that concert
     */
    public static List<String> loadSoldSeatsByConcert(String concertName) {
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
                if (data.length > 7) {
                    String concert = data[7].trim(); // Index 7 is ConcertName
                    if (concert.equals(concertName)) {
                        soldSeats.add(data[1].trim()); // Index 1 is SeatNumber
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tickets by concert: " + e.getMessage());
        }
        return soldSeats;
    }
}