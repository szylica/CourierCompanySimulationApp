package org.szylica.data.xml.deserializer;

public interface XmlDeserializer<T> {
    T fromXml(String xml);
}
