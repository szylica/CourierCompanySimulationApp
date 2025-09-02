package org.szylica.repository;

import org.szylica.model.User;
import org.szylica.repository.db.generic.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
