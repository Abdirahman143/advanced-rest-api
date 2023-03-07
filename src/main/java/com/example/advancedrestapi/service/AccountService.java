package com.example.advancedrestapi.service;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    ResponseEntity<Account> addAccount(AccountRequest accountRequest);

    //getting all account data
    ResponseEntity<List<AccountResponse>> getAllAccounts();

    //getting single account by Id
    Optional<AccountResponse> getAccountById(Long id) throws UserNotFoundException;

    //find account by Account Number
    Optional<AccountResponse>findByAccountNumber(String accountNumber) throws UserNotFoundException;
}
