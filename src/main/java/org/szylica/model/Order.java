package org.szylica.model;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
public class Order {
    private final Long id;
    private final Long userId;
    private final Long parcelId;
    private final LocalDateTime createdAt;
    private final LocalDateTime deliveredAt;
}
