package com.example.bot.response;

import com.example.bot.resolver.CommandResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ResponseGenerator {

    private final CommandResolver resolver;

    @Autowired
    public ResponseGenerator(CommandResolver resolver) {
        this.resolver = resolver;
    }

    public SendMessage generateResponseMessage(Message message) {
        SendMessage response = new SendMessage();
        Long chatId = message.getChatId();
        response.setChatId(chatId);
        String commandResponseText = resolver.resolve(message);
        response.setText(commandResponseText);
        return response;
    }
}
