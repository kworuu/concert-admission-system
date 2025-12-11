package ConcertAdmissionSystem;

import java.time.LocalDate;

public class SeatingTier {
    private String tierName;
    private double price;
    private String perks;

    public SeatingTier(String tierName, double price, String perks) {
        this.tierName = tierName;
        this.price = price;
        this.perks = perks;
    }

    public double getPrice() {
        return price;
    }

    public String getPerks() {
        return perks;
    }

    public String getTierName() {
        return tierName;
    }

    String getTierInfo(){
        return String.format("%s - PHP %.2f (Perks: %s)", tierName, price, perks);
    }

    @Override
    public String toString() {
        return String.format("%s - PHP %.2f (Perks: %s)", tierName, price, perks);
    }
}