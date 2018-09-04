package com.example.postman.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.RoutingKey;
import lombok.extern.slf4j.Slf4j;
import model.EmailToChat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class SendMessageSender {

    private final RabbitTemplate rabbitTemplate;

    private Gson gson;

    @Autowired
    public SendMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    private void init() {
        gson = new GsonBuilder().create();
    }

    void send(EmailToChat message) {
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(RoutingKey.SEND_EMAIL.getValue(), gson.toJson(message));
        log.info("Message sent.");
    }
}
