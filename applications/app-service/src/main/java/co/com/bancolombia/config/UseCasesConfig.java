package co.com.bancolombia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Bean;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import co.com.bancolombia.usecase.stats.StatsUseCase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import co.com.bancolombia.events.config.EventProperties;
import co.com.bancolombia.dynamodb.config.DynamoDbProperties;

@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
@Configuration
@EnableConfigurationProperties({ EventProperties.class, DynamoDbProperties.class })
public class UseCasesConfig {

    @Bean
    public StatsUseCase statsUseCase(StatsRepository repository, EventPublisher publisher) {
        return new StatsUseCase(repository, publisher);
    }
}