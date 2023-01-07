package com.example.servise;

import com.example.entity.CookieEntity;
import com.example.repository.CookieRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CookieService {

    private final CookieRepository repository;

    @Lazy
    public CookieService(CookieRepository repository) {
        this.repository = repository;
    }

    public String cookie(){
      return repository.findById(1).get().getCookie();
    }

}
