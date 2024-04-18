package com.api.barber.rest.dtos.request;

import com.api.barber.domain.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerAppointmentResponseDto {

    UUID id;

    String barberName;

    LocalDate date;

    Double total;

    AppointmentStatus status;
}
