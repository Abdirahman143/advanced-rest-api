package com.example.advancedrestapi.request;

import com.example.advancedrestapi.model.CardCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PaymentCardRequest {


    private Long Id;
    private Integer cardNumber;
    private String cardName;
    private Date expireYear;
    private Date expireMonth;
    private Integer code;
    private BigDecimal amount;
    private List<CardCategoryRequest> cardCategoryRequests;

}
