package ConcertAdmissionSystem;

import java.time.LocalDate;

public class GenAd extends SeatingTier{
    public GenAd() {
        super("General Admission", 200.0, "None");
    }
//    public GenAd(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold) {
//        super(ticketID, price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
//    }
}
