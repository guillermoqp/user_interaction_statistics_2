package co.com.bancolombia.config;

import co.com.bancolombia.events.EventPublisherAdapter;
import co.com.bancolombia.events.config.EventProperties;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfig {

    @Bean
    public EventPublisher eventPublisher(RabbitTemplate template, ObjectMapper mapper, EventProperties eventProperties) {
        return new EventPublisherAdapter(template, mapper, eventProperties);
    }
}