package org.szylica.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.szylica.database.repository.LockerRepository;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.files.repository.UserRepositoryFile;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.szylica.service.ParcelService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelMachineRepository parcelMachineRepository;
    private final LockerRepository lockerRepository;
    private final UserRepositoryFile userRepositoryFile;

    @Override
    public Optional<ParcelMachine> findNearestParcelMachine(Long userId, LockerSize lockerSize, double distanceLimit) {
        var user = userRepositoryFile
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                user.getLatitude(),
                user.getLongitude(),
                distanceLimit,
                lockerSize);
    }

    @Override
    public Long assignLockerToParcel(Long parcelMachineId, LockerSize lockerSize) {
        var freeLockers = lockerRepository.getAllFreeLockersInSizeFromParcelMachine(lockerSize, parcelMachineId);
        if(freeLockers.isEmpty()){
            throw new IllegalStateException("No free lockers in parcel machine with this size");
        }

        var lockerId = freeLockers.getFirst().getId();

        var updated = lockerRepository.updateLockerStatus(lockerId, LockerStatus.OCCUPIED);
        if(!updated){
            throw new IllegalStateException("Failed to update locker status");
        }
        return lockerId;

    }
}
