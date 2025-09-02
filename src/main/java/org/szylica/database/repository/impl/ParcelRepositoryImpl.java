package org.szylica.database.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.mappers.ParcelMapper;
import org.szylica.model.parcel.Parcel;
import org.szylica.database.repository.ParcelRepository;
import org.szylica.database.repository.db.generic.AbstractRepository;

import java.util.Map;

@Repository
public class ParcelRepositoryImpl extends AbstractRepository<Parcel, Long> implements ParcelRepository {
    protected ParcelRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected void registerMappers() {
        jdbi.registerRowMapper(Parcel.class, new ParcelMapper());
    }

    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.ofEntries(
                Map.entry("parcelId","parcel_id"),
                Map.entry("lockerId", "locker_id")
        );
    }
}
