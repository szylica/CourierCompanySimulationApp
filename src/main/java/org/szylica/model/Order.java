package org.szylica.data.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
public class Order {
    @Getter
    private final Long id;
    private final Long userId;
    private final Long parcelId;
    private final LocalDateTime createdAt;
    private final LocalDateTime deliveredAt;

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
