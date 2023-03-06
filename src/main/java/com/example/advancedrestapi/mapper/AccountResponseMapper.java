package com.example.advancedrestapi.mapper;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.response.AccountResponse;

public class AccountResponseMapper {

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
