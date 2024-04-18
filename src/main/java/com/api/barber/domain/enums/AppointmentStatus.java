package com.api.barber.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum AppointmentStatus {

    PENDING("PENDING"),
    FINISHED("FINISHED"),
    CANCELED("CANCELED");

    private final String value;
}
