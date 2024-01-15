package com.example.advancedrestapi.service;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.mapper.AccountResponseMapper;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.request.AccountPartialUpdateRequest;
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
    public ResponseEntity<Account> addAccount(AccountRequest accountRequest) {
        Account account = new Account();
        account.setFirstName(accountRequest.getFirstName());
        account.setMiddleName(accountRequest.getMiddleName());
        account.setLastName(accountRequest.getLastName());
        account.setEmail(accountRequest.getEmail());
        account.setMobileNumber(accountRequest.getMobileNumber());
        account.setAccountNumber(accountRequest.getAccountNumber());
        account.setDateOfBirth(accountRequest.getDateOfBirth());

        return new ResponseEntity<>(accountRepository.save(account), HttpStatus.CREATED);

    }


    @Override
    //getting all account data
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accountList = accountRepository.findAll();
        return new ResponseEntity<>(accountList.stream().
                map(AccountResponseMapper::MapToResponse).toList(),
                HttpStatus.FOUND);

    }


    //find employee details by email and account number
    @Override
    public Optional<AccountResponse> findAccountDetails(String accountNumber, String email) {
        Optional<Account> accountOptional = accountRepository.findAccountDetails(accountNumber, email);
        if (!accountOptional.isPresent()) {
            throw new UserNotFoundException("account number" + accountNumber + "and email" + email + "not found. Please try with a valid account number and email.");
        }
        Account account = accountOptional.get();
        AccountResponse accountResponse = AccountResponse
                .builder().
                id(account.getId()).
                firstName(account.getFirstName()).
                middleName(account.getMiddleName()).
                email(account.getEmail()).
                accountNumber(account.getAccountNumber()).
                mobileNumber(account.getMobileNumber()).
                build();
        return Optional.of(accountResponse);
    }

    //updating the account details
    @Override
    public ResponseEntity<AccountResponse> UpdateAccountDetails(AccountRequest accountRequest,
                                                                String accountNumber,
                                                                String email) {
        Optional<Account> accountOptional = accountRepository.findAccountDetails(accountNumber, email);
        if (!accountOptional.isPresent()) {
            throw new UserNotFoundException("account number" + accountNumber + "and email" + email + "not found. Please try with a valid account number and email.");
        }

        Account existingAccount = accountOptional.get();
        updateAccountDetails(existingAccount, accountRequest);
        Account updatedAccount = accountRepository.save(existingAccount);
        AccountResponse accountResponse = AccountResponseMapper.MapToResponse(updatedAccount);

        return new ResponseEntity<>(accountResponse, HttpStatus.OK);


    }

    //this method updates the account details
    public void updateAccountDetails(Account existingAccount, AccountRequest accountRequest) {
        existingAccount.setFirstName(accountRequest.getFirstName());
        existingAccount.setMiddleName(accountRequest.getMiddleName());
        existingAccount.setLastName(accountRequest.getLastName());
        existingAccount.setMobileNumber(accountRequest.getMobileNumber());
        existingAccount.setDateOfBirth(accountRequest.getDateOfBirth());
    }


//partial update the account details

    @Override
    public ResponseEntity<AccountResponse> updateAccountDetailsPartially(AccountPartialUpdateRequest partialUpdateRequest,
                                                                         String accountNumber,
                                                                         String email) {
        Optional<Account> accountOptional = accountRepository.findAccountDetails(accountNumber, email);
        if (!accountOptional.isPresent()) {
            throw new UserNotFoundException("account number" + accountNumber + "and email" + email + "not found. Please try with a valid account number and email.");

        }
        Account existingAccount = accountOptional.get();
        if (partialUpdateRequest.getMobileNumber() != null) {
            existingAccount.setMobileNumber(partialUpdateRequest.getMobileNumber());
        }
        if (partialUpdateRequest.getDateOfBirth() != null) {
            existingAccount.setDateOfBirth(partialUpdateRequest.getDateOfBirth());
        }
        Account updatedAccount = accountRepository.save(existingAccount);
        AccountResponse accountResponse = AccountResponseMapper.MapToResponse(updatedAccount);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    //Delete account by account Number;
    @Override
    public ResponseEntity<String> deleteAccountDetails(String accountNumber, String email) {
        Optional<Account> accountOptional = accountRepository.findAccountDetails(accountNumber, email);
        if (!accountOptional.isPresent()) {
            throw new UserNotFoundException("Account with account number " + accountNumber + " and email " + email + " not found. Please try with a valid account number and email.");
        }
        accountRepository.deleteByAccountNumber(accountNumber);
        return ResponseEntity.ok("Account with account number " + accountNumber+" has been permanently deleted.");

    }
}