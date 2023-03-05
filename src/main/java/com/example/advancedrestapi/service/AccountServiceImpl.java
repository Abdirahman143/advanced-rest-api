package com.example.advancedrestapi.service;

import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.request.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
         @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public ResponseEntity<Account>addAccount(AccountRequest accountRequest){
             Account account = new Account();
             account.setFirstName(accountRequest.getFirstName());
             account.setMiddleName(accountRequest.getMiddleName());
             account.setLastName(accountRequest.getLastName());
             account.setEmail(accountRequest.getEmail());
             account.setMobileNumber(accountRequest.getMobileNumber());
             account.setAccountNumber(accountRequest.getAccountNumber());
             account.setDateOfBirth(accountRequest.getDateOfBirth());

             return  new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);

    }
}
