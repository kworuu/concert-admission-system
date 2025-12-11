package ConcertAdmissionSystem;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailSender {
    // Email configuration - should be in a config file in production
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "wildcatsadmissionsystem@gmail.com";
    private static final String SENDER_PASSWORD = "cirwwhceqgtbmjsv"; // App password for email

    /**
     * Sends ticket email with PDF attachment
     * @param recipientEmail Customer's email
     * @param pdfPath Path to the PDF ticket file
     * @param customerName Customer's name for personalization
     * @param ticketID Ticket ID for reference
     * @return true if sent successfully, false otherwise
     */
    public static boolean sendTicketEmail(String recipientEmail, String pdfPath,
                                          String customerName, String ticketID) {

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // --- FIX FOR "PKIX PATH BUILDING FAILED" ---
        // This tells Java to trust the Gmail server, bypassing the strict certificate check.
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "WildCats Pub"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Concert Ticket - " + ticketID);

            // Email body (HTML)
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = String.format(
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif;'>" +
                            "<h2>Thank You for Your Purchase, %s!</h2>" +
                            "<p>Your ticket has been confirmed. Here are your details:</p>" +
                            "<ul>" +
                            "<li><strong>Ticket ID:</strong> %s</li>" +
                            "<li><strong>Concert:</strong> WildCats Pub Concert</li>" +
                            "</ul>" +
                            "<p>Your ticket is attached to this email as a PDF. Please present it at the entrance.</p>" +
                            "<p><strong>Important:</strong></p>" +
                            "<ul>" +
                            "<li>Arrive at least 30 minutes before the show</li>" +
                            "<li>Bring a valid ID</li>" +
                            "<li>No refunds or exchanges</li>" +
                            "</ul>" +
                            "<p>See you at the concert!</p>" +
                            "<br>" +
                            "<p style='color: gray; font-size: 12px;'>WildCats Pub<br>" +
                            "For support: support@wildcatspub.com</p>" +
                            "</body>" +
                            "</html>",
                    customerName, ticketID
            );
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            // PDF attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            File pdfFile = new File(pdfPath);

            if (!pdfFile.exists()) {
                System.err.println("PDF file not found: " + pdfPath);
                return false;
            }

            attachmentPart.attachFile(pdfFile);
            attachmentPart.setFileName("Ticket_" + ticketID + ".pdf");

            // Combine parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send
            Transport.send(message);

            System.out.println("Email sent successfully to: " + recipientEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
