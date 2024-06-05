package com.api.barber.rest.dtos.request;

import com.api.barber.domain.enums.BarberServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarberServiceStatusRequestDto {

    BarberServiceStatus status;
}
