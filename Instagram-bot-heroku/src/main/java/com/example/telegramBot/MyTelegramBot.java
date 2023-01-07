package com.example.telegramBot;

import com.example.config.BotConfig;
import com.example.controller.AdminController;
import com.example.controller.MainController;
import com.example.enums.UserStatus;
import com.example.entity.ProfileEntity;
import com.example.servise.UserServise;
import com.example.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class MyTelegramBot extends TelegramLongPollingBot {
    private final MainController mainController;
    private final UserServise userServise;
    private final AdminController adminController;



    private final BotConfig botConfig;
@Lazy
    public MyTelegramBot(MainController mainController, UserServise userServise, AdminController adminController, BotConfig botConfig) {
        this.mainController = mainController;
        this.userServise = userServise;
        this.adminController = adminController;
        this.botConfig = botConfig;
    }


    @Override
    public void onUpdateReceived(Update update) {



        if (update.hasMessage()) {

            Message message = update.getMessage();
            if (message.getChatId() == 1024661500) {
                adminController.start(update);
                return;
            }
            if (!userServise.isExists(message.getFrom().getId())) {
                ProfileEntity profile = new ProfileEntity();
                profile.setName(message.getFrom().getFirstName());
                profile.setUserName(message.getFrom().getUserName());
                profile.setUserId(message.getFrom().getId());
                profile.setStatus("ACTIVE");
                userServise.addUser(profile);
            }


            if (message.hasText()) {
                String text = message.getText();

//                SendMessage sendmessage = new SendMessage();
//                sendmessage.setChatId(message.getChatId());
//                sendmessage.setText("Bot vaqtinchalik ish faoliyatida emas hammadan uzur surab qolamiz ! \n\n" +
//                        "The bot is not in temporary work, we apologize to everyone ! \n\n" +
//                        "\uD83D\uDCE5 @isnta_video_bot");
//
//                send(sendmessage);
                if (text.equals("/start")) {
                    mainController.handle(message);
                } else if (text.equals("/help")) {
                    mainController.helpCommand(message);
                } else if (text.equals("/contact")) {
                    mainController.contactCommand(message);
                } else if (text.startsWith("https://www.instagram.com/p")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else if (text.startsWith("https://www.instagram.com/reel")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else if (text.startsWith("https://www.instagram.com/tv")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else if (text.startsWith("https://instagram.com/s")) {
                    send(SendMsg.sendMsg(message.getChatId(),
                            "I can't download Instagram stories 😢😢"));
                } else {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Sorry, Something wrong, or an invalid link. Please try again or check your url");
                    send(sendMessage);
                }

            } else {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText("This bot helps to get video and photo only from Instagram.\n" +
                        "\n\n" +
                        "Этот бот позволяет скачивать видео и фото только из Instagram.");
                send(sendMessage);
            }
        }
    }



    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
