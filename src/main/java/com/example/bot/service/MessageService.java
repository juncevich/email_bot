package com.example.bot.service;

import com.example.bot.entity.Message;

public interface MessageService {
    void save(Message message);

    Message findByEmail(String email);
}
