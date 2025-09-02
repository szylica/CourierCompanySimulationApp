package org.szylica.database.repository;

import org.szylica.model.ParcelMachine;
import org.szylica.database.repository.db.generic.CrudRepository;

import java.util.List;

public interface ParcelMachineRepository extends CrudRepository<ParcelMachine, Long> {
    List<ParcelMachine> getClosestParcelMachines(Double lat, Double lng);

}
