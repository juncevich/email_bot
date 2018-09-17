package com.example.bot.message;

import com.example.bot.entity.Message;
import com.example.bot.model.EmailToChat;
import com.example.bot.model.MessageCandidate;
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
public class MessageCandidateReciever {

    private final MessageService messageService;
    private final SendMessageSender sendMessageSender;
    private Gson gson;

    @Autowired
    public MessageCandidateReciever(MessageService messageService, SendMessageSender sendMessageSender) {
        this.messageService = messageService;
        this.sendMessageSender = sendMessageSender;
    }


    @PostConstruct
    private void init() {
        gson = new GsonBuilder().create();
    }

    @RabbitListener(queues = "message_candidate_queue")
    public void receive(String stringMessage) {
        log.info("Received com.example.bot.message: {}", stringMessage);
        MessageCandidate messageCandidate = gson.fromJson(stringMessage, MessageCandidate.class);
        Message userIdByEmail = messageService.findByEmail(messageCandidate.getEmail());
        if (userIdByEmail != null) {
            EmailToChat emailToChat = new EmailToChat();
            emailToChat.setUserId(String.valueOf(userIdByEmail.getUserId()));
            emailToChat.setEmail(userIdByEmail.getEmail());
            emailToChat.setMessage(messageCandidate.getMessage());
            sendMessageSender.send(emailToChat);
        }

    }

}
