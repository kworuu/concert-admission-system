package ConcertAdmissionSystem;

import java.time.LocalDateTime;
import java.util.Random;

public class Ticket {
    private String ticketID;
    private double price;
    private Concert concert;
    private SeatingTier seating;
    private LocalDateTime timeBought;
    private Seat seat;
    private Customer customer;

    public Ticket(Customer customer, double price, Concert concert, SeatingTier seating, Seat seat) {
        this.ticketID = generateTicketID();
        this.timeBought = LocalDateTime.now();
        this.customer = customer;
        this.price = price;
        this.concert = concert;
        this.seating = seating;
        this.seat = seat;
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

    public LocalDateTime getTimeBought() {
        return timeBought;
    }

    public Seat getSeat() {
        return seat;
    }

    public Customer getCustomer(){ 
        return customer; 
    }

    double calculateFinalPrice(){
        return price;
    }

    public void printTicket(){
        //andrei generate pdf and ticket tix eme-eme bayot
    }

    public void saveToFile(){
        String concertName = this.concert.getConcertName();
        TicketManager.saveTicket(this, concertName);
    }

    String generateTicketID(){
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++){
            char c = (char) ('A' + rand.nextInt(26));
            sb.append(c);
        }

        for(int i = 0; i < 7; i++){
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }
}
