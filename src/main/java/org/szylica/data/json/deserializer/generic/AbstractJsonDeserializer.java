package org.szylica.converter.json.deserializer.generic;

import lombok.RequiredArgsConstructor;
import org.szylica.converter.json.converter.JsonConverter;
import org.szylica.converter.json.deserializer.Deserializer;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class AbstractJsonDeserializer<T> implements Deserializer<T> {

    private final JsonConverter<T> jsonConverter;

    @SuppressWarnings("unchecked")
    private final Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0] ;

    @Override
    public T fromJson(String filename) {
        try(FileReader fileReader = new FileReader(filename)) {

            return jsonConverter.fromJson(fileReader, tClass);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
