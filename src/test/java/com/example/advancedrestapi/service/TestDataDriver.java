package com.example.advancedrestapi.service;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestDataDriver {

    public static Account createTestAccount() {
        LocalDate dob = LocalDate.of(1990, 1, 1); // Example date of birth
        Date dateOfBirth = Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Account account = new Account();
        account.setFirstName("John");
        account.setMiddleName("Doe");
        account.setLastName("Smith");
        account.setEmail("john.smith@example.com");
        account.setMobileNumber("1234567890");
        account.setAccountNumber("123456789");
        account.setDateOfBirth(dateOfBirth);

        return account;
    }

    public static AccountRequest createTestAccountRequest() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        Date dateOfBirth = Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setFirstName("John");
        accountRequest.setMiddleName("Doe");
        accountRequest.setLastName("Smith");
        accountRequest.setEmail("john.smith@example.com");
        accountRequest.setMobileNumber("1234567890");
        accountRequest.setAccountNumber("123456789");
        accountRequest.setDateOfBirth(dateOfBirth);

        return accountRequest;
    }

    public static AccountResponse createTestAccountResponse() {
        LocalDate dob = LocalDate.of(1990, 1, 1); // Example date of birth
        Date dateOfBirth = Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AccountResponse accountResponse = AccountResponse.builder()
                .firstName("John")
                .middleName("Doe")
                .lastName("Smith")
                .email("john.smith@example.com")
                .mobileNumber("1234567890")
                .accountNumber("123456789")
                .dateOfBirth(dateOfBirth)
                .build();

        return accountResponse;
    }

    public static List<Account> createTestAccounts() {
        LocalDate dob1 = LocalDate.of(1990, 1, 1); // Example date of birth
        LocalDate dob2 = LocalDate.of(1991, 2, 2);
        LocalDate dob3 = LocalDate.of(1992, 3, 3);

        Account account1 = new Account();
        account1.setFirstName("John");
        account1.setMiddleName("Doe");
        account1.setLastName("Smith");
        account1.setEmail("john.smith@example.com");
        account1.setMobileNumber("1234567890");
        account1.setAccountNumber("123456789");
        account1.setDateOfBirth(Date.from(dob1.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Account account2 = new Account();
        account2.setFirstName("Alice");
        account2.setMiddleName("Jane");
        account2.setLastName("Doe");
        account2.setEmail("alice.doe@example.com");
        account2.setMobileNumber("9876543210");
        account2.setAccountNumber("987654321");
        account2.setDateOfBirth(Date.from(dob2.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Account account3 = new Account();
        account3.setFirstName("Bob");
        account3.setMiddleName("John");
        account3.setLastName("Smith");
        account3.setEmail("bob.smith@example.com");
        account3.setMobileNumber("4561237890");
        account3.setAccountNumber("456123789");
        account3.setDateOfBirth(Date.from(dob3.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return List.of(account1, account2, account3);
    }

    public static List<AccountResponse> createTestAccountResponses() {
        return createTestAccounts().stream()
                .map(account -> AccountResponse.builder()
                        .firstName(account.getFirstName())
                        .middleName(account.getMiddleName())
                        .lastName(account.getLastName())
                        .email(account.getEmail())
                        .mobileNumber(account.getMobileNumber())
                        .accountNumber(account.getAccountNumber())
                        .dateOfBirth(account.getDateOfBirth())
                        .build())
                .collect(Collectors.toList());
    }
}
