package org.szylica.data.converter.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.szylica.data.converter.user.FileToUsersConverter;
import org.szylica.data.json.deserializer.JsonDeserializer;
import org.szylica.data.model.UserData;
import org.szylica.data.model.UsersData;
import org.szylica.model.User;
import org.szylica.data.validator.Validator;

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
