package org.szylica.files.json.serializer.generic;

import lombok.RequiredArgsConstructor;
import org.szylica.files.json.converter.JsonConverter;
import org.szylica.files.json.serializer.JsonSerializer;

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
