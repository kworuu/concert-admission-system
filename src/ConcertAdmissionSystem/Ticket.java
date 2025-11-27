package ConcertAdmissionSystem;

import java.time.LocalDate;

public class Ticket {
    private String ticketID;
    private double price;
    private Concert concert;
    private SeatingTier seating;
    private LocalDate timeBought;
    private Seat seat;

    public Ticket(String ticketID, double price, Concert concert, SeatingTier seating, Seat seat, LocalDate timeBought) {
        this.ticketID = ticketID;
        this.price = price;
        this.concert = concert;
        this.seating = seating;
        this.seat = seat;
        this.timeBought = timeBought;
    }

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

        return price;
    }

    void printTicket(){
        //andrei generate pdf and ticket tix eme-eme
    }
}
