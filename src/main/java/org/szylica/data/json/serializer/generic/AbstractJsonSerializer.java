package org.szylica.converter.json.serializer.generic;

import lombok.RequiredArgsConstructor;
import org.szylica.converter.json.converter.JsonConverter;
import org.szylica.converter.json.serializer.JsonSerializer;

@RequiredArgsConstructor
public class AbstractJsonSerializer<T> implements JsonSerializer<T> {

    private final JsonConverter<T> jsonConverter;
    @Override
    public void toJson(T object, String filename) {
        filename
    }
}
