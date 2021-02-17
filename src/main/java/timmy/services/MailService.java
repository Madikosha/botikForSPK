package timmy.services;

import lombok.extern.slf4j.Slf4j;
import timmy.dao.DaoFactory;

//import javax.jms.Session;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

@Slf4j
public class MailService {

    public static void sendMail(String recipient, String text) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myAccountEmail = DaoFactory.getInstance().getPropertiesDao().getPropertiesValue(7);
        String password       = DaoFactory.getInstance().getPropertiesDao().getPropertiesValue(8);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        Optional<Message> mbMessage = prepareMessage(session, myAccountEmail, recipient, text);
        mbMessage.flatMap(message -> {
            try {
                Transport.send(message);
                log.info("Message sent successfully, message text: " + text);
                return Optional.of(message);
            } catch (MessagingException e) {
                log.error("Cant send message: emailFrom - " + myAccountEmail + " password: " + password + " recipient " + recipient + "text " + text, e);
                return Optional.empty();
            }
        });
    }

    private static Optional<Message> prepareMessage(Session session, String myAccountEmail, String recipient, String text) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Поступило обращение Telegram бот");
            message.setText(text);
            return Optional.of(message);
        } catch (MessagingException e) {
            log.error("Cant send message: emailFrom - " + myAccountEmail + " recipient " + recipient + "text " + text, e);
            return Optional.empty();
        }
    }
}
