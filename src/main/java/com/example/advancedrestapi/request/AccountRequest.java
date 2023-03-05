package com.example.advancedrestapi.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {


    private String firstName;
    private String middleName;
    private String lastName;
    private String accountNumber;
    private String mobileNumber;
    private Date DateOfBirth;
    private String emailNumber;
}
