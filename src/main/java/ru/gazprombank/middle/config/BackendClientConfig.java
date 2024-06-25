package ru.gazprombank.middle.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.client.UserRegistrationClient;
import ru.gazprombank.middle.client.HttpUserRegistrationClientImpl;
import ru.gazprombank.middle.client.InMemoryUserRegistrationClientImpl;
import ru.gazprombank.middle.repository.UserRepository;

@Configuration
public class BackendClientConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
    public UserRegistrationClient inMemoryBackendClient(UserRepository userRepository) {
        return new InMemoryUserRegistrationClientImpl(userRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
    public UserRegistrationClient httpBackendClient(WebClient webClient) {
        return new HttpUserRegistrationClientImpl(webClient);
    }
}
