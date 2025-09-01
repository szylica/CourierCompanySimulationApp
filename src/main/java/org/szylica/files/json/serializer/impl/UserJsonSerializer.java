package org.szylica.data.json.serializer.impl;

import org.springframework.stereotype.Component;
import org.szylica.data.json.converter.JsonConverter;
import org.szylica.data.json.serializer.generic.AbstractJsonSerializer;
import org.szylica.model.User;

@Component
public class UserJsonSerializer extends AbstractJsonSerializer<User> {

    public UserJsonSerializer(JsonConverter<User> jsonConverter) {
        super(jsonConverter);
    }
}
