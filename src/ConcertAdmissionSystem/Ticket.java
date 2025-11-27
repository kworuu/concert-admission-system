package ConcertAdmissionSystem;

import java.time.LocalDate;

public class Ticket {
    private String ticketID;
    private double price;
    private Concert concert;
    private SeatingTier seating;
    private LocalDate timeBought;
    private Seat seat;

    public String getTicketID() {
        return ticketID;
    }

    public double getPrice() {
        return price;
    }

    public Concert getConcert() {
        return concert;
    }

    public SeatingTier getSeating() {
        return seating;
    }

    public LocalDate getTimeBought() {
        return timeBought;
    }

    public Seat getSeat() {
        return seat;
    }

    double calculateFinalPrice(){

        return 0;
    }

    void printTicket(){

    }
}
