package org.szylica.data.json.serializer;

public interface JsonSerializer<T> {
    void toJson(T object, String filename);
}
