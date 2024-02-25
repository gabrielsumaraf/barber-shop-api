package com.api.barber.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

    CUSTOMER("Customer"),
    OWNER("Owner");

    private final String role;
}
