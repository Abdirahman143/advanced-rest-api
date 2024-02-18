package com.example.advancedrestapi.service;

import com.example.advancedrestapi.controller.TestDataProvider;
import com.example.advancedrestapi.mapper.AccountResponseMapper;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    static private final Logger logger = LoggerFactory.getLogger(AccountServiceImplTest.class);

    @BeforeEach
    void setUp() {
        LocalDate dob = LocalDate.now().minusYears(30);
        account = TestDataProvider.createAccount("John", "John", "1234567", "John88@test.com", "0710816522", dob);
        accountRequest = TestDataProvider.createAccountRequest("John", "John", "1234567", "John88@test.com", "0710816522", dob);

    }

    @Test
    @DisplayName("Verify that add account details with valid data return success!")
    @Order(1)
    void addAccount() {
        // Given
        when(accountRepository.save(any(Account.class))).thenReturn(account);


        // When
        ResponseEntity<Account> responseEntity = accountService.addAccount(accountRequest);

        // Then
        assertThat(responseEntity)
                .isNotNull()
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.CREATED)
                .extracting(ResponseEntity::getBody)
                .isEqualTo(account);

        //verify
        verify(accountRepository).save(any(Account.class));
    }


    @Test
    @DisplayName("Verify get all account details should return success")
    @Order(2)
    void getAllAccounts() {
        // Given
        List<Account> expectedAccounts = TestDataDriver.createTestAccounts();
        List<AccountResponse> expectedAccountResponses =  expectedAccounts.stream()
                .map(AccountResponseMapper::MapToResponse)
                .collect(Collectors.toList());

        when(accountRepository.findAll()).thenReturn(expectedAccounts);

        // When
        ResponseEntity<List<AccountResponse>> responseEntity = accountService.getAllAccounts();

        // Then
        assertThat(responseEntity)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.FOUND);

        assertThat(responseEntity.getBody())
                .isNotNull()
                .hasSize(expectedAccounts.size())
                .extracting(
                        AccountResponse::getFirstName,
                        AccountResponse::getMiddleName,
                        AccountResponse::getLastName,
                        AccountResponse::getAccountNumber,
                        AccountResponse::getMobileNumber,
                        AccountResponse::getEmail,
                        AccountResponse::getDateOfBirth
                )
                .containsExactlyElementsOf(expectedAccountResponses.stream()
                        .map(accountResponse ->
                                tuple(
                                        accountResponse.getFirstName(),
                                        accountResponse.getMiddleName(),
                                        accountResponse.getLastName(),
                                        accountResponse.getAccountNumber(),
                                        accountResponse.getMobileNumber(),
                                        accountResponse.getEmail(),
                                        accountResponse.getDateOfBirth()
                                )
                        )
                        .toList()
                );
    }

    @Test
    void findAccountDetails() {
    }

    @Test
    void updateAccountDetails() {
    }


    @Test
    void updateAccountDetailsPartially() {
    }

    @Test
    void deleteAccountDetails() {
    }
}