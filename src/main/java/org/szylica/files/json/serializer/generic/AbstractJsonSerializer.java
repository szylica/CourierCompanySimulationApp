package org.szylica.data.json.serializer.generic;

import lombok.RequiredArgsConstructor;
import org.szylica.data.json.converter.JsonConverter;
import org.szylica.data.json.serializer.JsonSerializer;

import java.io.FileWriter;
import java.io.IOException;

@RequiredArgsConstructor
public class AbstractJsonSerializer<T> implements JsonSerializer<T> {

    private final JsonConverter<T> jsonConverter;
    @Override
    public void toJson(T object, String filename) {
        try(FileWriter fileWriter = new FileWriter(filename)) {
            jsonConverter.toJson(object, fileWriter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
