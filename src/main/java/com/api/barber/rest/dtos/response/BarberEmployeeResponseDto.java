package com.api.barber.rest.dtos.response;

import com.api.barber.domain.enums.UserRole;
import com.api.barber.domain.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarberEmployeeResponseDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserStatus status;

}
