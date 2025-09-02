package org.szylica.database.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.szylica.model.parcel.Parcel;
import org.szylica.model.parcel.enums.ParcelStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParcelMapper implements RowMapper<Parcel> {
    @Override
    public Parcel map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Parcel.builder()
                .id(rs.getLong("id"))
                .lockerId(rs.getLong("locker_id"))
                .width(rs.getInt("width"))
                .height(rs.getInt("height"))
                .depth(rs.getInt("depth"))
                .status(ParcelStatus.valueOf(rs.getString("status")))
                .build();
    }
}
