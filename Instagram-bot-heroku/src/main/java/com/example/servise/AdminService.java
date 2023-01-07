package com.example.servise;

import com.example.entity.CookieEntity;
import com.example.repository.CookieRepository;
import com.example.repository.ProfileRepository;
import com.example.telegramBot.MyTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MyTelegramBot myTelegramBot;

    @Autowired
    private CookieRepository cookieRepository;



    public void userCount(Message message) {

        long count = profileRepository.countByUserId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Botdan hozirda: " + count + " foydalanuvchi mavjud \n\n" +
                "\uD83D\uDCE5 @isnta_video_bot");
        myTelegramBot.send(sendMessage);


    }

    public String changeCookie(String text) {
        Optional<CookieEntity> optional = cookieRepository.findById(1);
       if (optional.isEmpty()){
        CookieEntity entity = new CookieEntity();
        entity.setCookie(text);
        cookieRepository.save(entity);
        return "created";
        }
        CookieEntity entity = optional.get();
        entity.setCookie(text);
        cookieRepository.save(entity);
        return "updated";
    }
}
