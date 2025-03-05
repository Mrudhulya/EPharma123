package com.example.pharm;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class GMailSender {
    private final String senderEmail = "epharma09@gmail.com"; // Change this
    private final String senderPassword = "uimy lhrr cohq tazr"; // Use app password
    private Session session;

    public GMailSender() {
        Properties props = new Properties();
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");  // Enable TLS
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
    }

    public void sendMail(String recipientEmail, String subject, String messageBody) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(messageBody);

        Transport.send(message);
    }
}
