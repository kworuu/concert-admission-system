package ConcertAdmissionSystem;

import java.time.LocalDate;

public class VIP extends SeatingTier{
    public VIP(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold) {
        super(ticketID, price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
    }
}
