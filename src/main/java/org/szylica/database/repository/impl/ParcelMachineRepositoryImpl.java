package org.szylica.database.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.util.NearestPoint;
import org.szylica.database.mapper.ParcelMachineMapper;
import org.szylica.model.ParcelMachine;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.database.repository.generic.AbstractRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ParcelMachineRepositoryImpl extends AbstractRepository<ParcelMachine, Long> implements ParcelMachineRepository {

    protected ParcelMachineRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected void registerMappers() {
        jdbi.registerRowMapper(ParcelMachine.class, new ParcelMachineMapper());
    }

    @Override
    public List<ParcelMachine> getClosestParcelMachinesWithLockerAvailable(
            double targetLatitude,
            double targetLongitude,
            double maxDistance,
            LockerSize lockerSize) {

        var sql = """
                SELECT pm.*,
                       6371 * acos(
                               cos(radians(:targetLatitude)) * cos(radians(pm.latitude)) *
                               cos(radians(pm.longitude) - radians(:targetLongitude)) +
                               sin(radians(:targetLatitude)) * sin(radians(pm.latitude))
                              ) as distance
                FROM parcel_machines pm
                         JOIN lockers l ON pm.id = l.parcel_machine_id
                WHERE l.status = 'FREE'
                  AND l.size = :lockerSize
                GROUP BY pm.id
                HAVING distance <= :maxDistance
                ORDER BY distance;
                """;

        return jdbi.withHandle(handle ->
                    handle.createQuery(sql)
                            .bind("targetLatitude", targetLatitude)
                            .bind("targetLongitude", targetLongitude)
                            .bind("maxDistance", maxDistance)
                            .bind("lockerSize", lockerSize.name())
                            .map(new ParcelMachineMapper())
                            .list()
                );
    }

    @Override
    public Optional<ParcelMachine> getOneClosestParcelMachineWithLockerAvailable(double lat, double lng, double maxDistance, LockerSize lockerSize) {
        var parcelMachines = getClosestParcelMachinesWithLockerAvailable(lat, lng, maxDistance, lockerSize);
        if(parcelMachines.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(parcelMachines.getFirst());

    }


    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.of();
    }
}
