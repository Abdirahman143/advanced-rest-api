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
@Table(name = "accounts_tbl",

        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique",columnNames = "email"),
                @UniqueConstraint(name = "Account_number_unique",columnNames = "accountNumber"),
                @UniqueConstraint(name = "mobile_number_unique", columnNames = "mobileNumber")
        }
)
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required.")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Account number is required.")
    @Column(unique = true)
    private String accountNumber;

    @Size(min = 10, max = 15, message = "Mobile number must be between 10 and 15 digits.")
    @Column(unique = true)
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Email(regexp = "[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email format is invalid. Please provide a valid email address.")
    @Column(unique = true)
    @NotBlank(message = "email is required!")
    private String email;

}
