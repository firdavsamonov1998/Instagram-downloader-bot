package com.example.servise;

import com.example.enums.UserStatus;
import com.example.entity.ProfileEntity;
import com.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServise {


    @Autowired
    private ProfileRepository repository;

    public boolean isExists(Long id){
     return repository.existsByUserId(id);
    }

    public void addUser(ProfileEntity profile) {
        repository.save(profile);
    }

    public List<Long> listUser(){
        List<ProfileEntity> profileEntities =repository.findAll();
        System.out.println(profileEntities);
        List<Long> userList = new ArrayList<>();
        for (ProfileEntity profileEntity : profileEntities) {
            userList.add(profileEntity.getUserId());
        }
        return userList;
    }

    public void changeStatus(Long userId) {
       Optional<ProfileEntity> optional =repository.findByUserId(userId);
        ProfileEntity entity = optional.get();
        entity.setStatus("BLOCK");

        repository.save(entity);
    }
}

