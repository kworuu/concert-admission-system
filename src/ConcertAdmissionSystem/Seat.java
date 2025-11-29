package ConcertAdmissionSystem;

import java.time.LocalDate;

public class Seat {
    private String seatNumber;
    private String row;
    private boolean isTaken;
    private SeatingTier tier;

    public Seat(String seatNumber, String row, SeatingTier tier) {
//        super(price, concert, seating, seat, timeBought, tierName, basePrice, perks, maxCapacity, currentSold);
        this.seatNumber = seatNumber;
        this.row = row;
        this.isTaken = false;
        this.tier = tier;
    }

    public String getSeatNumber() {
        return seatNumber;
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
