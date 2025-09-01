package org.szylica.data.xml.deserializer;

public interface XmlDeserializer<T> {
    T deserialize(String xml);
}
