package co.com.bancolombia.events;

import co.com.bancolombia.model.stats.Stats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import co.com.bancolombia.events.config.EventProperties;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventPublisherAdapterTest {

    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;
    private EventPublisherAdapter adapter;
    private EventProperties prop;


    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        objectMapper = new ObjectMapper();
        prop = mock(EventProperties.class);
        when(prop.getExchange()).thenReturn("amq.direct");
        when(prop.getRoutingKey()).thenReturn("event.stats.validated");
        adapter = new EventPublisherAdapter(rabbitTemplate, objectMapper, prop);
    }

    @Test
    void shouldPublishToRabbitSuccessfully() {
        Stats stats = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash("valido")
                .timestamp(System.currentTimeMillis())
                .build();
        adapter.publish(stats).block();
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("amq.direct"),
                eq("event.stats.validated"),
                any(String.class)
        );
    }

    @Test
    void shouldReturnErrorWhenSerializationFails() {
        Stats stats = mock(Stats.class);
        ObjectMapper brokenMapper = mock(ObjectMapper.class);
        try {
            when(brokenMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {});
        } catch (JsonProcessingException ignored) {}
        EventPublisherAdapter brokenAdapter = new EventPublisherAdapter(rabbitTemplate, brokenMapper, prop);
        StepVerifier.create(brokenAdapter.publish(stats))
                .expectErrorMatches(e -> e instanceof RuntimeException)
                .verify();

        verify(rabbitTemplate, never()).convertAndSend(
                any(String.class),
                any(String.class),
                any(Object.class)
        );
    }
}