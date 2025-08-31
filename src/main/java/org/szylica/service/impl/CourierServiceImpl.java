package org.szylica.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.szylica.dto.ParcelMachineDto;
import org.szylica.dto.UserDto;
import org.szylica.model.Order;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.szylica.model.parcel.Parcel;
import org.szylica.model.parcel.enums.ParcelStatus;
import org.szylica.repository.OrderRepository;
import org.szylica.repository.ParcelMachineRepository;
import org.szylica.repository.ParcelRepository;
import org.szylica.repository.UserRepository;
import org.szylica.repository.impl.LockerRepositoryImpl;
import org.szylica.service.CourierService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierServiceImpl implements CourierService {

    private final UserRepository userRepositoryImpl;
    private final ParcelMachineRepository parcelMachineRepositoryImpl;
    private final OrderRepository orderRepositoryImpl;
    private final ParcelRepository parcelRepositoryImpl;
    private final LockerRepositoryImpl lockerRepositoryImpl;


    @Override
    public List<ParcelMachine> findClosestParcelMachinesFromUser(UserDto userDto) {
        var user = userRepositoryImpl.findById(userDto.userId());

        return parcelMachineRepositoryImpl.getClosestParcelMachines(
                user.orElseThrow().getLatitude(),
                user.orElseThrow().getLongitude());
    }

    @Override
    public Order registerOrder(UserDto sendingUser) {
        var order = Order.builder()
                .userId(sendingUser.userId())
                .createdAt(LocalDateTime.now())
                .build();

        return orderRepositoryImpl.insert(order);
    }

    @Override
    public Parcel registerParcel(Order order, Double width, Double height, Double depth) {

        var parcel = Parcel.builder()
                .status(ParcelStatus.PENDING)
                .depth(depth)
                .width(width)
                .height(height)
                .build();

        var parcelDb = parcelRepositoryImpl.insert(parcel);
        System.out.println(parcelDb);
        orderRepositoryImpl.update(order.getId(), order.withParcelId(parcelDb.getId()));
        return parcelDb;
    }

    public void setUpParcel(Parcel parcel, ParcelMachineDto parcelMachineDto) {
        var lockersInPm = lockerRepositoryImpl.getAllLockersFromParcelMachine(parcelMachineDto.parcelMachineId());
        var lockerSizeNeeded = Parcel.calculateLockerSize(parcel);

        var finalLocker = lockersInPm
                .stream()
                .filter(locker -> locker.isLockerInSize(lockerSizeNeeded))
                .filter(Locker::isLockerFree)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Locker not found"));

        lockerRepositoryImpl.update(finalLocker.getId(), finalLocker.withStatus(LockerStatus.OCCUPIED));
        parcelRepositoryImpl.update(parcel.getId(), parcel.withLocker(finalLocker.getId()));

    }





}
