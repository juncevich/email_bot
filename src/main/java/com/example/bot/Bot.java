package com.example.bot;

import com.example.bot.response.ResponseGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {


    private final ResponseGenerator responseGenerator;

    @Autowired
    public Bot(ResponseGenerator responseGenerator) {
        this.responseGenerator = responseGenerator;
    }

    @Override
    public String getBotToken() {
        return "639901087:AAHYvUKYqQWEHu69nxYCn_e4quEwBjdagkc";
    }

    @Override
    public void onUpdateReceived(Update update) {


        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage response = responseGenerator.generateResponseMessage(message);
            try {
                if (!StringUtils.isEmpty(response.getText())) {
                    execute(response);
                    log.info("Sent com.example.bot.message \"{}\" to {}", response.getText(), response.getChatId());
                }
            } catch (TelegramApiException e) {
                log.error("Failed to send com.example.bot.message \"{}\" to {} due to error: {}", response.getText(), response.getChatId(), e.getMessage());
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "my_test_email_bot";
    }
}
