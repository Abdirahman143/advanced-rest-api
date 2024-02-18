package com.example.advancedrestapi.service;

import com.example.advancedrestapi.controller.TestDataProvider;
import com.example.advancedrestapi.customException.UserNotFoundException;
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
import java.util.Optional;
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
    @DisplayName("Verify find account details should return account response")
    @Order(3)
    void findAccountDetails_ShouldReturnAccountResponse() {
        // Given
        String accountNumber = "123456789";
        String email = "john.smith@example.com";
        Account testAccount = TestDataDriver.createTestAccount();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.of(testAccount));

        // When
        Optional<AccountResponse> optionalAccountResponse = accountService.findAccountDetails(accountNumber, email);

        // Then
         assertThat(optionalAccountResponse).isPresent();
        AccountResponse accountResponse = optionalAccountResponse.get();
        assertThat(accountResponse.getFirstName()).isEqualTo(testAccount.getFirstName());
        assertThat(accountResponse.getEmail()).isEqualTo(testAccount.getEmail());
        assertThat(accountResponse.getAccountNumber()).isEqualTo(testAccount.getAccountNumber());
        assertThat(accountResponse.getMobileNumber()).isEqualTo(testAccount.getMobileNumber());


    }

    @Test
    @DisplayName("Verify find account details with invalid account should throw exception")
    @Order(4)
    void findAccountDetails_WithInvalidAccount_ShouldThrowUserNotFoundException() {
        // Given
        String accountNumber = "invalidAccount";
        String email = "invalidEmail";

        // When
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> {
            accountService.findAccountDetails(accountNumber, email);
        });
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