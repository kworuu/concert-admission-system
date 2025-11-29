package ConcertAdmissionSystem;

public class SeatingTier {
    private String tierName;
    private String perks;
    private double price;

    public SeatingTier(String tierName, double price, String perks) {
        this.tierName = tierName;
        this.price = price;
        this.perks = perks;
    }

    String getTierInfo(){
        return String.format("%s - PHP %.2f (Perks: %s)", tierName, price, perks);
    }

    @Override
    public String toString() {
        return String.format("%s - PHP %.2f (Perks: %s)", tierName, price, perks);
    }
}
