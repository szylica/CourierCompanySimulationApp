package org.szylica.database.repository;

import org.szylica.model.User;
import org.szylica.database.repository.generic.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
