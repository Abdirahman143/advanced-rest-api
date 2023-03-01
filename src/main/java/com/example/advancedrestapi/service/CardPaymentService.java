package com.example.advancedrestapi.service;

import com.example.advancedrestapi.model.CardCategory;
import com.example.advancedrestapi.model.CardPayment;
import com.example.advancedrestapi.request.CardCategoryRequest;
import com.example.advancedrestapi.request.CardPaymentRequest;
import org.springframework.http.ResponseEntity;

public interface CardPaymentService {

    ResponseEntity<CardPayment> Save(CardPaymentRequest cardPaymentRequest);

}
