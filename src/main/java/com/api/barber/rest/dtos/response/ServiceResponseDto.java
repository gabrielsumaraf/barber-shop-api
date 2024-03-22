package com.api.barber.rest.dtos.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDto {

    private UUID id;

    private String title;

    private String description;

    private Double price;

    private byte[] image;
}
