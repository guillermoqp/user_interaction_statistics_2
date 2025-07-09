package co.com.bancolombia.usecase.stats;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.model.stats.gateways.EventPublisher;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static org.mockito.Mockito.*;

public class StatsUseCaseTest {

    private StatsRepository statsRepository;
    private EventPublisher eventPublisher;
    private StatsUseCase statsUseCase;

    @BeforeEach
    void setUp() {
        statsRepository = mock(StatsRepository.class);
        eventPublisher = mock(EventPublisher.class);
        statsUseCase = new StatsUseCase(statsRepository, eventPublisher);
    }

    @Test
    void shouldProcessValidStatsSuccessfully() {
        Stats input = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash(generateMD5("1,2,3,4,5,6,7"))
                .build();
        when(statsRepository.saveStats(any())).thenReturn(Mono.empty());
        when(eventPublisher.publish(any())).thenReturn(Mono.empty());

        StepVerifier.create(statsUseCase.processStats(input)).expectNextMatches(result -> result.getTimestamp() > 0).verifyComplete();

        verify(statsRepository, times(1)).saveStats(any());
        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    void shouldFailWhenHashIsInvalid() {
        Stats input = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash("incorrectHash")
                .build();
        StepVerifier.create(statsUseCase.processStats(input))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Hash invalido")
                )
                .verify();
        verifyNoInteractions(statsRepository);
        verifyNoInteractions(eventPublisher);
    }

    // Método auxiliar para generar el hash MD5 correcto
    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}