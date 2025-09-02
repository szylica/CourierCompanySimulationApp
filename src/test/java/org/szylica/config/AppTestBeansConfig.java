package org.szylica.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.szylica.files.json.converter.JsonConverter;
import org.szylica.files.json.converter.impl.GsonConverter;
import org.szylica.files.json.deserializer.JsonDeserializer;
import org.szylica.files.json.deserializer.impl.UsersDataJsonDeserializer;
import org.szylica.files.model.UserData;
import org.szylica.files.model.UsersData;
import org.szylica.files.validator.Validator;
import org.szylica.files.validator.impl.UserDataValidator;
import org.szylica.files.xml.deserializer.XmlFileDeserializer;
import org.szylica.files.xml.deserializer.impl.UsersDataXmlDeserializer;

public class AppTestBeansConfig {
    @Bean
    public Gson gson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public JsonConverter<UsersData> jsonConverter(Gson gson){
        return new GsonConverter<>(gson);
    }

    @Bean
    public JsonDeserializer<UsersData> jsonDeserializer(JsonConverter<UsersData> jsonConverter){
        return new UsersDataJsonDeserializer(jsonConverter);
    }

    @Bean
    public Validator<UserData> validator(){
        return new UserDataValidator();
    }

    @Bean
    public XmlFileDeserializer<UsersData> xmlFileDeserializer() {
        return new UsersDataXmlDeserializer();
    }
}
