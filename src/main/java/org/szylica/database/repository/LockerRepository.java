package org.szylica.database.repository;

import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.database.repository.db.generic.CrudRepository;

import java.util.List;

public interface LockerRepository extends CrudRepository<Locker, Long> {

    boolean isLockerFree(Long id);
    List<Locker> getAllLockersFromParcelMachine(Long parcelMachineId);
    List<Locker> isSizeLockerFree(LockerSize size, Long parcelMachineId);
}
