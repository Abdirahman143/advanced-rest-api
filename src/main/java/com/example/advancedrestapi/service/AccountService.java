package com.example.advancedrestapi.service;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountPartialUpdateRequest;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    ResponseEntity<Account> addAccount(AccountRequest accountRequest);

    //getting all account data
    ResponseEntity<List<AccountResponse>> getAllAccounts();


    Optional<AccountResponse>findAccountDetails(String accountNumber, String email);

    //updating the account details
    ResponseEntity<AccountResponse>UpdateAccountDetails(AccountRequest accountRequest,
                                                        String accountNumber,
                                                        String email);

    ResponseEntity<AccountResponse>updateAccountDetailsPartially(AccountPartialUpdateRequest partialUpdateRequest,
                                                                 String accountNumber,
                                                                 String email);

    //Delete account by account Number;
    String DeleteAccountDetails(String accountNumber, String email);
}
