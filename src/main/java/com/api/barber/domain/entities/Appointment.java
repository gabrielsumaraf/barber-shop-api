package com.api.barber.domain.entities;

import com.api.barber.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private User barber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barber_service_id")
    private BarberService barberService;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "total")
    Double total;
    @ManyToOne
    @JoinColumn(name = "working_hour_id")
    private WorkingHour workingHour;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
