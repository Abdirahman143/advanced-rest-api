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
    public ResponseEntity<Account>Save(@Valid @RequestBody  AccountRequest accountRequest){
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


    @PutMapping("/{accountNumber}/{email}")
    public ResponseEntity<AccountResponse>updateAccountDetails(@Valid @RequestBody  AccountRequest accountRequest,
                                                               @PathVariable String accountNumber,
                                                               @PathVariable String email)throws UserNotFoundException{

        return  accountService.UpdateAccountDetails(accountRequest,accountNumber,email);

    }

    @PatchMapping("/partial/{accountNumber}/{email}")
    public ResponseEntity<AccountResponse>updateAccountPartially(
            @RequestBody @Valid
            AccountPartialUpdateRequest partialUpdateRequest,
            @PathVariable String accountNumber,
            @PathVariable String email)throws UserNotFoundException{

        return accountService.updateAccountDetailsPartially(partialUpdateRequest,accountNumber,email);
    }

//Delete account

    @DeleteMapping("/{accountNumber}/{email}")
    public ResponseEntity<String> deleteAccountDetails(@PathVariable String accountNumber,
                                                       @PathVariable String email)throws UserNotFoundException{

        return accountService.deleteAccountDetails(accountNumber,email);
    }

}
