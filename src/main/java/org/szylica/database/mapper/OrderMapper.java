package org.szylica.database.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.szylica.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Order.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .parcelId(rs.getLong("parcel_id"))
                .createdAt(rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()))
                .deliveredAt(rs.getTimestamp("delivered_at").toInstant().atZone(ZoneId.systemDefault()))
                .build();
    }
}
