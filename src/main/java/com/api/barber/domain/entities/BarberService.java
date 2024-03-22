package com.api.barber.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "barber_services")
public class BarberService{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column
    private String description;

    @Column
    private Double price;

    @Lob
    private byte[] image;
}
