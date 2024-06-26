package com.api.barber.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN"),
    BARBER("BARBER");
    private final String role;
}
