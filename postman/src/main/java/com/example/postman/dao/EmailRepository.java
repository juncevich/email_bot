package com.example.postman.dao;

import com.example.postman.entity.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<Message, Long> {
    Message findByEmail(String email);
}
