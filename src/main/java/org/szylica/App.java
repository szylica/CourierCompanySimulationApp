package org.szylica;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.szylica.config.AppBeansConfig;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.database.repository.impl.LockerRepositoryImpl;
import org.szylica.database.repository.impl.ParcelMachineRepositoryImpl;
import org.szylica.database.repository.impl.ParcelRepositoryImpl;
import org.szylica.dto.ParcelMachineDto;
import org.szylica.dto.UserDto;
import org.szylica.files.converter.user.impl.JsonFileToUsersConverterImpl;
import org.szylica.model.Order;
import org.szylica.database.repository.impl.OrderRepositoryImpl;
import org.szylica.model.ParcelMachine;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;
import org.szylica.model.parcel.Parcel;
import org.szylica.model.parcel.enums.ParcelStatus;
import org.szylica.service.impl.CourierServiceImpl;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {


        var context = new AnnotationConfigApplicationContext(AppBeansConfig.class);
        var jdbi = context.getBean("jdbi", Jdbi.class);
        var orderRepository = context.getBean("orderRepositoryImpl", OrderRepositoryImpl.class);
        var parcelMachineRepository = context.getBean("parcelMachineRepositoryImpl", ParcelMachineRepositoryImpl.class);
        var lockerRepository = context.getBean("lockerRepositoryImpl", LockerRepositoryImpl.class);
        var parcelRepository = context.getBean("parcelRepositoryImpl", ParcelRepositoryImpl.class);
        var courierService = context.getBean("courierServiceImpl", CourierServiceImpl.class);

        parcelMachineRepository.insert(ParcelMachine.builder()
                .name("Paczkomat WAW001")
                .address("pies")
                .latitude(52.22977)
                .longitude(21.01178)
                .build());

        lockerRepository.insert(Locker.builder()
                .parcelMachineId(1L)
                .size(LockerSize.MEDIUM)
                .status(LockerStatus.FREE)
                .build());

        parcelRepository.insert(Parcel.builder()
                .status(ParcelStatus.PENDING)
                .width(120)
                .depth(120)
                .height(120)
                .lockerId(1L)
                .build());

        orderRepository.insert(Order.builder()
                        .userId(1L)
                        .parcelId(1L)
                        .createdAt(ZonedDateTime.now())
                        .deliveredAt(ZonedDateTime.now().plusDays(2))
                .build());

        System.out.println("----------- [PARCEL MACHINES] -----------");
        System.out.println(parcelMachineRepository.findAll());
        System.out.println("----------- [LOCKERS] -----------");
        System.out.println(lockerRepository.findAll());
        System.out.println("----------- [PARCELS] -----------");
        System.out.println(parcelRepository.findAll());
        System.out.println("----------- [ORDERS] -----------");
        System.out.println(orderRepository.findAll());

//        var pm = new ParcelMachine(1L, "Paczkomat WAW001", 52.22977, 21.01178, "Warszawa, ul. Marszałkowska 1");
//        var pmDto = new ParcelMachineDto(1L);
//
////
//        var uDto = new UserDto(1L);
//        var order1 = courierService.registerOrder(uDto);
//        var parcel = courierService.registerParcel(order1, 90, 90, 90);
//        courierService.setUpParcel(parcel, pmDto);

//        var jsonFileToUsersConverter = context.getBean("jsonFileToUsersConverterImpl", JsonFileToUsersConverterImpl.class);
//        System.out.println(jsonFileToUsersConverter.convert("users.json"));
    }
}
