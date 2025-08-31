package org.szylica.converter.json.serializer;

public interface JsonSerializer<T> {
    void toJson(T object, String filename);
}
