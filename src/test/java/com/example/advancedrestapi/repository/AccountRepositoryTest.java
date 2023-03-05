package com.example.advancedrestapi.repository;

import com.example.advancedrestapi.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;


    @Test
    public void addAccount(){
        Account account = Account.
                builder().
                firstName("Abdirahman").
                middleName("Bashir").
                lastName("Abdi").
                email("abdirahman.abdi@gmail.com").
                mobileNumber("0700987654").
                accountNumber("12345678").
                build();

        accountRepository.save(account);
    }

}