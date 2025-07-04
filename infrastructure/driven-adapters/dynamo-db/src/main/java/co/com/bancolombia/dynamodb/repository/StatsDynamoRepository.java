package co.com.bancolombia.dynamodb.repository;

import co.com.bancolombia.dynamodb.entity.StatsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatsDynamoRepository extends ReactiveCrudRepository<StatsEntity, Long> {
}