package org.szylica.files.repository;

import org.szylica.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryFile {
    Optional<User> findById(Long id);
}
