package ConcertAdmissionSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Manages the list of available concerts using the Singleton pattern.
 * Ensures only one instance exists to manage the master list of concerts.
 */
public class ConcertManager {

    // 1. Private static final instance of the manager (the Singleton)
    private static final ConcertManager INSTANCE = new ConcertManager();

    // 2a. Private instance variable to hold the list of concerts
    private final List<Concert> availableConcerts;

    // 3. Private Constructor: Prevents external classes from creating new instances
    public ConcertManager() {
        // Initialize the list when the manager is first created
        this.availableConcerts = new ArrayList<>();

        // Load the initial data
        loadConcerts();
    }

    // 4. Public static method to retrieve the single instance
    public static ConcertManager getInstance() {
        return INSTANCE;
    }

    // 5. Data Loading Logic: Populates the list with multiple concerts
    private void loadConcerts() {
        // Concert 1: The original one
        LocalDateTime concertDate1 = LocalDateTime.of(2025, 12, 25, 19, 0);
        Concert concert1 = new Concert("WildCats Pub Concert for a Cause", concertDate1, "Zild", "CIT-U Gymnasium");

        // Concert 2: The test concert
        LocalDateTime concertDate2 = LocalDateTime.of(2026, 1, 15, 20, 30);
        Concert concert2 = new Concert("Electro Dance Festival 2026", concertDate2, "DJ Tiesto & Friends", "Cebu Open Field");

        // Concert 3: A third example
        LocalDateTime concertDate3 = LocalDateTime.of(2026, 3, 10, 18, 0);
        Concert concert3 = new Concert("Classical Music Gala", concertDate3, "Cebu Philharmonic Orchestra", "SM Seaside Arena");


        this.availableConcerts.add(concert1);
        this.availableConcerts.add(concert2);
        this.availableConcerts.add(concert3);
    }

    // 6. Getter Method: Allows other classes to read the list
    /**
     * Returns an unmodifiable list of available concerts to prevent external modification.
     * @return A List of Concert objects.
     */
    public List<Concert> getAvailableConcerts() {
        return Collections.unmodifiableList(availableConcerts);
    }

    // 7. Utility Method: Easily find a specific concert by its title
    /**
     * Finds a Concert object based on its title.
     * @param title The title of the concert to search for.
     * @return The Concert object, or null if not found.
     */
    public Concert getConcertByTitle(String title) {
        for (Concert concert : availableConcerts) {
            if (concert.getConcertName().equals(title)) {
                return concert;
            }
        }
        return null; // Return null if no match is found
    }
}