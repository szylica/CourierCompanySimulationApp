package org.szylica.repository;

import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.repository.generic.CrudRepository;

import java.util.List;

public interface ParcelMachineRepository extends CrudRepository<ParcelMachine, Long> {
    List<ParcelMachine> getClosestParcelMachines(Double lat, Double lng);

}
