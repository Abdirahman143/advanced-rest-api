package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
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


    //get Accounts details by Id

    @GetMapping("/{id}")

    public Optional<AccountResponse>getAccountsById(
            @PathVariable(value = "id")Long id) throws UserNotFoundException {
        return accountService.getAccountById(id);
    }


    @GetMapping("/{account-number}")
    public Optional<AccountResponse>getAccountsByAccountNumber(
            @Param("account-number")String accountNumber) throws UserNotFoundException {
        return accountService.findByAccountNumber(accountNumber);
    }

}
