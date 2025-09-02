package org.szylica.database.repository;


import org.szylica.model.Order;
import org.szylica.database.repository.generic.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
