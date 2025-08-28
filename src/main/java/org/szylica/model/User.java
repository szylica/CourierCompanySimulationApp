package org.szylica.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class User {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    @Getter
    private final Double latitude;
    @Getter
    private final Double longitude;
}
