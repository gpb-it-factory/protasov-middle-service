package ru.gazprombank.middle.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gazprombank.middle.client.CreateAccountClient;
import ru.gazprombank.middle.client.HttpCreateAccountClientImpl;
import ru.gazprombank.middle.client.InMemoryCreateAccountClientImpl;
import ru.gazprombank.middle.repository.AccountRepository;
import ru.gazprombank.middle.repository.UserRepository;

@Configuration
public class CreateAccountClientConfig {
    @Bean
    @Primary
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "inMemory")
    public CreateAccountClient inMemoryCreateAccountClient(UserRepository userRepository,
                                                           AccountRepository accountRepository) {
        return new InMemoryCreateAccountClientImpl(userRepository, accountRepository);
    }

    @Bean
    @ConditionalOnProperty(name = "backend.client-type", havingValue = "http")
    public CreateAccountClient httpCreateAccountClient(WebClient webClient) {
        return new HttpCreateAccountClientImpl(webClient);
    }
}
