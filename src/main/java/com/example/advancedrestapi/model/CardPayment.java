package com.example.advancedrestapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments",
        uniqueConstraints = {
                 @UniqueConstraint(name = "cardNumber_unique", columnNames = "cardNumber"),
                 @UniqueConstraint(name = "code_unique",columnNames = "code")

        }

)
@Entity
public class CardPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotBlank
    @Max(value = 15, message = "maximum number of digit is 15")
    @CreditCardNumber
    @Min(value = 12, message = "card should be at least 12 digits")
    private Integer cardNumber;
    @NotNull(message = "Name can not be null!!")
    private String cardName;
     @OneToMany(
             cascade =CascadeType.ALL,
             fetch =  FetchType.LAZY,
             orphanRemoval = true,
             mappedBy = "cardCategory"
     )

    private List<CardCategory> cardCategory;
    @Future(message = "expire year should be future")
    private Date expireYear;
    @Future(message = "expire month should be future")
    private Date expireMonth;
    @NotBlank(message = "code is required!! Can not be blank")
    private Integer code;
    @NotEmpty(message = "amount can not be empty")
    @Range(min = 1, max = 10000,
            message = "minimum amount should be 1 dollar" +
            "and maximum amount is 10,000")
    private BigDecimal amount;
}
