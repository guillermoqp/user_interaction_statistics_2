package co.com.bancolombia.dynamodb.repository;

import co.com.bancolombia.dynamodb.entity.StatsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class StatsDynamoRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;

    public Mono<StatsEntity> save(StatsEntity statsEntity) {
        // Obtenemos la tabla en cada llamada, lo cual es seguro y liviano
        DynamoDbAsyncTable<StatsEntity> table = enhancedAsyncClient
                .table("stats", TableSchema.fromBean(StatsEntity.class));
        return Mono.fromFuture(table.putItem(statsEntity))
                .thenReturn(statsEntity);
    }
    /*
    private final DynamoDbAsyncTable<StatsEntity> statsTable;

    public Mono<StatsEntity> save(StatsEntity statsEntity) {
        return Mono.fromFuture(statsTable.putItem(statsEntity)).thenReturn(statsEntity);
    }

    public Mono<StatsEntity> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(statsTable.getItem(key));
    }*/
}