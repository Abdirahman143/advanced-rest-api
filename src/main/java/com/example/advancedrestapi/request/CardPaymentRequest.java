package com.example.advancedrestapi.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardPaymentRequest {


    private Long Id;
    private Integer cardNumber;
    private String cardName;
    private Date expireYear;
    private Date expireMonth;
    private Integer code;
    private BigDecimal amount;
    private List<CardCategoryRequest> cardCategoryRequests;

}
