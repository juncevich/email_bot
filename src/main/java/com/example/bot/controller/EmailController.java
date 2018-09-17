package com.example.bot.controller;

import com.example.bot.message.MessageCandidateSender;
import com.example.bot.model.MessageCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Properties;

@Component
public class EmailController {

    private final MessageCandidateSender messageCandidateSender;

    @Autowired
    public EmailController(MessageCandidateSender messageCandidateSender) {
        this.messageCandidateSender = messageCandidateSender;
    }


    @Scheduled(fixedDelay = 5000)
    public void getEmail() throws MessagingException, IOException {

        Properties props = new Properties();
        props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.pop3.socketFactory.fallback", "false");
        props.put("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.host", "pop.mail.ru");
        props.put("mail.pop3.user", "test_teset@bk.ru");
        props.put("mail.store.protocol", "pop3");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("test_teset@bk.ru", "test88005553535");
            }
        };

        Session session = Session.getDefaultInstance(props, auth);

        Store store = session.getStore("pop3");
        store.connect("pop.mail.ru", "test_teset@bk.ru", "test88005553535");
        System.out.println(store);

        Folder inbox = store.getFolder("INBOX");

        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.getMessages();
        for (Message message : messages) {

            MessageCandidate messageCandidate = new MessageCandidate();
            messageCandidate.setEmail(((InternetAddress) message.getFrom()[0]).getAddress());
            String textFromMessage = getTextFromMessage(message);
            String emailMessage =
                    "From: " + messageCandidate.getEmail() + "\n" +
                            "Subject: " + message.getSubject() + "\n" +
                            "Message: " + textFromMessage;
            messageCandidate.setMessage(emailMessage);
            messageCandidateSender.send(messageCandidate);
            message.setFlag(Flags.Flag.DELETED, true);
        }

        // 7. Close folder and close store.
        inbox.close(true);
        store.close();
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }


}

