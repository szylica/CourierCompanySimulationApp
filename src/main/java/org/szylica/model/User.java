package org.szylica.model;

import lombok.*;

@RequiredArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {
    @Getter
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
