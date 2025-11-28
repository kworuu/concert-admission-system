package ConcertAdmissionSystem;

import java.time.LocalDate;
import java.util.List;

public class Concert {
    private String concertName = "WildCats Pub Concert";
    private LocalDate concertDate = LocalDate.of(2025, 11, 27);
    private String artist = "Zild";
    private String venue = "WIldCats Pub";

    public String getConcertName() {
        return concertName;
    }

    public LocalDate getConcertDate() {
        return concertDate;
    }

    public String getArtist() {
        return artist;
    }

    public String getVenue() {
        return venue;
    }

}