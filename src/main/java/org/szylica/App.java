package org.szylica;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.szylica.config.AppBeansConfig;
import org.szylica.model.Order;
import org.szylica.repository.OrderRepository;
import org.szylica.repository.generic.AbstractRepository;
import org.szylica.repository.impl.OrderRepositoryImpl;

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

        Order order = Order.builder()
                .userId(10L)
                .parcelId(5L)
                .deliveredAt(LocalDateTime.now().plusDays(1))
                .createdAt(LocalDateTime.now())
                .build();

        //System.out.println(orderRepository.insert(order));
        orderRepository.update(1L, order);
    }
}
