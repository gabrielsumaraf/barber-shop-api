package com.api.barber.rest.dtos.response;

import com.api.barber.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String phone;

    private UserRole role;
}
