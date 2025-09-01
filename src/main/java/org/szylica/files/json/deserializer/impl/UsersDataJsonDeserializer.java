package org.szylica.data.json.deserializer.impl;

import org.springframework.stereotype.Component;
import org.szylica.data.json.converter.JsonConverter;
import org.szylica.data.json.deserializer.generic.AbstractJsonDeserializer;
import org.szylica.data.model.UsersData;

@Component
public class UsersDataJsonDeserializer extends AbstractJsonDeserializer<UsersData> {

    public UsersDataJsonDeserializer(JsonConverter<UsersData> jsonConverter) {
        super(jsonConverter);
    }
}
