package com.example.mail_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailServerApplication.class, args);
    }
}
