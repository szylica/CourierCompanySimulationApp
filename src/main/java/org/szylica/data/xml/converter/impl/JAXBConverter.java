package org.szylica.data.xml.converter.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.szylica.data.xml.converter.XmlConverter;

import java.io.FileReader;
import java.io.FileWriter;

@Component
public class JAXBConverter<T> implements XmlConverter<T> {

    @SneakyThrows
    @Override
    public void toXml(T data, FileWriter writer) {
        JAXBContext context = JAXBContext.newInstance(data.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(data, writer);
    }
    @SneakyThrows
    @Override
    public T fromXml(FileReader reader, Class<T> tClass) {
        JAXBContext context = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(reader);
    }
}
