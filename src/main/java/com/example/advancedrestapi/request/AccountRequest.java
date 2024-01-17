package com.example.advancedrestapi.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {




    @NotBlank(message = "First name is required.")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Account number cannot be blank.")
    @Column(unique = true)
    private String accountNumber;

    @Size(min = 10, max = 15, message = "Mobile number must be between 10 and 15 digits.")
    @Column(unique = true)
    private String mobileNumber;

    @Past(message = "Date of birth must be a date in the past.")
    private Date dateOfBirth;

    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email format is invalid. Please provide a valid email address.")
    @Column(unique = true)
    private String email;
}
