package org.szylica.converter.json.deserializer.impl;

import org.szylica.converter.json.converter.JsonConverter;
import org.szylica.converter.json.deserializer.generic.AbstractJsonDeserializer;
import org.szylica.model.User;

public class UserJsonDeserializer extends AbstractJsonDeserializer<User> {

    public UserJsonDeserializer(JsonConverter<User> jsonConverter) {
        super(jsonConverter);
    }
}
