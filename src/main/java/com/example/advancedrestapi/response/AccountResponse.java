package com.example.advancedrestapi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String accountNumber;
    private String mobileNumber;
    private Date dateOfBirth;
    private String email;

}
