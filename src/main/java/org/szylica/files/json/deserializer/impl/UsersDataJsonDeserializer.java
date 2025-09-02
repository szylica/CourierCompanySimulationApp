package org.szylica.files.json.deserializer.impl;

import org.springframework.stereotype.Component;
import org.szylica.files.json.converter.JsonConverter;
import org.szylica.files.json.deserializer.generic.AbstractJsonDeserializer;
import org.szylica.files.model.UsersData;

@Component
public class UsersDataJsonDeserializer extends AbstractJsonDeserializer<UsersData> {

    public UsersDataJsonDeserializer(JsonConverter<UsersData> jsonConverter) {
        super(jsonConverter);
    }
}
