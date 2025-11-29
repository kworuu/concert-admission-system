package ConcertAdmissionSystem;

import java.time.LocalDate;

public class GenAd extends SeatingTier{
    public GenAd(String tierName, double price, String perks) {
        super("General Admission", 200.0, "None");
    }

}
