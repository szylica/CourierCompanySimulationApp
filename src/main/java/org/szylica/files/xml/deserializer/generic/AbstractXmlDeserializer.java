package org.szylica.files.xml.deserializer.generic;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.szylica.files.xml.converter.XmlConverter;
import org.szylica.files.xml.deserializer.XmlFileDeserializer;

import java.io.FileReader;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class AbstractXmlDeserializer<T> implements XmlFileDeserializer<T> {


    private final Class<T> clazz =
            (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @SneakyThrows
    public T deserialize(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(filename);
            return clazz.cast(unmarshaller.unmarshal(reader));
        }catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
