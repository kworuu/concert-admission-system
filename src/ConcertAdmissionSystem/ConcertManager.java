package ConcertAdmissionSystem;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class ConcertManager {
    private static final ConcertManager INSTANCE = new ConcertManager();

    private final List<Concert> availableConcerts;

    private ConcertManager(){
        this.availableConcerts = new ArrayList<>();

        loadConcerts();
    }

    public static ConcertManager getInstance(){
        return INSTANCE;
    }

    private void loadConcerts(){
        LocalDateTime concertDate = LocalDateTime.of(2025,12,25,19,0);
        Concert concert1 = new Concert("Wildcats Pub Concert for a Cause", concertDate, "Zild", "CIT-U Gymnasium");
        Concert concert2 = new Concert("Test", concertDate, "Imong Mama", "Tisa Barangay Hall");

        this.availableConcerts.add(concert1);
        this.availableConcerts.add(concert2);
    }

    public List<Concert> getAvailableConcerts(){
        return availableConcerts;
    }

    public Concert getConcertByTitle(String title){
        for(Concert concert : availableConcerts){
            if(concert.getConcertName().equals(title)){
                return concert;
            }
        }
        return null;
    }
}
