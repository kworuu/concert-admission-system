package ConcertAdmissionSystem;

import java.time.LocalDate;
import java.util.List;

public class Concert {
    private String concertName;
    private LocalDate concertDate;
    private String artist;
    private int totalTicketsSold;
    private String venue;
    private List<SeatingTier> seatingTierList;

    String getConcertInfo(){
        return "";
    }

    void incrementSoldCount(){

    }

    int getTotalTicketsSold(){
        return totalTicketsSold;
    }
}
