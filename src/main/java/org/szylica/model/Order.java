package org.szylica.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Order {
    @Getter
    private Long id;
    private Long userId;
    private Long parcelId;
    private ZonedDateTime createdAt;
    private ZonedDateTime deliveredAt;

    public Order withParcelId(Long parcelId) {
        return Order.builder()
                .id(id)
                .userId(userId)
                .parcelId(parcelId)
                .createdAt(createdAt)
                .deliveredAt(deliveredAt)
                .build();
    }
}
