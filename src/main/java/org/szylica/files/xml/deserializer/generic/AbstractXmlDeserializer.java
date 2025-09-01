package org.szylica.data.xml.deserializer.generic;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.szylica.data.xml.converter.XmlConverter;
import org.szylica.data.xml.deserializer.XmlDeserializer;
import org.szylica.data.xml.deserializer.XmlFileDeserializer;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class AbstractXmlDeserializer<T> implements XmlFileDeserializer<T> {

    private final XmlConverter<T> converter;

    private final Class<T> clazz =
            (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SneakyThrows
    public T deserialize(String filename) {
        try(var reader = new FileReader(filename)) {
            return converter.fromXml(reader, clazz);
        }
    }
}
