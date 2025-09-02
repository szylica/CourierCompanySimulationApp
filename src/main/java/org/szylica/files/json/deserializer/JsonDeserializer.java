package org.szylica.files.json.deserializer;

public interface JsonDeserializer<T> {
    T fromJson(String filename);

}
