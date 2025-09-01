package org.szylica.data.xml.deserializer.impl;

import org.springframework.stereotype.Component;
import org.szylica.data.model.UsersData;
import org.szylica.data.xml.converter.XmlConverter;
import org.szylica.data.xml.deserializer.generic.AbstractXmlDeserializer;

@Component
public class UsersDataXmlDeserializer extends AbstractXmlDeserializer<UsersData> {
    public UsersDataXmlDeserializer(XmlConverter<UsersData> converter) {
        super(converter);
    }
}
