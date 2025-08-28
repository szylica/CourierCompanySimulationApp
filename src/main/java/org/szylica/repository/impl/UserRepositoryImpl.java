package org.szylica.repository.impl;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import org.szylica.model.User;
import org.szylica.repository.UserRepository;
import org.szylica.repository.generic.AbstractRepository;

import java.util.Map;

@Repository
public class UserRepositoryImpl extends AbstractRepository<User, Long> implements UserRepository {
    protected UserRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    protected void registerMappers() {
        jdbi.registerRowMapper()
    }

    @Override
    protected Map<String, String> getFieldColumnMap() {
        return Map.of();
    }
}
