package org.szylica.files.converter.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.szylica.files.converter.user.FileToUsersConverter;
import org.szylica.files.json.deserializer.JsonDeserializer;
import org.szylica.files.model.UserData;
import org.szylica.files.model.UsersData;
import org.szylica.model.User;
import org.szylica.files.validator.Validator;

@Component
@RequiredArgsConstructor
public class JsonFileToUsersConverterImpl implements FileToUsersConverter {

    private final JsonDeserializer<UsersData> userJsonDeserializer;
    private final Validator<UserData> validator;

    @Override
    public Iterable<User> convert(String filename) {
        return userJsonDeserializer.fromJson(filename)
                .getUsers()
                .stream()
                .filter(userData -> Validator.validate(userData, validator))
                .map(UserData::toUser)
                .toList();
    }
}
