package org.szylica.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.szylica.dto.UserDto;
import org.szylica.model.Order;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.repository.OrderRepository;
import org.szylica.repository.ParcelMachineRepository;
import org.szylica.repository.UserRepository;
import org.szylica.service.CourierService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierServiceImpl implements CourierService {

    private final UserRepository userRepositoryImpl;
    private final ParcelMachineRepository parcelMachineRepositoryImpl;
    private final OrderRepository orderRepositoryImpl;




    @Override
    public List<ParcelMachine> findClosestParcelMachinesFromUser(UserDto userDto) {
        var user = userRepositoryImpl.findById(userDto.userId());

        return parcelMachineRepositoryImpl.getClosestParcelMachines(
                user.orElseThrow().getLatitude(),
                user.orElseThrow().getLongitude());
    }

    @Override
    public void sendPackage(UserDto sendingUser, ParcelMachine parcelMachineToGo, LockerSize lockerSize) {
        var parcelId = registerParcel(sendingUser);
        System.out.println(parcelId);

    }

    private Long registerParcel(UserDto sendingUser){

        var order = Order.builder()
                        .userId(sendingUser.userId())
                        .createdAt(LocalDateTime.now())
                                .build();

        return orderRepositoryImpl.insert(order).getId();
    }

    private void setUpParcel(){

    }




}
