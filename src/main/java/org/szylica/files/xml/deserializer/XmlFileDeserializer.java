package org.szylica.files.xml.deserializer;

import java.nio.file.Files;
import java.nio.file.Path;

public interface XmlFileDeserializer<T> extends XmlDeserializer<T>{
    default T deserializeFromFile(Path path) {
        try {
            var xml = Files.readString(path);
            return deserialize(xml);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
