package org.szylica.files.json.serializer.impl;

import org.springframework.stereotype.Component;
import org.szylica.files.json.converter.JsonConverter;
import org.szylica.files.json.serializer.generic.AbstractJsonSerializer;
import org.szylica.model.User;

@Component
public class UserJsonSerializer extends AbstractJsonSerializer<User> {

    public UserJsonSerializer(JsonConverter<User> jsonConverter) {
        super(jsonConverter);
    }
}
