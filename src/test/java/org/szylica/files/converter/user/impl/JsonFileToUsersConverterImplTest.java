package org.szylica.files.converter.user.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.szylica.config.AppTestBeansConfig;
import org.szylica.files.converter.user.FileToUsersConverter;
import org.szylica.files.json.deserializer.JsonDeserializer;
import org.szylica.files.model.UserData;
import org.szylica.files.model.UsersData;
import org.szylica.files.validator.Validator;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.szylica.files.converter.user.Users.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes =AppTestBeansConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class JsonFileToUsersConverterImplTest {

    @Autowired
    private Validator<UserData> validator;

    @Mock
    private JsonDeserializer<UsersData> usersJsonDeserializer;

    private FileToUsersConverter fileToUsersConverter;

    @BeforeEach
    void setUp() {
        fileToUsersConverter = new JsonFileToUsersConverterImpl(usersJsonDeserializer, validator);
    }

    @Test
    @DisplayName("when all data is correct")
    void test1(){
        when(usersJsonDeserializer.fromJson(ArgumentMatchers.anyString()))
                .thenReturn(new UsersData(List.of(ADAM_USER)));

        Assertions.assertThat(fileToUsersConverter.convert("users.json")).hasSize(1);
    }

    @Test
    @DisplayName("when not all data is correct")
    void test2(){
        when(usersJsonDeserializer.fromJson(ArgumentMatchers.anyString()))
                .thenReturn(new UsersData(List.of(
                        new UserData(
                                3L,
                                "Iza",
                                "Kowalska",
                                "aaa",
                                "+48132312132",
                                111.1,
                                222.2
                        ),
                        ADAM_USER)));

        Assertions.assertThat(fileToUsersConverter.convert("users.json")).hasSize(1);
    }

}
