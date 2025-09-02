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
import org.szylica.files.model.UserData;
import org.szylica.files.model.UsersData;
import org.szylica.files.validator.Validator;
import org.szylica.files.xml.deserializer.XmlFileDeserializer;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.szylica.files.converter.user.Users.ADAM_USER;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class XmlFileToUsersConverterImplTest {
    @Autowired
    private Validator<UserData> validator;

    @Mock
    private XmlFileDeserializer<UsersData> xmlFileDeserializer;

    private FileToUsersConverter fileToUsersConverter;

    @BeforeEach
    void setUp() {
        fileToUsersConverter = new XmlFileToUsersConverterImpl(xmlFileDeserializer, validator);
    }

    @Test
    @DisplayName("When all data is correct")
    void test1(){
        when(xmlFileDeserializer.deserializeFromFile(ArgumentMatchers.any(Path.class)))
                .thenReturn(new UsersData(List.of(ADAM_USER)));

        Assertions.assertThat(fileToUsersConverter.convert("users.xml")).hasSize(1);
    }

    @Test
    @DisplayName("When not all data is correct")
    void test2(){
        when(xmlFileDeserializer.deserializeFromFile(ArgumentMatchers.any(Path.class)))
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

        Assertions.assertThat(fileToUsersConverter.convert("users.xml")).hasSize(1);
    }
}
