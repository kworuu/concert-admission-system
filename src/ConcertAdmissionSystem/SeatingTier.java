package ConcertAdmissionSystem;

import java.time.LocalDate;

public class SeatingTier extends Ticket{
    private String tierName;
    private double basePrice;
    private String perks;
    private int maxCapacity;
    private int currentSold;

    public SeatingTier(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought, String tierName, double basePrice, String perks, int maxCapacity, int currentSold) {
        super(ticketID, price, concert, seating, seat, timeBought);
        this.tierName = tierName;
        this.basePrice = basePrice;
        this.perks = perks;
        this.maxCapacity = maxCapacity;
        this.currentSold = currentSold;
    }

    void sellOneSeat() {
        currentSold ++;
    }

    boolean isAvailable(){
        if(currentSold < maxCapacity){
            return true;
        }
        return false;
    }

    String getTierInfo(){
        return tierName + " will be able to access to: " + perks;
    }

    int getRemainingCapacity(){
        return maxCapacity - currentSold;
    }
}
