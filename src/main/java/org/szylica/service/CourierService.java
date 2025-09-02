package org.szylica.service;

import org.szylica.dto.UserDto;
import org.szylica.model.Order;
import org.szylica.model.ParcelMachine;
import org.szylica.model.parcel.Parcel;

import java.util.List;

public interface CourierService {

    List<ParcelMachine> findClosestParcelMachinesFromUser(UserDto userDto);
    Order registerOrder(UserDto sendingUser);
    Parcel registerParcel(Order order, Double width, Double height, Double depth);


}
