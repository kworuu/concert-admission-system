package ConcertAdmissionSystem;

import java.time.LocalDate;

public class VIP extends SeatingTier{
    public VIP(String tierName, double price, String perks) {
        super("VIP", 450.0, "Free table");
    }
}
