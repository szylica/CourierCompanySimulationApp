package org.szylica.files.json.converter.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.szylica.files.json.converter.JsonConverter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GsonConverter<T> implements JsonConverter<T> {

    private final Gson gson;

    @Override
    public void toJson(T object, FileWriter fileWriter) throws IOException {
        gson.toJson(object, fileWriter);
    }

    @Override
    public T fromJson(FileReader fileReader, Class<T> tClass) throws IOException {
        return gson.fromJson(fileReader, tClass);
    }
}
