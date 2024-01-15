package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountPartialUpdateRequest;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import com.example.advancedrestapi.service.AccountService;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
//@BasePathAwareController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    //adding account details to database

                   @PostMapping()
    public ResponseEntity<Account>Save(@RequestBody @Valid AccountRequest accountRequest){
        return accountService.addAccount(accountRequest);
    }


    @GetMapping()
    public ResponseEntity<List<AccountResponse>>getAccounts(){
        return accountService.getAllAccounts();
    }


    //get Accounts details by account number and email

    @GetMapping("/details")
    public Optional<AccountResponse>getAccountDetails(@RequestParam(required = true)String accountNumber,
                                                      @RequestParam(required = true)String email){
        return accountService.findAccountDetails(accountNumber,email);
    }

    //update account details

    @PutMapping()
    public ResponseEntity<AccountResponse>updateAccountDetails(@RequestBody @Valid
                                                               AccountRequest accountRequest,
                                                               @RequestParam(required = true)String accountNumber,
                                                               @RequestParam(required = true)String email){
        return  accountService.UpdateAccountDetails(accountRequest,accountNumber,email);

    }

    @PatchMapping("/partial")
    public ResponseEntity<AccountResponse>updateAccountPartially(
            @RequestBody @Valid
            AccountPartialUpdateRequest partialUpdateRequest,
            @RequestParam String accountNumber,
            @RequestParam String email
    ){
        return accountService.updateAccountDetailsPartially(partialUpdateRequest,accountNumber,email);
    }

//Delete account
    @DeleteMapping
    public ResponseEntity<String> deleteAccountDetails(@RequestParam String accountNumber,
                                       @RequestParam String email){
        return accountService.deleteAccountDetails(accountNumber,email);
    }

}
