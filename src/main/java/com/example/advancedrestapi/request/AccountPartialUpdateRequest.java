package com.example.advancedrestapi.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountPartialUpdateRequest {
    private String mobileNumber;
    private Date dateOfBirth;
}
