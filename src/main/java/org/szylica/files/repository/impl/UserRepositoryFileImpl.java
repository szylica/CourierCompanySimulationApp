package org.szylica.files.repository.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.szylica.files.converter.Converter;
import org.szylica.files.repository.UserRepositoryFile;
import org.szylica.model.User;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryFileImpl implements UserRepositoryFile {

    @Value("${filename}")
    String filename;

    @Value("${format}")
    String format;

    private Converter<String, List<User>> converter;
    private Map<Long, User> users;

    private final ApplicationContext applicationContext;

    @PostConstruct
    void init() {
        converter = applicationContext.getBean("%sFileToUsersConverterImpl".formatted(format), Converter.class);
        loadUsers();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private void loadUsers() {
        users = converter
                .convert(filename)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

    }
}
