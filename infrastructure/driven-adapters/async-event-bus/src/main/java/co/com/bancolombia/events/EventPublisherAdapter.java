package co.com.bancolombia.events;

import co.com.bancolombia.events.config.EventProperties;
import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final EventProperties properties;

    @Override
    public Mono<Void> publish(Stats stats) {
        return Mono.fromRunnable(() -> {
            try {
                String message = objectMapper.writeValueAsString(stats);
                rabbitTemplate.convertAndSend(
                        properties.getExchange(),
                        properties.getRoutingKey(),
                        message
                );
                log.info("Evento publicado: {}", message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error al serializar el evento", e);
            }
        });
    }
}