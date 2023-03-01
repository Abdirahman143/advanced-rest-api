package com.example.advancedrestapi.service;

import com.example.advancedrestapi.model.CardCategory;
import com.example.advancedrestapi.model.CardPayment;
import com.example.advancedrestapi.repository.CardPaymentRepository;
import com.example.advancedrestapi.request.CardCategoryRequest;
import com.example.advancedrestapi.request.CardPaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CardPaymentServiceImpl implements CardPaymentService {

    private final CardPaymentRepository cardPaymentRepository;

    @Autowired
    public CardPaymentServiceImpl(CardPaymentRepository cardPaymentRepository) {
        this.cardPaymentRepository = cardPaymentRepository;
    }




    @Override
    public ResponseEntity<CardPayment>Save(CardPaymentRequest cardPaymentRequest){
       CardPayment cardPayment = new CardPayment();
       cardPayment.setCardName(cardPaymentRequest.getCardName());
       cardPayment.setCardNumber(cardPaymentRequest.getCardNumber());
       cardPayment.setCode(cardPaymentRequest.getCode());
       cardPayment.setExpireYear(cardPaymentRequest.getExpireYear());
       cardPayment.setExpireMonth(cardPaymentRequest.getExpireMonth());

       cardPayment.setCardCategory(cardPaymentRequest.getCardCategoryRequests().
               stream().map(this::MapToCardRequest).toList()
       );
       return  new ResponseEntity<>(cardPaymentRepository.save(cardPayment), HttpStatus.CREATED);
    }



    public CardCategory MapToCardRequest(CardCategoryRequest cardCategoryRequest){
       return CardCategory.
               builder().
               cardName(cardCategoryRequest.getCardName()).
               build();
    }
}
