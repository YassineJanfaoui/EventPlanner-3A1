/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.esprit.api;

import tn.esprit.entities.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailerBan {
    public static void sendEmail(User u) {
        final String username = "rebhiayoub03@gmail.com";
        final String password = "gsnu bslh gcjw baol";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Add this line

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            String signature = "\n\n-- \nHackForge Application \nNuméro de téléphone : +216 11 111 111 \nAdresse e-mail : hackforge3a1@gmail.com \nSite web : www.HACKFORGE.com";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "HACKFORGE Application"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getEmail()));
            message.setSubject("Your account is BANNED ");
            message.setText("Mr/Ms " + u.getName() + " ,"
                    + "\n\nUnfortunately your account is banned !! ."
                    + "\n\nCordialement,\n\n" + signature);

            Transport.send(message);
            System.out.println("Mail sent successfully");

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}