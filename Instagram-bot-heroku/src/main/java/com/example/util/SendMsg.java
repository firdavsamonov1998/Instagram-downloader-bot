package com.example.util;

import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public class SendMsg {
    public static SendMessage sendMsg(Long id, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        return sendMessage;
    }

    public static SendMessage sendMsg(Long id, String text, ReplyKeyboardMarkup markup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }

    public static SendPhoto sendPhoto(Long id, String text, String inputFile) {
        SendPhoto sendPhoto = new SendPhoto();
        InputFile input = new InputFile();
        input.setMedia(inputFile);
        sendPhoto.setChatId(id);
        sendPhoto.setPhoto(input);
        sendPhoto.setCaption(text);

        return sendPhoto;
    }



    public static SendVideo sendVideo(Long id, String urlVideo,String url) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(id);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(urlVideo);
        sendVideo.setVideo(inputFile);
        sendVideo.setCaption(url + "\n\n" +
                "\uD83D\uDCE5 @isnta_video_bot");
        return sendVideo;
    }


}
