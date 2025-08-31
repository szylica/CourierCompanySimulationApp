package org.szylica.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class ParcelMachine {
    private final Long id;
    private final String name;
    @Getter
    private final Double latitude;
    @Getter
    private final Double longitude;
    private final String address;
}
