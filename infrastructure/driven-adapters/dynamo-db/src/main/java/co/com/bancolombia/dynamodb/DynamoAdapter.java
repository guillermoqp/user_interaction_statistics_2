package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.entity.StatsEntity;
import co.com.bancolombia.dynamodb.entity.StatsEntity;
import co.com.bancolombia.dynamodb.repository.StatsDynamoRepository;
import co.com.bancolombia.model.stats.Stats;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DynamoAdapter implements StatsRepository {

    private final StatsDynamoRepository repository;

    @Override
    public Mono<Stats> saveStats(Stats stats) {
        StatsEntity entity = StatsEntity.builder()
                .timestamp(stats.getTimestamp())
                .totalContactoClientes(stats.getTotalContactoClientes())
                .motivoReclamo(stats.getMotivoReclamo())
                .motivoGarantia(stats.getMotivoGarantia())
                .motivoDuda(stats.getMotivoDuda())
                .motivoCompra(stats.getMotivoCompra())
                .motivoFelicitaciones(stats.getMotivoFelicitaciones())
                .motivoCambio(stats.getMotivoCambio())
                .hash(stats.getHash())
                .build();

        return repository.save(stats).thenReturn(stats);
    }
}