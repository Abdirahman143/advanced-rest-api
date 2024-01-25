package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.customException.CustomizedExceptionHandler;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountRequest;
import com.example.advancedrestapi.response.AccountResponse;
import com.example.advancedrestapi.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
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
        LocalDate dob = LocalDate.now().minusYears(30);
        account = TestDataProvider.createAccount("Abdirahman", "Abdi", "1234567", "bashir@test.com", "0722345673", dob);
        accountRequest = TestDataProvider.createAccountRequest("Abdirahman", "Abdi", "1234567", "bashir@test.com", "0722345673", dob);
        accountResponse = new AccountResponse();
    }


    // add account details endpoint
    @DisplayName("verify add account details should return success")
    @Order(1)
    @Test
    public void addAccountDetails() throws Exception {
       LocalDate START_OF_1994 = LocalDate.of(1994, 1, 1);
       LocalDate END_OF_1994 = LocalDate.of(1994, 12, 31);

        // Arrange
        String validJson = objectMapper.writeValueAsString(accountRequest);
        when(accountService.addAccount(any(AccountRequest.class))).thenReturn(ResponseEntity.ok(account));

        // Act
        MvcResult result = mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(account.getAccountNumber()))
                .andExpect(jsonPath("$.email").value(account.getEmail()))
                .andDo(print())
                .andReturn();

        // Assert
        String jsonResponse = result.getResponse().getContentAsString();
        DocumentContext jsonContext = JsonPath.parse(jsonResponse);
        LocalDate dob = LocalDate.parse(jsonContext.read("$.dateOfBirth", String.class));

        assertTrue(!dob.isBefore(START_OF_1994) && !dob.isAfter(END_OF_1994),
                "Date of birth is not within the year 1994");

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
                    "accountRequest : Date of birth is required",
                    "accountRequest : Account number is required."
            ))).
            andDo(print());


    //verify no interaction

    verify(accountService,never()).addAccount(any(AccountRequest.class));



    }
// email format validation

    @DisplayName("verify add account details with invalid email format should throw an errors")
    @Order(3)
    @Test
    public void  addAccountDetailsWithInvalidEmailFormat() throws Exception {

        //Arrange
        // Create a date that is definitely in the past, e.g., 30 years ago from now
        LocalDate localDateOfBirth = LocalDate.now().minusYears(30);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());

      AccountRequest invalidEmailFormat =AccountRequest.
              builder().
              firstName("Abdi").
              middleName("Abdirahman").
              lastName("Abdi").
              accountNumber("345678").
              mobileNumber("0700916533").
              email("bashir.abdi@test").
              dateOfBirth(dateOfBirth).
              build();

      String  jsonFormat = objectMapper.writeValueAsString(invalidEmailFormat);


      //act and assert

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON).
                content(jsonFormat)
        ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").value("Validation Errors")).
                andExpect(jsonPath("$.errors").isArray()).
                andExpect(jsonPath("$.errors",hasSize(1))).
                andExpect(jsonPath("$.errors[0]").value( "accountRequest : Email format is invalid. Please provide a valid email address.")).
                andDo(print());

        //verify no interaction
        verify(accountService, never()).addAccount(any(AccountRequest.class));
    }


    //Invalid mobile number size and date of birth format

    @DisplayName("verify add account details with invalid phone number format and Future DOB should throw an errors")
    @Order(4)
    @Test
    public void  addAccountDetailsWithInvalidMobileNumberSizeAndDateOfBirthFormat() throws Exception {

        //arrange
        LocalDate localDateOfBirth = LocalDate.now().plusYears(2);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AccountRequest invalidRequest = AccountRequest.
                builder().
                firstName("Abdi").
                middleName("Abdirahman").
                lastName("Abdi").
                accountNumber("345678").
                mobileNumber("070091653").
                email("bashir.abdi@test.com").
                dateOfBirth(dateOfBirth).
                build();

        String invalidObject = objectMapper.writeValueAsString(invalidRequest);


        //act and assert

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidObject)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Errors"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors",hasSize(2)))
                .andExpect(jsonPath("$.errors",containsInAnyOrder(
                        "accountRequest : Mobile number must be between 10 and 15 digits.",
                        "accountRequest : Date of birth must be in the past"
                )))
                .andDo(print());


        //verify no interaction between controller and service layer


        verify(accountService, never()).addAccount(any(AccountRequest.class));
    }









}