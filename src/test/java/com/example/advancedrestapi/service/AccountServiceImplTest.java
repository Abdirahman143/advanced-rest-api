package com.example.advancedrestapi.service;

import com.example.advancedrestapi.controller.TestDataProvider;
import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.mapper.AccountResponseMapper;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.repository.AccountRepository;
import com.example.advancedrestapi.request.AccountPartialUpdateRequest;
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
import static org.mockito.Mockito.when;
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


        //verify
        verify(accountRepository).findAccountDetails(accountNumber,email);


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

        //verify
        verify(accountRepository).findAccountDetails(accountNumber,email);
    }

    @Test
    @DisplayName("Verify update account details should return updated account response")
    @Order(5)
    void updateAccountDetails_ShouldReturnUpdatedAccountResponse() {
        // Given
        String accountNumber = "123456789";
        String email = "john.smith@example.com";
        AccountRequest updatedAccountRequest = TestDataDriver.createTestAccountRequest();
        Account existingAccount = TestDataDriver.createTestAccount();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        // When
        ResponseEntity<AccountResponse> responseEntity = accountService.UpdateAccountDetails(updatedAccountRequest, accountNumber, email);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountResponse updatedAccountResponse = responseEntity.getBody();
        assertThat(updatedAccountResponse).isNotNull();
        assertThat(updatedAccountResponse.getFirstName()).isEqualTo(updatedAccountRequest.getFirstName());
        assertThat(updatedAccountResponse.getMiddleName()).isEqualTo(updatedAccountRequest.getMiddleName());
        assertThat(updatedAccountResponse.getMobileNumber()).isEqualTo(updatedAccountRequest.getMobileNumber());
        assertThat(updatedAccountResponse.getDateOfBirth()).isEqualTo(updatedAccountRequest.getDateOfBirth());

        //verify
        verify(accountRepository,times(1)).save(any(Account.class));
        verify(accountRepository,times(1)).findAccountDetails(accountNumber,email);

    }

    @Test
    @DisplayName("Verify update account details with invalid account should throw exception")
    @Order(6)
    void updateAccountDetails_WithInvalidAccount_ShouldThrowUserNotFoundException() {
        // Given
        String accountNumber = "invalidAccount";
        String email = "invalidEmail";
        AccountRequest updatedAccountRequest = TestDataDriver.createTestAccountRequest();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> {
            accountService.UpdateAccountDetails(updatedAccountRequest, accountNumber, email);
        });

        //verify
        verify(accountRepository,times(1)).findAccountDetails(accountNumber,email);
        verify(accountRepository,never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Verify partial update account details should return updated account response")
    @Order(7)
    void updateAccountDetailsPartially_ShouldReturnUpdatedAccountResponse() {
        // Given
        String accountNumber = "123456789";
        String email = "john.smith@example.com";
        AccountPartialUpdateRequest partialUpdateRequest = TestDataDriver.createPartialUpdateRequest();
        Account existingAccount = TestDataDriver.createTestAccount();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);

        // When
        ResponseEntity<AccountResponse> responseEntity = accountService.updateAccountDetailsPartially(partialUpdateRequest, accountNumber, email);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        AccountResponse updatedAccountResponse = responseEntity.getBody();
        assertThat(updatedAccountResponse).isNotNull();
        assertThat(updatedAccountResponse.getMobileNumber()).isEqualTo(partialUpdateRequest.getMobileNumber());
        assertThat(updatedAccountResponse.getDateOfBirth()).isEqualTo(partialUpdateRequest.getDateOfBirth());

        //verify
        verify(accountRepository).findAccountDetails(accountNumber,email);
        verify(accountRepository,times(1)).save(any(Account.class));

    }

    @Test
    @DisplayName("Verify partial update account details with invalid account should throw exception")
    @Order(8)
    void updateAccountDetailsPartially_WithInvalidAccount_ShouldThrowUserNotFoundException() {
        // Given
        String accountNumber = "invalidAccount";
        String email = "invalidEmail";
        AccountPartialUpdateRequest partialUpdateRequest = TestDataDriver.createPartialUpdateRequest();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UserNotFoundException.class, () -> {
            accountService.updateAccountDetailsPartially(partialUpdateRequest, accountNumber, email);
        });

        //verify
        verify(accountRepository,never()).save(any(Account.class));
        verify(accountRepository,times(1)).findAccountDetails(accountNumber,email);
    }

    @Test
    @DisplayName("Verify delete account should return success message")
    @Order(9)
    void deleteAccountDetails_ShouldReturnSuccessMessage() {
        // Given
        String accountNumber = "123456789";
        String email = "john.smith@example.com";
        Account existingAccount = TestDataDriver.createTestAccount();
        when(accountRepository.findAccountDetails(accountNumber, email)).thenReturn(Optional.of(existingAccount));

        // When
        ResponseEntity<String> responseEntity = accountService.deleteAccountDetails(accountNumber, email);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Account with account number " + accountNumber + " has been permanently deleted.");

        // Verify that deleteByAccountNumber method is called
        verify(accountRepository, times(1)).deleteByAccountNumber(accountNumber);
    }
}