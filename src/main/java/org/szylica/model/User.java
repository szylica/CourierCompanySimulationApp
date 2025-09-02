package org.szylica.model;

import lombok.*;

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
    private Double latitude;
    @Getter
    private Double longitude;
}
