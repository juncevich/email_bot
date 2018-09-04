package com.example.bot.message;

import com.example.bot.Bot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import model.EmailToChat;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class EmailToChatReceiver {

    private final Bot bot;

    private Gson gson;

    @Autowired
    public EmailToChatReceiver(Bot bot) {
        this.bot = bot;
    }


    @PostConstruct
    private void init() {
        gson = new GsonBuilder().create();
    }

    @RabbitListener(queues = "send_email")
    public void receive(String stringMessage) {
        log.info("Received message: {}", stringMessage);
        EmailToChat emailToChat = gson.fromJson(stringMessage, EmailToChat.class);

        SendMessage message = new SendMessage();
        message.setChatId(emailToChat.getUserId());
        message.setText(emailToChat.getMessage());

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Случилось непоправимое: ", e);
        }
    }

}
