package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TestDataProvider {

    public static Account createAccount(String firstName, String lastName, String accountNumber, String email, String mobileNumber, LocalDate dob) {
        return Account.builder()
                .firstName(firstName)
                .lastName(lastName)
                .accountNumber(accountNumber)
                .email(email)
                .mobileNumber(mobileNumber)
                .dateOfBirth(Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
    }

    public static AccountRequest createAccountRequest(String firstName, String lastName, String accountNumber, String email, String mobileNumber, LocalDate dob) {
        return AccountRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .accountNumber(accountNumber)
                .email(email)
                .mobileNumber(mobileNumber)
                .dateOfBirth(Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}
