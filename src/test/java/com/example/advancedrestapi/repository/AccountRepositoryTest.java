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

    @DisplayName("verify find account details returns 200")
    @Test
    @Order(1) // if you need to order your tests
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

}