package com.api.barber.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BarberServiceStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String name;
}
