package ConcertAdmissionSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {

    private static final String FILE_PATH = "TicketsSold.csv";

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
