package com.example.bot.dao;

import com.example.bot.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<Message, Long> {
    Message findByEmail(String email);
}
