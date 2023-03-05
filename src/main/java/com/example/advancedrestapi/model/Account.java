package com.example.advancedrestapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account_tbl",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique",columnNames = "email"),
                @UniqueConstraint(name = "Account_number_unique",columnNames = "accountNumber"),
                @UniqueConstraint(name = "mobile_number_unique", columnNames = "mobileNumber")
        }
)
@Builder
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long Id;

    @NotBlank
    @NotNull(message = "first name is required!!!!")
    private String firstName;
    private String middleName;
    @NotBlank
    @NotNull(message = "please provide last name")
    private String lastName;
    @NotBlank
    @NotNull
    private String accountNumber;
    @Size(min = 10, max = 15, message = "please provide valid mobile number")
    private String mobileNumber;
    @Past(message = "date of birth should be past")
    private Date DateOfBirth;
    @Email(regexp ="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message ="invalid email format"
    )
    private String email;


}
