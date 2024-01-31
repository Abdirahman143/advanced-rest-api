package com.example.advancedrestapi.controller;

import com.example.advancedrestapi.customException.CustomizedExceptionHandler;
import com.example.advancedrestapi.customException.UserNotFoundException;
import com.example.advancedrestapi.model.Account;
import com.example.advancedrestapi.request.AccountPartialUpdateRequest;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
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



// add account details with duplicate account number

    @DisplayName("Verify add account with duplicate account number should return error")
    @Test
    @Order(5)
    public void shouldReturnErrorWhenAddingAccountWithDuplicateNumber() throws Exception {
        //arrange
        LocalDate localDateOfBirth = LocalDate.now().minusYears(22);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AccountRequest duplicateRequest =AccountRequest.
                builder().
                firstName("Asho").
                middleName("Ahmed").
                lastName("Abdi").
                accountNumber("1234567").
                mobileNumber("0700815432").
                dateOfBirth(dateOfBirth).
                email("asho.ahmed1@gmail.com").
                build();

        String jsonRequest = objectMapper.writeValueAsString( duplicateRequest);
        String validRequest =objectMapper.writeValueAsString(accountRequest);

        //act and assert for first valid request

        when(accountService.addAccount(any(AccountRequest.class))).thenReturn(ResponseEntity.ok(account));
        mockMvc.perform(post("/api/v1/accounts").
                contentType(MediaType.APPLICATION_JSON).
                content(validRequest)
        ).
                andExpect(status().isOk()).
                andDo(print());

        //verify the interaction for the first valid request
        verify(accountService).addAccount(any(AccountRequest.class));

      //simulate a duplicate entry scenario
        when(accountService.addAccount(any(AccountRequest.class))).
                thenThrow(new DataIntegrityViolationException("An entity with the same identifier (e.g., account number) already exists."));

        //act and assert
        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
        ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").value("Data Integrity Error")).
                andExpect(jsonPath("$.errors").isArray()).
                andExpect(jsonPath("$.errors",hasSize(1))).
                andExpect(jsonPath("$.errors[0]").value("Operation cannot be performed due to a data integrity violation.")).
                andDo(print());


        //verify no interaction
        verify(accountService,times(2)).addAccount(any(AccountRequest.class));

    }

//add account details with duplicate email


    @DisplayName("Verify add account with duplicate email should return error")
    @Test
    @Order(6)
    public void shouldReturnErrorWhenAddingAccountWithDuplicateEmail() throws Exception {
        //arrange
        LocalDate localDateOfBirth = LocalDate.now().minusYears(22);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AccountRequest duplicateEmailRequest =AccountRequest.
                builder().
                firstName("Asho").
                middleName("Ahmed").
                lastName("Abdi").
                accountNumber("12345679").
                mobileNumber("0700815432").
                dateOfBirth(dateOfBirth).
                email("bashir@test.com").
                build();

        String jsonRequest = objectMapper.writeValueAsString( duplicateEmailRequest);
        String validRequest =objectMapper.writeValueAsString(accountRequest);

        //Act and Assert for first valid request

        when(accountService.addAccount(any(AccountRequest.class))).thenReturn(ResponseEntity.ok(account));
        mockMvc.perform(post("/api/v1/accounts").
                        contentType(MediaType.APPLICATION_JSON).
                        content(validRequest)
                ).
                andExpect(status().isOk()).
                andDo(print());

        //verify the interaction for the first valid request
        verify(accountService).addAccount(any(AccountRequest.class));

        //simulate a duplicate entry scenario
        when(accountService.addAccount(any(AccountRequest.class))).
                thenThrow(new DataIntegrityViolationException("An entity with the same identifier (e.g., account number) already exists."));

        //act and assert
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.message").value("Data Integrity Error")).
                andExpect(jsonPath("$.errors").isArray()).
                andExpect(jsonPath("$.errors",hasSize(1))).
                andExpect(jsonPath("$.errors[0]").value("Operation cannot be performed due to a data integrity violation.")).
                andDo(print());


        //verify no interaction
        verify(accountService,times(2)).addAccount(any(AccountRequest.class));
    }


    //verify get all account details should return success

    @DisplayName("verify get all acount details should return success")
    @Test
    @Order(7)
    public void getAllAccountDetailsShouldReturnSuccess() throws Exception {
        //Arrange
       List<AccountResponse>accountResponse = TestDataProvider.createTestAccountResponses();
       ResponseEntity<List<AccountResponse>>expectedResponse =new ResponseEntity<>(accountResponse,HttpStatus.OK);

        when(accountService.getAllAccounts()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$",hasSize(accountResponse.size()))).
                andExpect(jsonPath("$[0].email").value(accountResponse.get(0).getEmail()))
                .andDo(print());


        //verify there is one interaction

        verify(accountService,times(1)).getAllAccounts();

    }

    @DisplayName("Verify get account with Valid Account Number and Email should return success")
    @Test
    @Order(8)
    public void getAccountDetailsWithValidAccountNumberAndEmailReturnSuccess() throws Exception {
        // Arrange

        String accountNumber = TestDataProvider.createTestAccountResponses().get(0).getAccountNumber();
        String email = TestDataProvider.createTestAccountResponses().get(0).getEmail();
        when(accountService.findAccountDetails(accountNumber, email)).
                thenReturn(Optional.of(TestDataProvider.createTestAccountResponses().get(0)));
        //Act and Assert
    mockMvc.perform(get("/api/v1/accounts/details")
            .contentType(MediaType.APPLICATION_JSON)
            .param("accountNumber",accountNumber)
            .param("email",email)
    ).
            andExpect(status().isOk()).
            andExpect(header().string("Content-Type", "application/json")).
            andExpect(jsonPath("$.accountNumber").value(accountNumber)).
            andExpect(jsonPath("$.email").value(email)).
            andDo(print());


    //verify the interaction
    verify(accountService,times(1)).findAccountDetails(accountNumber,email);



}
    @DisplayName("Verify get account with InValid Account Number and Email should throw an error")
    @Test
    @Order(9)
    public void getAccountDetailsWithInValidAccountNumberAndEmailReturnSuccess() throws Exception {
        //Arrange
        String accountNumber ="45678901";
        String email = "cbc.test@gmail.com";
        String message = "account number : " + accountNumber + " and email : " + email + "not found. Please try with a valid account number and email.";
        when(accountService.findAccountDetails(accountNumber,email)).
                thenThrow(new UserNotFoundException(message));

        //Act and Assert
        mockMvc.perform(get("/api/v1/accounts/details").
                contentType(MediaType.APPLICATION_JSON).
                param("accountNumber",accountNumber).
                param("email",email)
                ).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("$.message").value("Resource Not Found")).
                andExpect(jsonPath("$.errors[0]").value(message)).
                andDo(print());


        //verify the service interaction
        verify(accountService,times(1)).findAccountDetails(accountNumber,email);


    }
    @DisplayName("Test: Updating account details with valid account number and email should succeed")
    @Test
    @Order(10)
    public void shouldUpdateAccountDetailsSuccessfullyWithValidData() throws Exception {
        // Arrange
        AccountResponse testAccount = TestDataProvider.createTestAccountResponses().get(0);
        String accountNumber = testAccount.getAccountNumber();
        String email = testAccount.getEmail();

        AccountRequest accountRequest = AccountRequest.builder()
                .firstName("Mohamed")
                .middleName("Ahmed")
                .lastName("Kahim")
                .mobileNumber(testAccount.getMobileNumber())
                .accountNumber(accountNumber)
                .email(email)
                .dateOfBirth(testAccount.getDateOfBirth())
                .build();

        AccountResponse expectedUpdatedAccount = AccountResponse.builder()
                .firstName(accountRequest.getFirstName())
                .middleName(accountRequest.getMiddleName())
                .lastName(accountRequest.getLastName())
                .mobileNumber(testAccount.getMobileNumber())
                .accountNumber(accountNumber)
                .email(email)
                .dateOfBirth(testAccount.getDateOfBirth())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(accountRequest);

        when(accountService.UpdateAccountDetails(any(AccountRequest.class), eq(accountNumber), eq(email)))
                .thenReturn(new ResponseEntity<>(expectedUpdatedAccount, HttpStatus.OK));

        // Act and Assert
        mockMvc.perform(put("/api/v1/accounts/{accountNumber}/{email}", accountNumber, email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.firstName").value(expectedUpdatedAccount.getFirstName()))
                .andDo(print());

        // Verify
        verify(accountService, times(1))
                .UpdateAccountDetails(any(AccountRequest.class), eq(accountNumber), eq(email));
    }

    //update account details with either wrong account number or email

    @DisplayName("Test: Updating account details with Invalid account number or email should throw an error")
    @Test
    @Order(11)
    public void shouldUpdateAccountDetailsUnSuccessfulWithInValidData() throws Exception {
        // Arrange
        AccountResponse testAccount =TestDataProvider.createTestAccountResponses().get(0);
        //wrong email
        String email ="omar.bashir@test.com";
        //correct accountNumber
        String accountNumber = testAccount.getAccountNumber();
        //account request --> what goes to the body of the request
        AccountRequest testRequest = AccountRequest.
                builder().
                firstName("Shukri").
                middleName("Kahin").
                lastName(testAccount.getLastName()).
                email(email).
                accountNumber(accountNumber).
                mobileNumber(testAccount.getMobileNumber()) .
                dateOfBirth(testAccount.getDateOfBirth()).
                build();

        //ExpectedResponse returns the api

        AccountResponse expectedResponse = AccountResponse.
                builder().
                firstName(testRequest.getFirstName()).
                middleName(testRequest.getMiddleName()).
                lastName(testRequest.getLastName()).
                email(testRequest.getEmail()).
                accountNumber(testRequest.getAccountNumber()).
                mobileNumber(testRequest.getMobileNumber()).
                dateOfBirth(testRequest.getDateOfBirth()).
                build();
        String formattedJson = objectMapper.writeValueAsString(testRequest);
        String message = "account number : " + accountNumber + " and email : " + email + "not found. Please try with a valid account number and email.";
        //Act and Assert
        when(accountService.UpdateAccountDetails
                (any(AccountRequest.class),eq(accountNumber),eq(email))).
                thenThrow(new UserNotFoundException(message))  ;
        mockMvc.perform(put("/api/v1/accounts/{accountNumber}/{email}",accountNumber,email).
                contentType(MediaType.APPLICATION_JSON)
                .content(formattedJson)).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("$.message").value("Resource Not Found"))
                .andExpect(jsonPath("$.errors[0]").value(message)).
                andDo(print());


        //verify there is on interaction
        verify(accountService,times(1)).
                UpdateAccountDetails(any(AccountRequest.class),eq(accountNumber),eq(email));


    }

    @DisplayName("Test: Updating Partially account details with valid should return success")
    @Test
    @Order(12)
    public void shouldPartialUpdateAccountDetailsSuccessfullyWithValidData() throws Exception {
        // Arrange

        //arrange
        LocalDate localDateOfBirth = LocalDate.now().minusYears(26);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        AccountResponse originalAccountResponse = TestDataProvider.createTestAccountResponses().get(0);
        String accountNumber = originalAccountResponse.getAccountNumber();
        String email = originalAccountResponse.getEmail();

        //What goes to the body of the request---> fields to be updated
        AccountPartialUpdateRequest partialUpdateRequest =
                AccountPartialUpdateRequest.
                        builder().
                        mobileNumber("0733816522").
                        dateOfBirth(dateOfBirth).
                        build();

        //Expected Response
        AccountResponse expectedResponse = AccountResponse.
                builder().
                firstName(originalAccountResponse.getFirstName()).
                middleName(originalAccountResponse.getMiddleName()).
                lastName(originalAccountResponse.getLastName()).
                accountNumber(originalAccountResponse.getAccountNumber()).
                email(originalAccountResponse.getEmail()).
                mobileNumber(partialUpdateRequest.getMobileNumber()).
                dateOfBirth(partialUpdateRequest.getDateOfBirth()).
                build();

        when(accountService.
                updateAccountDetailsPartially(any(AccountPartialUpdateRequest.class),eq(accountNumber),eq(email))).
                thenReturn(new ResponseEntity<>(expectedResponse,HttpStatus.OK));

        String jsonBody = objectMapper.writeValueAsString(partialUpdateRequest);

        //Act and Assert

        mockMvc.perform(patch
                ("/api/v1/accounts/partial/{accountNumber}/{email}",accountNumber,email)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth)).
                andExpect(jsonPath("$.mobileNumber").value(expectedResponse.getMobileNumber())).
                andDo(print());


        //verify

        verify(accountService,times(1)).
                updateAccountDetailsPartially(any(AccountPartialUpdateRequest.class),eq(accountNumber),eq(email));

    }

    @DisplayName("Test: Updating Partially account details with invalid accountNumber and email should Throw an error!")
    @Test
    @Order(13)
    public void shouldPartialUpdateAccountDetailsUnSuccessfulWithInValidEmailOrAccountNumber() throws Exception {
        // Arrange

        //arrange
        LocalDate localDateOfBirth = LocalDate.now().minusYears(26);
        // Convert LocalDate to Date
        Date dateOfBirth = Date.from(localDateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AccountResponse originalAccountResponse = TestDataProvider.createTestAccountResponses().get(0);
        //wrong accountNumber
        String accountNumber ="2907666";
        String email = originalAccountResponse.getEmail();

        //What goes to the body of the request---> fields to be updated
        AccountPartialUpdateRequest partialUpdateRequest =
                AccountPartialUpdateRequest.
                        builder().
                        mobileNumber("0733816522").
                        dateOfBirth(dateOfBirth).
                        build();

        //Expected Response
        AccountResponse expectedResponse = AccountResponse.
                builder().
                firstName(originalAccountResponse.getFirstName()).
                middleName(originalAccountResponse.getMiddleName()).
                lastName(originalAccountResponse.getLastName()).
                accountNumber(originalAccountResponse.getAccountNumber()).
                email(originalAccountResponse.getEmail()).
                mobileNumber(partialUpdateRequest.getMobileNumber()).
                dateOfBirth(partialUpdateRequest.getDateOfBirth()).
                build();
        String message = "account number : " + accountNumber + " and email : " + email + "not found. Please try with a valid account number and email.";

        when(accountService.
                updateAccountDetailsPartially(any(AccountPartialUpdateRequest.class),eq(accountNumber),eq(email))).
               thenThrow(new UserNotFoundException(message));

        String jsonBody = objectMapper.writeValueAsString(partialUpdateRequest);
       //Act and Assert

        mockMvc.perform(patch
                        ("/api/v1/accounts/partial/{accountNumber}/{email}",accountNumber,email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody)
        ).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("$.message").value("Resource Not Found")).
                andExpect(jsonPath("$.errors[0]").value(message)).
                andDo(print());




        //verify

        verify(accountService,times(1)).
                updateAccountDetailsPartially(any(AccountPartialUpdateRequest.class),eq(accountNumber),eq(email));

    }


    //Delete account details with valid accountNumber and email

}