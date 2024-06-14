package ru.gazprombank.middle.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.client.BackendClient;
import ru.gazprombank.middle.client.HttpBackendClientImpl;
import ru.gazprombank.middle.client.InMemoryBackendClientImpl;

@Configuration
public class BackendClientConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
    public BackendClient inMemoryBackendClient() {
        return new InMemoryBackendClientImpl();
    }

    @Bean
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
    public BackendClient httpBackendClient(WebClient webClient) {
        return new HttpBackendClientImpl(webClient);
    }
}
