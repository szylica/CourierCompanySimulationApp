package org.szylica.data.json.converter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public interface JsonConverter<T>{
    void toJson(T object, FileWriter fileWriter) throws IOException;
    T fromJson(FileReader fileReader, Class<T> tClass) throws IOException;


}
