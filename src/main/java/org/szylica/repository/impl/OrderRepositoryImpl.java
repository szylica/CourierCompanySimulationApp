package org.szylica.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.model.Order;
import org.szylica.repository.OrderRepository;
import org.szylica.repository.generic.AbstractRepository;

import java.util.Map;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    public OrderRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected Map<String, String> getFieldColumnMap(){
        return Map.ofEntries(
                Map.entry("userId", "user_id"),
                Map.entry("parcelId","parcel_id"),
                Map.entry("createdAt", "created_at"),
                Map.entry("deliveredAt", "delivered_at")
        );
    }
}
