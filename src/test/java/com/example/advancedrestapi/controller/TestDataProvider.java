package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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

public static List<AccountResponse> createTestAccountResponses() {
      //arrange
      LocalDate localDateOfBirth = LocalDate.now().plusYears(23);
      // Convert LocalDate to Date
      Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
             return List.of(
                     AccountResponse.
                     builder().
                             firstName("Noor").
                             middleName("Dalaac").
                             lastName("Kahin").
                             accountNumber("345678900").
                             mobileNumber("070091653").
                             email("kahin.abdi@test.com").
                             dateOfBirth(dateOfBirth).
                             build(),
                     AccountResponse.
                             builder().
                             firstName("shukri").
                             middleName("shafi").
                             lastName("Sahal").
                             accountNumber("31245678").
                             mobileNumber("070090053").
                             email("sahal.shakir@test.com").
                             dateOfBirth(dateOfBirth).
                             build(),
                     AccountResponse.
                             builder().
                             firstName("Abdi").
                             middleName("Abdirahman").
                             lastName("Abdi").
                             accountNumber("345678").
                             mobileNumber("070091653").
                             email("bashir.abdi@test.com").
                             dateOfBirth(dateOfBirth).
                             build());
    }
}
