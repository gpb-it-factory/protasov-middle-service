package ru.gazprombank.middle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gazprombank.middle.config.MiddleProperties;

@SpringBootApplication
@EnableConfigurationProperties(MiddleProperties.class)
public class MiddleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddleApplication.class, args);
    }

}