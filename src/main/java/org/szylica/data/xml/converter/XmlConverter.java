package org.szylica.data.xml.converter;

import java.io.FileReader;
import java.io.FileWriter;

public interface XmlConverter<T> {
    void toXml(T data, FileWriter writer);
    T fromXml(FileReader reader, Class<T> tClass);
}
