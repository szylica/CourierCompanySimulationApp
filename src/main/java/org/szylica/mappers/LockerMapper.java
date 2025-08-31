package org.szylica.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LockerMapper implements RowMapper<Locker> {
    @Override
    public Locker map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Locker.builder()
                .id(rs.getLong("id"))
                .parcelMachineId(rs.getLong("parcel_machine_id"))
                .size(LockerSize.valueOf(rs.getString("size").toUpperCase()))
                .status(LockerStatus.valueOf(rs.getString("status").toUpperCase()))
                .build();
    }
}
