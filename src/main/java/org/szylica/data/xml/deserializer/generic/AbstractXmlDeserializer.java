package org.szylica.data.xml.deserializer.generic;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.szylica.data.xml.converter.XmlConverter;
import org.szylica.data.xml.deserializer.XmlDeserializer;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class AbstractXmlDeserializer<T> implements XmlDeserializer<T> {

    private final XmlConverter<T> converter;

    private final Class<T> clazz =
            (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SneakyThrows
    public T fromXml(String filename) {
        try(var reader = new FileReader(filename)) {
            return converter.fromXml(reader, clazz);
        }
    }
}
