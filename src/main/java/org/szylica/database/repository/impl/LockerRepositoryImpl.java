package org.szylica.database.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.database.mapper.LockerMapper;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.database.repository.LockerRepository;
import org.szylica.database.repository.generic.AbstractRepository;
import org.szylica.model.locker.enums.LockerStatus;

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
    public List<Locker> getAllFreeLockersInSizeFromParcelMachine(LockerSize size, Long parcelMachineId) {

        return findAllWhere(Map.of(
                "size", size.name(),
                "parcel_machine_id", parcelMachineId.toString(),
                "status", "FREE"),
                "AND", "AND");
    }

    @Override
    public boolean updateLockerStatus(Long lockerId, LockerStatus lockerStatus) {
        var locker = findById(lockerId);
        if (locker.isPresent()) {
            return update(lockerId, locker.get()) > 0;
        }
        throw new IllegalStateException("Locker with id " + lockerId + " not found");
    }


    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.ofEntries(Map.entry("parcelMachineId", "parcel_machine_id"));
    }
}
