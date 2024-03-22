package com.api.barber.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    CUSTOMER("CUSTOMER"),
    OWNER("OWNER"),
    BARBER("BARBER");
    private final String role;
}
