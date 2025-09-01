package org.szylica.data.json.deserializer;

public interface JsonDeserializer<T> {
    T fromJson(String filename);

}
