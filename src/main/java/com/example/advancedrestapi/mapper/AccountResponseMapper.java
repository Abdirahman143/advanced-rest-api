package com.example.advancedrestapi.mapper;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AccountResponseMapper{
    @Autowired
    private AccountRepository accountRepository;

   public static AccountResponse MapToResponse(Account account) {
        return  AccountResponse.
                builder().
                id(account.getId()).
                firstName(account.getFirstName()).
                middleName(account.getMiddleName()).
                email(account.getEmail()).
                mobileNumber(account.getMobileNumber()).
                accountNumber(account.getAccountNumber()).
                dateOfBirth(account.getDateOfBirth()).
                build();
    }



}
