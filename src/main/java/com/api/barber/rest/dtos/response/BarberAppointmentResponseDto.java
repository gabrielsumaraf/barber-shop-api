package com.api.barber.rest.dtos.response;

import com.api.barber.domain.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BarberAppointmentResponseDto {

    private UUID id;

    private String customerName;

    private String customerPhone;

    private LocalDate date;

    private LocalTime hour;

    private String barberServiceTitle;

    private Double total;

    private AppointmentStatus status;

}
