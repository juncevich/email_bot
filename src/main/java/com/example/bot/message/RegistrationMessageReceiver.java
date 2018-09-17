package com.example.bot.message;

import com.example.bot.entity.Message;
import com.example.bot.model.RegistrationMessage;
import com.example.bot.service.MessageService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RegistrationMessageReceiver {

    private final MessageService messageService;

    private Gson gson;

    @Autowired
    public RegistrationMessageReceiver(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostConstruct
    private void init() {
        gson = new GsonBuilder().create();
    }

    @RabbitListener(queues = "registration_queue")
    public void receive(String stringMessage) {
        log.info("*** Received com.example.bot.message: {}", stringMessage);
        RegistrationMessage registrationMessage = gson.fromJson(stringMessage, RegistrationMessage.class);
        if (registrationMessage.getUserId() != null) {
            Message message = new Message();
            message.setUserId(registrationMessage.getUserId());
            message.setEmail(registrationMessage.getEmail());

            messageService.save(message);
        }

    }

}
