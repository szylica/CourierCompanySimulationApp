package org.szylica.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Builder
@ToString
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
