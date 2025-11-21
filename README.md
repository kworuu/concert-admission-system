# CONCERT ADMISSION SYSTEM

## Project Overview

The **Concert Ticket System** is a Java-based application designed to manage ticket sales for _concerts with multiple seating tiers_. Users can purchase tickets (could also be multiple tickets), select their desired seat, and view ticket details. The system is designed using **using object-oriented programming principles** (OOP) and includes a graphical user interface (GUI) for easier interaction.

This system models real-world concert operations, including:

- Customers buying tickets
- Different seating tiers (VIP, General Admission, VVIP)
- Seat assignments
- Concert details (artist, venue, date)

## Entities and Classes

  The system consists of the following main classes:

### Customer

  Represents a concert attendee who can purchase tickets.
  
  Fields:
  - name : String
  - email : String
  - age : int

  **Behavior:**
  A Customer can update their information, purchase tickets, and view their purchased tickets.
  A Customer also stores the connection between themselves and their tickets (composition — without the customer, their
  tickets do not exist).
  
### Ticket

  Represents a ticket purchased by a customer, linked to a concert, a seat, and a seating tier.

  Fields:
  - ticketID : String
  - customer : Customer
  - seat : Seat
  - seatingTier : SeatingTier
  - price : int

  **Behavior:**
  A Ticket can return its details, calculate its final price, and print its formatted ticket information.
  A Ticket exists because of its customer and concert — both are composition relationships.

### SeatingTier

  Represents different categories of seating (e.g., VIP, General Admission, VVIP) and their associated perks and capacities.

  Fields:
  - tierName : String
  - basePrice : int
  - perks : String
  - maxCapacity : int
  - currentSold : int

  **Behavior:**
  A seating tier can sell seats, check if there is available capacity, report remaining seats, and provide tier details.
  Seating tiers own seats via aggregation — they conceptually group seats, but seats can still exist independently in memory.

  **VIP, GA, and VVIP**
  These are subclasses of SeatingTier.
  They inherit all functionality from SeatingTier and do not introduce new fields or methods.
  They exist to categorize seating types and can be expanded later if needed (e.g., special perks or pricing rules).
  

### Concert

  Represents a concert event, incldugin the artist, date, venue, and seating tiers.

  Fields:
  - concertName : String
  - artistName : String
  - concertDate : LocalDate
  - venu : String
  - ticketSold : int
  - seatingTiers : List<SeatingTiers>

  **Behavior:**
  The Concert manages its seating tiers and tracks how many tickets have been sold.
  Every ticket issued belongs to the concert (composition — when the concert is deleted, its tickets are also gone).

### LocalDate

  Represents the full date and time of the concert using simple integer fields. This class is used internally by the
  Concert entity to store schedule information without relying on external date/time libraries.

  Fields:
  - day : int
  - month : int
  - year : int
  - hour : int
  - minute : int

  **Behavior:**
  The LocalDate class stores and returns date/time values and provides formatted access to the concert schedule.
  It is part of the Concert through composition, meaning the LocalDate object cannot exist without the Concert that owns it.


 

  
