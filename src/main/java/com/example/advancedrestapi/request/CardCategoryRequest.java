package com.example.advancedrestapi.request;

import com.example.advancedrestapi.model.CardPayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardCategoryRequest {
    private String cardName;
    private CardPayment paymentCard;
}
