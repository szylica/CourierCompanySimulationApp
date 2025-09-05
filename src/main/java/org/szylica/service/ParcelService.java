package org.szylica.service;

import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.enums.LockerSize;

import java.util.Optional;

public interface ParcelService {

    Optional<ParcelMachine> findNearestParcelMachine(Long userId, LockerSize lockerSize, double distanceLimit);
    boolean assignLockerToParcel(Long parcelMachineId, LockerSize lockerSize);
}
