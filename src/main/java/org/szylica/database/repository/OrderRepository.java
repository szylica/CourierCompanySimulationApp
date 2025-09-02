package org.szylica.database.repository;


import org.szylica.model.Order;
import org.szylica.database.repository.db.generic.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
