package ConcertAdmissionSystem;

import java.time.LocalDate;

public class GenAd extends SeatingTier{
    public GenAd(Ticket ticket, String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold) {
        super(ticket, ticketID, price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
    }
//    public GenAd(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold) {
//        super(ticketID, price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
//    }
}
