package org.szylica.files.json.deserializer.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.szylica.config.AppTestBeansConfig;
import org.szylica.files.json.deserializer.JsonDeserializer;
import org.szylica.files.model.UsersData;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class UsersDataJsonDeserializerTest {

    @Autowired
    private JsonDeserializer<UsersData> jsonDeserializer;

    @Test
    @DisplayName("when data is loaded correctly")
    void test1(){
        var path = Paths.get("src", "test", "resources", "users-test.json")
                .toFile()
                .getAbsolutePath();
        var users = jsonDeserializer.fromJson(path).getUsers();
        assertThat(users).hasSize(5);
    }

}
