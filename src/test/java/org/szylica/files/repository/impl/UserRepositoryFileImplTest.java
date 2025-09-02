package org.szylica.files.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.szylica.config.AppTestBeansConfig;
import org.szylica.files.converter.Converter;
import org.szylica.files.converter.user.FileToUsersConverter;
import org.szylica.files.converter.user.Users;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.szylica.files.converter.user.Users.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserRepositoryFileImplTest {

    @Mock
    private FileToUsersConverter fileToUsersConverter;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private UserRepositoryFileImpl userRepositoryFileImpl;

    @BeforeEach
    void setUp() {
        var users = List.of(ADAM_USER.toUser(), EWA_USER.toUser());
        var format = "json";
        var filename = "users.json";

        when(applicationContext.getBean("%sFileToUsersConverterImpl".formatted(format), Converter.class))
                .thenReturn(fileToUsersConverter);

        when(fileToUsersConverter.convert(ArgumentMatchers.anyString()))
                .thenReturn(users);

        userRepositoryFileImpl.filename = filename;
        userRepositoryFileImpl.format = format;
        userRepositoryFileImpl.init();
    }

    @Test
    @DisplayName("test")
    void test1(){
        Assertions.assertThat(userRepositoryFileImpl.findById(1L))
                .contains(ADAM_USER.toUser());
    }

}
