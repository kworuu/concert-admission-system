package ConcertAdmissionSystem;

import java.time.LocalDate;

public class Seat extends SeatingTier {
    private String seatNumber;
    private String row;
    private boolean isTaken;
    private SeatingTier tier;

    public Seat(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold, String seatNumber, String row, boolean isTaken, SeatingTier tier) {
        super(ticketID, price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
        this.seatNumber = seatNumber;
        this.row = row;
        this.isTaken = isTaken;
        this.tier = tier;
    }

    void markAsTaken(){
        isTaken = true;
    }

    boolean isAvailable(){
        return false;
    }

    String getSeatInfo() {
        return "Row " + row + ", Seat " + seatNumber + " [" + tier.getTierInfo() + "] - " + (isTaken ? "Taken" : "Available");
    }

}
