package ru.gazprombank.middle.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "backend")
public class MiddleProperties {
    private Endpoint register = new Endpoint();
    private Endpoint createAccount = new Endpoint();
    private Endpoint currentBalance = new Endpoint();
    private Endpoint transfer = new Endpoint();

    @Getter
    @Setter
    public static class Endpoint {
        private String url;
    }
}