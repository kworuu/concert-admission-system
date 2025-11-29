package ConcertAdmissionSystem;

import java.time.LocalDate;

public class Seat {
    private String seatNumber;
    private String row;
    private boolean isTaken;
    private SeatingTier tier;

    public Seat(SeatingTier tier, String seatNumber, String row) {
//        super(price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
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
