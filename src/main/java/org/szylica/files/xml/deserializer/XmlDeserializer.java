package org.szylica.files.xml.deserializer;

public interface XmlDeserializer<T> {
    T deserialize(String xml);
}
