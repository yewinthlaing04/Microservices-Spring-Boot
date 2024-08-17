package com.ye.cards.service;

import com.ye.cards.dto.CardDto;



public interface ICardService {

    void createCard(String mobileNumber);

    CardDto fetchCard(String mobileNumber);

    boolean updateCard(CardDto cardDto);

    boolean deleteCard(String mobileNumber);
}
