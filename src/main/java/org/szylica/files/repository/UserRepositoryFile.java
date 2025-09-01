package org.szylica.files.repository;

import org.szylica.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();
    List<User> loadUsers();
}
