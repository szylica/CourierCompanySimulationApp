package org.szylica.database.repository;

import org.szylica.model.ParcelMachine;
import org.szylica.database.repository.generic.CrudRepository;
import org.szylica.model.locker.enums.LockerSize;

import java.util.List;
import java.util.Optional;

public interface ParcelMachineRepository extends CrudRepository<ParcelMachine, Long> {
    List<ParcelMachine> getClosestParcelMachinesWithLockerAvailable(double lat, double lng, double maxDistance, LockerSize lockerSize);
    Optional<ParcelMachine> getOneClosestParcelMachineWithLockerAvailable(double lat, double lng, double maxDistance, LockerSize lockerSize);


}
