package ConcertAdmissionSystem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    // The Master File (Used by the Scanner to check ALL tickets)
    private static final String MASTER_CSV_PATH = "MasterTicketList.csv";

    // Helper to generate the unique file path for a specific concert (Used by UI)
    private static Path getConcertFilePath(String concertName) {
        String sanitizedName = concertName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return Paths.get(sanitizedName + "TicketsSold.csv");
    }

    public static String generateVerificationHash(String ticketID, String email, String seatNum) {
        try {
            String secret = "WILDCATS_SECRET_2025"; // Secret Key
            String data = ticketID + email + seatNum + secret;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().substring(0, 16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    // --- SCANNER METHODS (Read from MASTER_CSV_PATH) ---

    public static boolean isTicketUsed(String ticketID) {
        try (BufferedReader br = new BufferedReader(new FileReader(MASTER_CSV_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Column 0 is ID, Column 8 is Status
                if (parts.length > 8 && parts[0].equals(ticketID)) {
                    return parts[8].equalsIgnoreCase("USED");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Master ticket file not found yet.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifyTicket(String ticketID, String verificationHash) {
        try (BufferedReader br = new BufferedReader(new FileReader(MASTER_CSV_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Column 0 is ID, Column 1 is Hash
                if (parts.length > 1 && parts[0].equals(ticketID)) {
                    return parts[1].equalsIgnoreCase(verificationHash);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void markTicketAsUsed(String ticketID) {
        File file = new File(MASTER_CSV_PATH);
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 8 && parts[0].equals(ticketID)) {
                    parts[8] = "USED"; // Update Status at index 8
                    found = true;
                    System.out.println("Ticket " + ticketID + " marked as USED.");
                }
                newLines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String s : newLines) {
                    bw.write(s);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // --- UI METHODS (Save to BOTH files) ---

    public static void saveTicket(Ticket ticket, String concertName) {
        String id = ticket.getTicketID();
        String seat = ticket.getSeat().getSeatNumber();
        String name = ticket.getCustomer().getName();
        String email = ticket.getCustomer().getEmail();
        String age = String.valueOf(ticket.getCustomer().getAge());
        String price = String.format("%.2f", ticket.getPrice());
        String tier = ticket.getSeating().getTierName();
        String status = "ACTIVE";
        // Generate Hash locally so it is saved in the CSV
        String hash = generateVerificationHash(id, email, seat);

        // Standard CSV Record Format:
        // 0:ID, 1:Hash, 2:Seat, 3:Name, 4:Email, 5:Age, 6:Price, 7:Tier, 8:Status
        String record = String.join(",", id, hash, seat, name, email, age, price, tier, status);

        // 2. Save to Master File (For Scanner)
        saveToCsv(Paths.get(MASTER_CSV_PATH), record);

        // 3. Save to Concert Specific File (For UI "Taken" Seats)
        saveToCsv(getConcertFilePath(concertName), record);
    }

    private static void saveToCsv(Path path, String record) {
        try {
            // Check if file doesn't exist OR if it is empty (0 bytes)
            boolean isNewOrEmpty = !Files.exists(path) || Files.size(path) == 0;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
                if (isNewOrEmpty) {
                    // Write Header only if file is new or completely empty
                    writer.write("TicketID,Hash,SeatNumber,CustomerName,Email,Age,Price,Tier,Status");
                    writer.newLine();
                }
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + path + " - " + e.getMessage());
        }
    }

    public static List<String> loadSoldSeats(String concertName) {
        List<String> soldSeats = new ArrayList<>();
        Path filePath = getConcertFilePath(concertName);

        if (!Files.exists(filePath)) {
            return soldSeats;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // In our new format, SeatNumber is at Index 2
                if (data.length > 2) {
                    soldSeats.add(data[2].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading sold seats: " + e.getMessage());
        }
        return soldSeats;
    }
}