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
public class OwnerAppointmentResponseDto {

    private UUID id;

    private String customerName;

    private String customerPhone;

    private String barberName;

    private String barberPhone;

    private String barberServiceTitle;

    private LocalDate date;

    private LocalTime hour;

    private Double total;

    private AppointmentStatus status;
}
