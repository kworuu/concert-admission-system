package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    private static final String FILE_PATH = "TicketsSold.csv";
    private static final String CSV_PATH = "concert-admission-system/ticketSold.csv";

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

    public static void saveTicket(String seat, String name, String email, String age, String price, String tier) {
        try {
            File file = new File(FILE_PATH);
            boolean isNewFile = !file.exists();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            if (isNewFile) {
                writer.write("SeatNumber,CustomerName,Email,Age,Price,Tier");
                writer.newLine();
            }

            String record = seat + "," + name + "," + email + "," + age + "," + price + "," + tier;

            writer.write(record);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving ticket: " + e.getMessage());
        }
    }

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
                if (data.length > 0) {
                    soldSeats.add(data[0].trim());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading tickets: " + e.getMessage());
        }
        return soldSeats;
    }
}
