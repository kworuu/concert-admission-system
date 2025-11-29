package ConcertAdmissionSystem;

import java.time.LocalDate;

public class VVIP extends SeatingTier{
    public VVIP(String tierName, double price, String perks) {
        super("VVIP", 600.0, "Free table and backstage access");
    }
}
