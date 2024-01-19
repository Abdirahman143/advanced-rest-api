package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.customException.CustomizedExceptionHandler;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import com.example.advancedrestapi.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private Account account;
    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){

        CustomizedExceptionHandler customizedExceptionHandler = new CustomizedExceptionHandler();

        mockMvc = MockMvcBuilders.
                standaloneSetup(accountController).
                setControllerAdvice(customizedExceptionHandler). //register custom exception handling
                build();

        objectMapper= new ObjectMapper();
        account = createAccountTest();
        accountRequest = createAccountRequestTest();
        accountResponse = new AccountResponse();
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

    private AccountRequest createAccountRequestTest(){
        // Create a date that is definitely in the past, e.g., 30 years ago from now
        LocalDate localDateOfBirth = LocalDate.now().minusYears(30);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return AccountRequest.
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

    // add account details endpoint
    @DisplayName("verify add account details should return success")
    @Order(1)
    @Test
    public void addAccountDetails() throws Exception {
        String validJson = objectMapper.writeValueAsString(accountRequest);
        when(accountService.addAccount(any(AccountRequest.class))).thenReturn(ResponseEntity.ok(account));

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(account.getAccountNumber()))
                .andExpect(jsonPath("$.email").value(account.getEmail()))
                .andExpect(jsonPath("$.dateOfBirth").value(account.getDateOfBirth()))
                .andDo(print());

        verify(accountService, times(1)).addAccount(any(AccountRequest.class));
    }


    //add account with empty required fields

@DisplayName("verify add account details with empty fields should throw an errors")
@Order(2)
@Test
    public void  addAccountDetailsWithBlankRequiredFields() throws Exception {
    //arrange
        AccountRequest emptyFieldsRequest = AccountRequest.
                builder().
                firstName("").
                middleName("Abdirahman").
                lastName(null).
                accountNumber("").
                mobileNumber("0700916533").
                email("bashir.abdi@test.com").
                dateOfBirth(null).
                build();

    String JsonFields = objectMapper.writeValueAsString(emptyFieldsRequest);


    //acct and assert

    mockMvc.perform(post("/api/v1/accounts").
            contentType(MediaType.APPLICATION_JSON).
            content(JsonFields)
    ).
            andExpect(status().isBadRequest()).
            andExpect(jsonPath("$.message").value("Validation Errors")).
            andExpect(jsonPath("$.errors").isArray()).
            andExpect(jsonPath("$.errors",hasSize(4))).
            andExpect(jsonPath("$.errors",containsInAnyOrder(
                    "accountRequest : First name is required.",
                    "accountRequest : Last name is required.",
                    "accountRequest : Account number is required.",
                    "accountRequest : Date of birth is required"
            ))).
            andDo(print());


    //verify no interaction

    verify(accountService,never()).addAccount(any(AccountRequest.class));



    }

}