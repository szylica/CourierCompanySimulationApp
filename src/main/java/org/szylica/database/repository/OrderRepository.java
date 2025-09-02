package org.szylica.repository;


import org.szylica.model.Order;
import org.szylica.repository.db.generic.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
