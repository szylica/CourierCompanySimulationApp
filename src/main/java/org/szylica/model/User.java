package org.szylica.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class User {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final Double latitude;
    private final Double longitude;
}
