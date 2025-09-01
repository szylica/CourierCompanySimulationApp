package org.szylica.data.converter.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.szylica.data.converter.user.FileToUsersConverter;
import org.szylica.data.model.UserData;
import org.szylica.data.model.UsersData;
import org.szylica.data.xml.deserializer.XmlFileDeserializer;
import org.szylica.model.User;
import org.szylica.data.validator.Validator;

import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class XmlFileToUsersConverterImpl implements FileToUsersConverter {

    private final XmlFileDeserializer<UsersData> userXmlDeserializer;
    private final Validator<UserData> validator;

    @Override
    public Iterable<User> convert(String filename) {
        return userXmlDeserializer.deserializeFromFile(Paths.get(filename))
                .getUsers()
                .stream()
                .filter(userData -> Validator.validate(userData, validator))
                .map(UserData::toUser)
                .toList();
    }
}
