package co.com.bancolombia.dynamodb.repository;

import co.com.bancolombia.model.stats.Stats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class StatsDynamoRepository {
    private final DynamoDbAsyncTable<Stats> statsTable;

    public Mono<Stats> save(Stats stats) {
        return Mono.fromFuture(statsTable.putItem(stats)).thenReturn(stats);
    }

    public Mono<Stats> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(statsTable.getItem(key));
    }
}