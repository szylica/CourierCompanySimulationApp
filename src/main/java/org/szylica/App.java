package org.szylica;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.szylica.config.AppBeansConfig;
import org.szylica.dto.ParcelMachineDto;
import org.szylica.dto.UserDto;
import org.szylica.files.converter.user.impl.JsonFileToUsersConverterImpl;
import org.szylica.model.Order;
import org.szylica.database.repository.impl.OrderRepositoryImpl;
import org.szylica.model.ParcelMachine;
import org.szylica.service.impl.CourierServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(Arrays.stream(Order.class.getDeclaredFields())
                .toList());


        var context = new AnnotationConfigApplicationContext(AppBeansConfig.class);
        var jdbi = context.getBean("jdbi", Jdbi.class);
        var orderRepository = context.getBean("orderRepositoryImpl", OrderRepositoryImpl.class);
        var courierService = context.getBean("courierServiceImpl", CourierServiceImpl.class);

        Order order = Order.builder()
                .userId(10L)
                .parcelId(5L)
                .deliveredAt(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .build();

        //System.out.println(orderRepository.insert(order));
//        orderRepository.update(1L, order);
//        orderRepository.findAllWhere(Map.of("parcelId", "2", "userId", "5"), "AND");

        var pm = new ParcelMachine(1L, "Paczkomat WAW001", 52.22977, 21.01178, "Warszawa, ul. Marszałkowska 1");
        var pmDto = new ParcelMachineDto(1L);
//
        var uDto = new UserDto(1L);
        var order1 = courierService.registerOrder(uDto);
        var parcel = courierService.registerParcel(order1, 9.0,9.0,9.0);
        courierService.setUpParcel(parcel, pmDto);

//        var jsonFileToUsersConverter = context.getBean("jsonFileToUsersConverterImpl", JsonFileToUsersConverterImpl.class);
//        System.out.println(jsonFileToUsersConverter.convert("users.json"));
    }
}
