package ConcertAdmissionSystem;

public class Seat {
    private final String seatNumber;
    private final String row;
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

    public String getRow() {
        return row;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public SeatingTier getTier() {
        return tier;
    }

    public void markAsTaken(){
        if(isTaken) {
            throw new IllegalStateException("Seat " + seatNumber + " is already taken");
        }
        isTaken = true;
    }

    public boolean isAvailable(){
        return !isTaken;
    }

    public String getSeatInfo() {
        return "Row " + row + ", Seat " + seatNumber + " [" + tier.getTierInfo() + "] - " + (isTaken ? "Taken" : "Available");
    }

}