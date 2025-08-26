package org.szylica.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParcelMachine {
    private final Long id;
    private final String name;
    private final Double latitude;
    private final Double longitude;
    private final String address;
}
