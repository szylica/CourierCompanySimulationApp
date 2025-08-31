package org.szylica.converter.json.deserializer;

public interface Deserializer<T> {
    T fromJson(String filename);

}
