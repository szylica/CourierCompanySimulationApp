package org.szylica.service;

import org.szylica.dto.UserDto;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.enums.LockerSize;

import java.util.List;

public interface CourierService {

    List<ParcelMachine> findClosestParcelMachinesFromUser(UserDto userDto);
    void sendPackage(UserDto sendingUser, ParcelMachine parcelMachineToGo, LockerSize lockerSize);


}
