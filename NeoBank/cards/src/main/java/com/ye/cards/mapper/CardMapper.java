package com.ye.cards.mapper;

import com.ye.cards.dto.CardDto;
import com.ye.cards.entity.Cards;

public class CardMapper {

    public static CardDto mapToCardDto(Cards card , CardDto cardDto){

        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setCardType(card.getCardType());
        cardDto.setMobileNumber(card.getMobileNumber());
        cardDto.setTotalLimit(card.getTotalLimit());
        cardDto.setAvailableAmount(card.getAvailableAmount());
        cardDto.setAmountUsed(card.getAmountUsed());

        return cardDto;
    }

    public static Cards mapToCards(Cards card , CardDto cardDto){

        card.setCardNumber(cardDto.getCardNumber());
        card.setCardType(cardDto.getCardType());
        card.setMobileNumber(cardDto.getMobileNumber());
        card.setTotalLimit(cardDto.getTotalLimit());
        card.setAvailableAmount(cardDto.getAvailableAmount());
        card.setAmountUsed(cardDto.getAmountUsed());

        return card;
    }
}
