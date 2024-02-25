package com.api.barber.rest.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private String confirmPassword;
}
