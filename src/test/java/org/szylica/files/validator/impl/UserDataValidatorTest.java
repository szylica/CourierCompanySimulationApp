package org.szylica.files.validator.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.szylica.config.AppTestBeansConfig;
import org.szylica.files.model.UserData;
import org.szylica.files.validator.Validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.szylica.files.converter.user.Users.ADAM_USER;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class UserDataValidatorTest {
    @Autowired
    private Validator<UserData> validator;

    static Stream<Arguments> validationData() {
        return Stream.of(
                Arguments.of(ADAM_USER, Map.of()),
                Arguments.of(new UserData(
                        3L,
                        "Iza",
                        "Kowalska",
                        "aaa",
                        "+48132312132",
                        111.1,
                        222.2
                ), Map.of("email", "Email is not valid"))
        );
    }

    @ParameterizedTest
    @MethodSource("validationData")
    @DisplayName("when validation works")
    void test1(UserData userData, Map<String, String> expectedErrors) {
        Assertions.assertThat(validator.validate(userData)).isEqualTo(expectedErrors);
    }
}
