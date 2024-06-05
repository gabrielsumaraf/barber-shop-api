package com.api.barber.rest.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private UserResponseDto user;
    private String token;
}
