package com.example.advancedrestapi.service;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.mapper.AccountResponseMapper;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
         @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    //saving account related data to database
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


            @Override
            //getting all account data
       public ResponseEntity<List<AccountResponse>> getAllAccounts(){
            List<Account>accountList = accountRepository.findAll();
            return  new ResponseEntity<>( accountList.stream().
                    map(AccountResponseMapper::MapToResponse).toList(),
                    HttpStatus.FOUND);

       }


       @Override
       //getting single account by Id
    public Optional<AccountResponse>getAccountById(Long id) throws UserNotFoundException {
             Optional<Account> account = accountRepository.findById(id);
                 AccountResponse accountResponse = AccountResponse.
                         builder().
                         id(account.get().getId()).
                         firstName(account.get().getFirstName()).
                         middleName(account.get().getMiddleName()).
                         lastName(account.get().getLastName()).
                         email(account.get().getEmail()).
                         mobileNumber(account.get().getMobileNumber()).
                         accountNumber(account.get().getAccountNumber()).
                         dateOfBirth(account.get().getDateOfBirth()).
                         build();

                 if(account!=null){
                     return  Optional.ofNullable(accountResponse);
                 }else{
                     throw new  UserNotFoundException("the Id"+id+"not found please try with valid Id");
                 }

    }

}
