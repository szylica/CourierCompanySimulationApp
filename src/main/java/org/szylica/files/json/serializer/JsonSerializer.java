package org.szylica.files.json.serializer;

public interface JsonSerializer<T> {
    void toJson(T object, String filename);
}
