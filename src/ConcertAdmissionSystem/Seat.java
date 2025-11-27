package ConcertAdmissionSystem;

public class Seat {
    private String seatNumber;
    private String row;
    private boolean isTaken;
    private SeatingTier tier;

    void markAsTaken(){

    }

    boolean isAvailable(){
        return false;
    }

    String getSeatInfo(){
        return "";
    }
}
