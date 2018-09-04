package com.example.postman.service.impl;

import com.example.postman.dao.EmailRepository;
import com.example.postman.entity.Message;
import com.example.postman.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    private final EmailRepository repository;

    @Autowired
    public MessageServiceImpl(EmailRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Message message) {
        Message savedMessage = repository.save(message);
        log.debug("Saved message: {}", savedMessage);
    }

    public Message findByEmail(String email) {
        Message message = repository.findByEmail(email);
        log.debug("Founded message: {}", message);
        return message;
    }
}
