package ru.gazprombank.middle.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.repository.UserRepository;
import ru.gazprombank.middle.service.HttpRegistrationService;
import ru.gazprombank.middle.service.InMemoryRegistrationService;
import ru.gazprombank.middle.service.RegistrationService;

@Configuration
public class MiddleConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
    public RegistrationService inMemoryBackendClient(UserRepository userRepository) {
        return new InMemoryRegistrationService(userRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
    public RegistrationService httpBackendClient(WebClient webClient) {
        return new HttpRegistrationService(webClient);
    }
}
