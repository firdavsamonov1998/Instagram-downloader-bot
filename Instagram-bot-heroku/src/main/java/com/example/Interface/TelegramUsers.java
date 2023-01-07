package com.example.Interface;

import com.example.enums.Step;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class TelegramUsers {
    private Long chatId;
    private Step step;

}
