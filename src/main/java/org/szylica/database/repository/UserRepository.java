package org.szylica.database.repository;

import org.szylica.model.User;
import org.szylica.database.repository.db.generic.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
