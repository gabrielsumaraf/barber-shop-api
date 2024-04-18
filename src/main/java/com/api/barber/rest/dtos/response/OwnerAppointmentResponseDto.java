package com.api.barber.rest.dtos.response;

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
public class OwnerAppointmentResponseDto {

    UUID id;

    String customerName;

    String customerPhone;

    String barberName;

    String barberPhone;

    LocalDate date;

    Double total;

    AppointmentStatus status;
}
