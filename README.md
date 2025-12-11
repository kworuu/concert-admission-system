# WildCats Pub - Concert Admission System
A comprehensive Java-based application designed to streamline the concert ticket booking process. This system handles seat selection, secure ticket generation (PDF with QR codes), automated emailing, and real-time ticket verification using a webcam.

## Project Overview
The Concert Admission System allows users to browse concerts, select specific seats from a visual layout (VVIP, VIP, General Admission), and purchase tickets. Upon confirmation, the system generates a secure PDF ticket embedded with a cryptographic hash and QR code, which is immediately emailed to the customer. A companion Ticket Scanner module uses a webcam to verify these tickets at the venue entrance, preventing fraud and duplicate entries.

## Key Features
- Interactive Seat Selection: Visual grid interface for selecting seats across multiple tiers.
- Automated Ticket Generation: Creates professional PDF tickets with dynamic user data and background styling.
- QR Code Integration: Generates unique QR codes containing encrypted verification data.
- Email Delivery: Automatically sends the ticket PDF to the customer's email via SMTP.
- Real-Time Scanner: Uses computer vision to scan printed or digital QR codes for entry validation.
- Data Persistence: Stores transaction records and seat availability in CSV files.
- Modern GUI: Utilizes FlatLaf for a modern, dark-themed user interface.

## Design Patterns Applied
Per the project requirements, the following Design Patterns were implemented to ensure modularity and scalability:

1. Singleton Pattern
- Applied in: ConcertManager.java
- Explanation: We implemented the Singleton pattern to ensure that the ConcertManager class has only one single instance throughout the application's lifecycle. This ensures a "single source of truth" for the list of available concerts, preventing data conflicts where different parts of the app might see different concert lists.

2. Facade Pattern
- Applied in: TicketManager.java and ConcertAdmissionSystemUI.java
- Explanation:
⋅⋅⋅⋅* TicketManager: Acts as a facade for the complex File I/O subsystem. It hides the messy details of reading, parsing, writing, and updating CSV files behind simple methods like saveTicket() and markTicketAsUsed().
⋅⋅⋅⋅*UI Class: The main UI acts as a facade for the purchase transaction, coordinating the Customer creation, PDFGenerator, and EmailSender subsystems into a single "Confirm" action.

3. Adapter Pattern
- Applied in: TicketScanner.java
- Explanation: The webcam-capture library outputs a standard Java BufferedImage, but the QR scanning library (ZXing) requires a specific BinaryBitmap format. We implemented adapter logic inside the scanning loop to convert and "adapt" the webcam's video frame into a format the QR reader can understand in real-time.

4. Template Method Pattern
- Applied in: BackgroundEvent.java
- Explanation: By extending PdfPageEventHelper from the iText library, we utilized the Template Method pattern. The library provides the skeleton for generating a PDF page but leaves the onEndPage() method empty. We overrode this specific step to inject our custom background images (VVIP/VIP/GenAd) dynamically during the generation process.

## Object-Oriented Principles
This project demonstrates the four pillars of OOP:
- Encapsulation: Used extensively in classes like Ticket and Customer. Data fields (like price, ticketID, name) are private and accessed only through public getters/setters to protect data integrity.
- Inheritance: The BackgroundEvent class inherits from PdfPageEventHelper, gaining its properties while adding specific behavior for our ticket styling.
- Polymorphism: Used in the UI event listeners (e.g., ActionListener) and the TicketScanner threading model (Runnable), allowing objects to be treated as instances of their parent interfaces.
- Abstraction: The TicketScanner relies on abstract interfaces to handle threading and hardware interaction without needing to know the low-level details of the CPU or Camera driver.

## Libraries & Dependencies
- Java Swing: Core GUI framework.
- FlatLaf: For the modern look and feel.
- iText PDF: For generating PDF documents.
- ZXing (Zebra Crossing): For generating and decoding QR codes.
- Webcam Capture: For accessing hardware cameras.
- JavaMail API: For sending emails via SMTP.
- BridJ / SLF4J: Required dependencies for hardware interaction.



👤 Authors
<ins>Charles B. Daragosa</ins> - Lead Frontend Developer

<ins>Alleah I. Dela Peña</ins> - Backend Developer

<ins>Mark Lou A. Villagonzalo</ins> - Backend Developer

<ins>Vonn Vincent S. Caballero Daragosa</ins> - Backend Developer

<ins>Andre B. Camasura</ins> - Backend Developer

Submitted for CSIT227 Object Oriented Programming Capstone

