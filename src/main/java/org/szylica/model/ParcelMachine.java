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
    @Getter
    private double latitude;
    @Getter
    private double longitude;
    private String address;
}
