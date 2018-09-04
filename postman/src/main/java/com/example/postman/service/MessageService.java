package com.example.postman.service;

import com.example.postman.entity.Message;

public interface MessageService {
    void save(Message message);

    Message findByEmail(String email);
}
