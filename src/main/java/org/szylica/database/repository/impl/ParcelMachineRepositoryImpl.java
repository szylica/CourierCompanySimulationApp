package org.szylica.database.repository.impl;

import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.files.NearestPoint;
import org.szylica.database.mapper.ParcelMachineMapper;
import org.szylica.model.ParcelMachine;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.database.repository.generic.AbstractRepository;

import java.util.List;
import java.util.Map;
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
    public List<ParcelMachine> getClosestParcelMachines(Double lat, Double lng) {
        return this.findAll().stream()
                .collect(
                        Collectors.groupingBy(pm -> NearestPoint.haversine(
                                lat,
                                lng,
                                pm.getLatitude(),
                                pm.getLongitude())
                ))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByKey())
                .orElseThrow(() -> new IllegalStateException("No parcel machine found"))
                .getValue();
    }


    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.of();
    }
}
