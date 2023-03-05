package com.example.advancedrestapi.service;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<Account> addAccount(AccountRequest accountRequest);
}
