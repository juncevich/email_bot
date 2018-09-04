package com.example.bot.resolver;

import com.example.bot.message.RegistrationMessageSender;
import com.example.bot.validation.EmailValidator;
import model.RegistrationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandResolver {

    private final EmailValidator emailValidator;

    private final RegistrationMessageSender messageSender;

    @Autowired
    public CommandResolver(EmailValidator emailValidator, RegistrationMessageSender messageSender) {
        this.emailValidator = emailValidator;
        this.messageSender = messageSender;
    }

    public String resolve(Message message) {
        String text;
        if ("/".equals(message.getText())) {
            text = "/start\n" +
                    "/register";
        } else if (message.getText().startsWith("/start")) {
            text = "Введи /register + email для регистрации";
        } else if (message.getText().startsWith("/register")) {
            String[] commandArguments = message.getText().split(" ");
            if (commandArguments.length > 1) {
                String email = commandArguments[1];
                if (emailValidator.validate(email)) {
                    text = "Зарегистрированный адрес: " + email;
                    RegistrationMessage registrationMessage = new RegistrationMessage();
                    registrationMessage.setUserId(message.getChatId());
                    registrationMessage.setEmail(email);
                    messageSender.send(registrationMessage);
                } else {
                    text = "Невалидный адрес";
                }
            } else {
                text = "Ты чего, забыл ввести email? o_O";
            }
        } else {
            text = "\"" + message.getText() + "\"? - Чёт не понимаю о чём ты (";
        }
        return text;
    }
}
