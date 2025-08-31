package org.szylica.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.mappers.LockerMapper;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.repository.LockerRepository;
import org.szylica.repository.generic.AbstractRepository;

import java.util.List;
import java.util.Map;

@Repository
public class LockerRepositoryImpl extends AbstractRepository<Locker, Long> implements  LockerRepository {
    protected LockerRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected void registerMappers() {
        jdbi.registerRowMapper(Locker.class, new LockerMapper());
    }

    @Override
    public boolean isLockerFree(Long id) {

        var locker = findById(id);

        if (locker.isPresent()) {
            return locker.get().isLockerFree();
        }
        throw new IllegalStateException("Locker with id " + id + " not found");
    }

    @Override
    public List<Locker> getAllLockersFromParcelMachine(Long parcelMachineId) {

        var sql = "SELECT * FROM lockers";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapTo(Locker.class)
                .list());
    }

    @Override
    public List<Locker> isSizeLockerFree(LockerSize size, Long parcelMachineId) {

        var sql = "SELECT * FROM lockers WHERE SIZE = :size";

        return jdbi.withHandle(handle -> handle.createQuery(sql)
                .bind("size", size)
                .mapTo(Locker.class)
                .list());
    }


    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.ofEntries(Map.entry("parcelMachineId", "parcel_machine_id"));
    }
}
