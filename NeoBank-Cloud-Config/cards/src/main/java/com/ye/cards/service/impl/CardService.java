package com.ye.cards.service.impl;

import com.ye.cards.constants.CardsConstants;
import com.ye.cards.dto.CardDto;
import com.ye.cards.entity.Cards;
import com.ye.cards.exception.CardAlreadyExistsException;
import com.ye.cards.exception.ResourceNotFoundException;
import com.ye.cards.mapper.CardMapper;
import com.ye.cards.repository.CardRepository;
import com.ye.cards.service.ICardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService implements ICardService {

    private final CardRepository cardRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> card = cardRepository.findByMobileNumber(mobileNumber);
        if ( card.isPresent() ){
            throw new CardAlreadyExistsException("Card already register with given mobile Number : "+ mobileNumber);
        }
        cardRepository.save(createNewCard(mobileNumber));
    }

    private Cards createNewCard(String mobileNumber) {
        Cards card = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        card.setCardNumber(Long.toString(randomCardNumber));
        card.setMobileNumber(mobileNumber);
        card.setCardType(CardsConstants.CREDIT_CARD);
        card.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        card.setAmountUsed(0);
        card.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return card;
    }

    @Override
    public CardDto fetchCard(String mobileNumber) {
        Cards cards = cardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Card" , "Mobile number" , mobileNumber )
                );
        return CardMapper.mapToCardDto(cards , new CardDto());
    }

    @Override
    public boolean updateCard(CardDto cardDto) {
        Cards cards = cardRepository.findByCardNumber(cardDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card" , "Card number" , cardDto.getCardNumber() )
        );
        CardMapper.mapToCards(cards, cardDto);
        cardRepository.save(cards);

        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card" , "Card number" , mobileNumber )
        );
        cardRepository.deleteById(cards.getCardId());
        return true;
    }
}
