package com.example.controller;

import com.example.Interface.Constant;
import com.example.Interface.TelegramUsers;
import com.example.enums.Step;
import com.example.servise.AdminService;
import com.example.servise.UserServise;
import com.example.telegramBot.MyTelegramBot;
import com.example.util.Button;
import com.example.util.SendMsg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final MainController mainController;
    private final MyTelegramBot myTelegramBot;
    private final AdminService adminService;

    private final UserServise userServise;

    private List<TelegramUsers> usersList = new ArrayList<>();

    @Lazy
    public AdminController(MainController mainController, MyTelegramBot myTelegramBot, AdminService adminService, UserServise userServise) {
        this.mainController = mainController;
        this.myTelegramBot = myTelegramBot;
        this.adminService = adminService;
        this.userServise = userServise;
    }

    public void start(Update update) {


        Message message = update.getMessage();
        if (message.hasText()) {
            TelegramUsers users = saveUser(message.getChatId());
            String text = message.getText();
            if (text.equals("/start")) {
                users.setStep(Step.START);
                menu(message);
                return;
            } else if (text.equals("/help")) {
                mainController.helpCommand(message);
                return;
            } else if (text.equals("/contact")) {
                mainController.contactCommand(message);
                return;
            }


            switch (text) {
                case Constant.advertising -> {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "Rasm yoki video yuboring",
                            Button.markup(Button.rowList(
                                    Button.row(Button.button(Constant.back))
                            ))));
                    users.setStep(Step.ADVERTISING);
                    return;
                }
                case Constant.countUser -> {
                    adminService.userCount(message);
                    return;
                }
                case Constant.change -> {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "Yangi cookieni yuboring"));
                    users.setStep(Step.COOKIE);
                    return;
                }
                case Constant.back -> {
                    menu(message);
                    users.setStep(Step.START);
                    return;
                }


            }
            if (users.getStep() != null) {
                switch (users.getStep()) {
                    case COOKIE -> {
                        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                                adminService.changeCookie(text)
                        ));
                        users.setStep(Step.START);
                        return;
                    }
                    case ADVERTISING -> {
                        List<Long> longList = userServise.listUser();

                        for (Long aLong : longList) {
                            if (aLong != null) {
                                try {
                                    myTelegramBot.send(SendMsg.sendMsg(aLong, text));

                                } catch (RuntimeException e) {
                                    userServise.changeStatus(aLong);
                                }
                            }
                        }
                    }
                }


                if (text.startsWith("https://www.instagram.com/p")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else if (text.startsWith("https://www.instagram.com/reel")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else if (text.startsWith("https://www.instagram.com/tv")) {
                    mainController.getUrlAndSendVideoReel(message, text);
                } else {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText("Sorry, Something wrong, or an invalid link. Please try again or check your url");
                    myTelegramBot.send(sendMessage);
                }
            }
            return;
        }


        if (message.hasVideo()) {
            boolean result = sendVideoCaption(message);
            if (result) {

                myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                        "successfully"));
            }


        } else if (message.hasPhoto()) {

            sendPhotoCaption(message);
            myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                    "successfully"));

        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("This bot helps to get video and photo only from Instagram.\n" +
                    "\n" +
                    "Этот бот позволяет скачивать видео и фото только из Instagram.");
            myTelegramBot.send(sendMessage);
        }

    }


    private void sendPhotoCaption(Message message) {

        List<Long> usersId = userServise.listUser();

        PhotoSize photoSize = message.getPhoto().get(0);
        String photoId = photoSize.getFileId();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(photoId);

        SendPhoto sendPhoto = new SendPhoto();
        for (Long aLong : usersId) {
            if (aLong != null) {
                try {
                    sendPhoto.setPhoto(inputFile);
                    sendPhoto.setChatId(aLong);
                    sendPhoto.setCaption(message.getCaption());
                    myTelegramBot.send(sendPhoto);
                } catch (RuntimeException e) {
                    userServise.changeStatus(aLong);
                }

            }
        }

    }

    public boolean sendVideoCaption(Message message) {

        List<Long> usersId = userServise.listUser();

        String videoId = message.getVideo().getFileId();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(videoId);

        SendVideo sendVideo = new SendVideo();
        for (Long aLong : usersId) {
            if (aLong != null) {
                try {
                    sendVideo.setVideo(inputFile);
                    sendVideo.setChatId(aLong);
                    sendVideo.setCaption(message.getCaption());
                    myTelegramBot.send(sendVideo);
                } catch (RuntimeException e) {
                    userServise.changeStatus(aLong);
                }

            }

        }
        return true;
    }


    public void menu(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Welcome Amin aka ");
        sendMessage.setReplyMarkup(Button.markup(Button.rowList(Button.row(
                        Button.button("Advertising Post 📮"), Button.button("User Count 🔄")
                ),
                Button.row(Button.button("Change Cookie")))));

        myTelegramBot.send(sendMessage);
    }


    public TelegramUsers saveUser(Long chatId) {

        for (TelegramUsers users : usersList) {
            if (users.getChatId().equals(chatId)) {
                return users;
            }
        }
//        userController.getStep(chatId);


        TelegramUsers users = new TelegramUsers();
        users.setChatId(chatId);
        usersList.add(users);

        return users;
    }
}


