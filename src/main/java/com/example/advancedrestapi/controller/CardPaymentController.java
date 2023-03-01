package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.model.CardPayment;
import com.example.advancedrestapi.request.CardPaymentRequest;
import com.example.advancedrestapi.service.CardPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/card-payment")
public class CardPaymentController {


    private final CardPaymentService cardPaymentService;

    public CardPaymentController(CardPaymentService cardPaymentService) {
        this.cardPaymentService = cardPaymentService;
    }


    @PostMapping()

    public ResponseEntity<CardPayment>createCardPayment(@RequestBody CardPaymentRequest cardPaymentRequest){
        return cardPaymentService.Save(cardPaymentRequest);
    }
}
