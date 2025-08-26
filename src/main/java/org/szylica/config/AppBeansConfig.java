package org.szylica.config;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("org.szylica")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class AppBeansConfig {

    private final Environment environment;


    @Bean
    public Jdbi jdbi() {

        return Jdbi.create(
                environment.getRequiredProperty("db.url"),
                environment.getRequiredProperty("db.username"),
                environment.getRequiredProperty("db.password")
        );

    }
}