package com.ds.Helpers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class MailHelper {

    private static final String GONDEREN_EMAIL = "sender@mail.com";
    private static final String UYGULAMA_SIFRESI = "google_secret_key";

    public static String generateVerificationCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static boolean sendMail(String aliciEmail, String konu, String icerik) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(GONDEREN_EMAIL, UYGULAMA_SIFRESI);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(GONDEREN_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aliciEmail));
            message.setSubject(konu);
            message.setText(icerik);

            Transport.send(message);
            System.out.println("Mail başarıyla gönderildi: " + aliciEmail);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}