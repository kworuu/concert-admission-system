package ConcertAdmissionSystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Concert {
    private String concertName;
    private LocalDateTime concertDate;
    private String artist;
    private String venue;

    public Concert(String concertName, LocalDateTime concertDate, String artist, String venue) {
        this.concertName = concertName;
        this.concertDate = concertDate;
        this.artist = artist;
        this.venue = venue;
    }

    public String getConcertName() {
        return concertName;
    }

    public LocalDateTime getConcertDate() {
        return concertDate;
    }

    public String getArtist() {
        return artist;
    }

    public String getVenue() {
        return venue;
    }

}