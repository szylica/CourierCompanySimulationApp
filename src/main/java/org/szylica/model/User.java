package org.szylica.model;

import lombok.*;
import org.szylica.util.NearestPoint;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {
    @Getter
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @Getter
    private double latitude;
    @Getter
    private double longitude;

    public double calcDistanceFromParcelMachine(ParcelMachine parcelMachine) {
        return NearestPoint.haversine(latitude, longitude, parcelMachine.latitude, parcelMachine.longitude);
    }
}
