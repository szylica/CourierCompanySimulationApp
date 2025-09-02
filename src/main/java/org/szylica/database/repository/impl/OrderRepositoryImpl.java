package org.szylica.database.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.database.mapper.OrderMapper;
import org.szylica.model.Order;
import org.szylica.database.repository.OrderRepository;
import org.szylica.database.repository.generic.AbstractRepository;

import java.util.Map;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    public OrderRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected void registerMappers() {
        jdbi.registerRowMapper(Order.class, new OrderMapper());
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
