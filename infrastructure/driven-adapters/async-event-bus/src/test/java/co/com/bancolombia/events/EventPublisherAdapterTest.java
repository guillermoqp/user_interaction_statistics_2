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
        adapter = new EventPublisherAdapter(rabbitTemplate, objectMapper, prop);
    }

    @Test
    void shouldPublishEvent() {
        Stats stats = Stats.builder()
                .totalContactoClientes(250)
                .motivoReclamo(25)
                .motivoGarantia(10)
                .motivoDuda(100)
                .motivoCompra(100)
                .motivoFelicitaciones(7)
                .motivoCambio(8)
                .hash("02946f262f2eb0d8d5c8e76c50433ed8")
                .timestamp(System.currentTimeMillis())
                .build();
        doNothing().when(rabbitTemplate).convertAndSend(
                any(String.class),
                any(String.class),
                any(String.class)
        );
        adapter.publish(stats);
        verify(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(String.class));
    }

    @Test
    void shouldReturnErrorWhenSerializationFails() {
        Stats stats = mock(Stats.class);
        ObjectMapper brokenMapper = mock(ObjectMapper.class);
        try {
            when(brokenMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("fail") {});
        } catch (JsonProcessingException ignored) {}
        EventPublisherAdapter brokenAdapter = new EventPublisherAdapter(rabbitTemplate, brokenMapper, "amq.direct", "event.stats.validated");
        StepVerifier.create(brokenAdapter.publish(stats))
                .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().contains("Error serializando evento"))
                .verify();

        verify(rabbitTemplate, never()).convertAndSend(any(), any(), any());
    }
}