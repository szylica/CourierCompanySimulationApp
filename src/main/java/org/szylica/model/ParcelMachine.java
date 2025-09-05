package org.szylica.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ParcelMachine {
    private Long id;
    private String name;
    private String address;
    @Getter
    double latitude;
    @Getter
    double longitude;
}
