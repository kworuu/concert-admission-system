package ConcertAdmissionSystem;

public class SeatingTier {
    private String tierName;
    private double basePrice;
    private String perks;
    private int maxCapacity;
    private int currentSold;

    public SeatingTier(String perks) {
        this.perks = perks;
    }

    void sellOneSeat() {

    }

    boolean isAvailable(){
     return false;
    }

    String getTierInfo(){
        return "";
    }

    int getRemainingCapacity(){
        return 0;
    }
}
