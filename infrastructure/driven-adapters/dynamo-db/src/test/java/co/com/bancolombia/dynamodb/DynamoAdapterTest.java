package co.com.bancolombia.dynamodb;

import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.dynamodb.repository.StatsDynamoRepository;
import co.com.bancolombia.dynamodb.DynamoAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

class DynamoAdapterTest {

    private StatsDynamoRepository repository;
    private DynamoAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(StatsDynamoRepository.class);
        adapter = new DynamoAdapter(repository);
    }

    @Test
    void shouldSaveStatsSuccessfully() {
        Stats stats = Stats.builder()
                .totalContactoClientes(1)
                .motivoReclamo(2)
                .motivoGarantia(3)
                .motivoDuda(4)
                .motivoCompra(5)
                .motivoFelicitaciones(6)
                .motivoCambio(7)
                .hash("abc123")
                .build();
        when(repository.save(stats)).thenReturn(Mono.just(stats));
        StepVerifier.create(adapter.saveStats(stats))
                .expectNext(stats)
                .verifyComplete();
        verify(repository, times(1)).save(stats);
    }
}