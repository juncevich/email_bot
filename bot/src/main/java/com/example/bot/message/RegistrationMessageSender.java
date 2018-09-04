package com.example.bot.message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import enums.RoutingKey;
import lombok.extern.slf4j.Slf4j;
import model.RegistrationMessage;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RegistrationMessageSender {

    private final RabbitTemplate rabbitTemplate;

    private final DirectExchange exchange;
    private Gson gson;

    @Autowired
    public RegistrationMessageSender(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    @PostConstruct
    private void init() {
        gson = new GsonBuilder().create();
    }

    public void send(RegistrationMessage message) {
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(exchange.getName(), RoutingKey.REGISTRATION.getValue(), gson.toJson(message));
        log.info("Message sent.");
    }

}
