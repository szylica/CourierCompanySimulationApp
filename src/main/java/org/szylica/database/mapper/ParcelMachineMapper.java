package org.szylica.database.mappers;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.szylica.model.ParcelMachine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParcelMachineMapper implements RowMapper<ParcelMachine> {
    @Override
    public ParcelMachine map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ParcelMachine.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .latitude(rs.getDouble("latitude"))
                .longitude(rs.getDouble("longitude"))
                .address(rs.getString("address"))
                .build();
    }
}
