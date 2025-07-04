package co.com.bancolombia.config;

import co.com.bancolombia.dynamodb.DynamoAdapter;
import co.com.bancolombia.dynamodb.repository.StatsDynamoRepository;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoConfig {

    @Bean
    public StatsRepository statsRepository(StatsDynamoRepository repository) {
        return new DynamoAdapter(repository);
    }
}