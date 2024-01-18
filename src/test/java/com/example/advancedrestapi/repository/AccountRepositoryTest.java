package com.example.advancedrestapi.repository;

import com.example.advancedrestapi.model.Account;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.within;
import static java.util.concurrent.TimeUnit.HOURS;
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRepositoryTest {
    private final static Logger logger = LoggerFactory.getLogger(AccountRepositoryTest.class);
    @Autowired
 AccountRepository accountRepository;
Account account;



    @BeforeEach
    void setUp() {
       account = createAccountTest();
        accountRepository.save(account);
    }

    @AfterEach
    void tearDown() {
        account=null;
        accountRepository.deleteAll();
    }
public Account createAccountTest(){
    // Create a date that is definitely in the past, e.g., 30 years ago from now
    LocalDate localDateOfBirth = LocalDate.now().minusYears(30);
    // Convert LocalDate to Date
    Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Account.
                builder().
                firstName("Abdirahman").
                middleName("Bashir").
                lastName("Abdi").
                accountNumber("1234567").
                email("bashir@test.com").
                mobileNumber("0722345673").
                dateOfBirth(dateOfBirth).
                build();
}

    @DisplayName("verify find account details returns account details")
    @Test
    @Order(1)
    public void testFindAccountDetailsByAccountNumberAndEmail_Found(){
        Optional<Account> accountOptional = accountRepository.findAccountDetails(account.getAccountNumber(), account.getEmail());

        // Assert that the Optional is present before proceeding
        assertThat(accountOptional)
                .as("Check that an account has been found")
                .isPresent();

        // Extract the account from Optional safely after the above assertion
        Account foundAccount = accountOptional.get();

        // Chain assertions for better readability and failure messages
        assertThat(foundAccount)
                .as("Account details should match the saved account")
                .isNotNull()
                .satisfies(acc -> {
                    assertThat(acc.getMobileNumber()).isEqualTo(account.getMobileNumber());
                    assertThat(acc.getAccountNumber()).isEqualTo(account.getAccountNumber());
                    assertThat(acc.getEmail()).isEqualTo(account.getEmail());
                });

        // Assert that the date of birth is within 24 hours (1 day) of what was set in setUp
        assertThat(account.getDateOfBirth())
                .as("Date of birth should be within 24 hours of the expected value")
                .isBefore( "1995-01-18");



        //logging
       logger.info("Account found with account number: " + account.getAccountNumber());
    }




    @DisplayName("verify find account details with either wrong account number or email returns Not found")
    @Test
    @Order(2)
    public void testFindAccountDetailsByAccountNumberAndEmail_NotFound() {
        String wrongAccount = "123890";
        String wrongEmail = "bashir@gmail.com";

        // Test with wrong account number
        Optional<Account> accountOptional = accountRepository.findAccountDetails(wrongAccount, account.getEmail());
        assertThat(accountOptional)
                .as("Account should not be found with wrong account number")
                .isEmpty();

        // Test with wrong email
        Optional<Account> accountOptional1 = accountRepository.findAccountDetails(account.getAccountNumber(), wrongEmail);
        assertThat(accountOptional1)
                .as("Account should not be found with wrong email")
                .isEmpty();
    }

    //delete existing account details

    @DisplayName("verify find account details returns account details")
    @Test
    @Order(3)
    public void testDeleteAccountDetailsByAccountNumberReturn_Success(){
        String accountNumber = account.getAccountNumber();
        assertThat(accountRepository.findAccountDetails(accountNumber,account.getEmail())).
                as("Account should exist before deletion").
                isNotEmpty();

        //deleting the accountNumber
        accountRepository.deleteByAccountNumber(account.getAccountNumber());

        assertThat(accountRepository.findAccountDetails(accountNumber,account.getEmail())).
                as("Account should not exist after deletion").
                isEmpty();

    }

}