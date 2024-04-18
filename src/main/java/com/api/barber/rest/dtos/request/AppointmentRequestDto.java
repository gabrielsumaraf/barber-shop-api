package com.api.barber.rest.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequestDto {

    private UUID barberId;
    private UUID barberServiceId;
    private LocalDate date;
    private UUID workingHourId;
}

