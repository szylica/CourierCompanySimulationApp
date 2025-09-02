package org.szylica.files.converter.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.szylica.files.converter.user.FileToUsersConverter;
import org.szylica.files.model.UserData;
import org.szylica.files.model.UsersData;
import org.szylica.files.xml.deserializer.XmlFileDeserializer;
import org.szylica.model.User;
import org.szylica.files.validator.Validator;

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
